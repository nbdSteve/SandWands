package com.nbdsteve.sandwands.event;

import com.nbdsteve.sandwands.SandWands;
import com.nbdsteve.sandwands.file.LoadProvidedFiles;
import com.nbdsteve.sandwands.support.Factions;
import com.nbdsteve.sandwands.support.MassiveCore;
import com.nbdsteve.sandwands.support.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Event called when the player right clicks a block, most of the code is not executed unless they are using the
 * tool. The tool check is done first to reduce memory usage.
 */
public class BlockClick implements Listener {
    //Register the main class
    private Plugin pl = SandWands.getPlugin(SandWands.class);
    //Register LoadProvidedFiles class
    private LoadProvidedFiles lpf = ((SandWands) pl).getFiles();
    //Get the cooldown hashmap from the main class
    private HashMap<UUID, Long> toolCDT = ((SandWands) pl).getSandCDT();

    /**
     * All code for the event is stored in this method.
     *
     * @param e the event, cannot be null.
     */
    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        //Get the player
        Player p = e.getPlayer();
        //Check that it is the right event
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            //Check that the player has the sandwand in their hand
            if (p.getInventory().getItemInHand().hasItemMeta()) {
                if (p.getInventory().getItemInHand().getItemMeta().hasLore()) {
                    ItemMeta toolMeta = p.getInventory().getItemInHand().getItemMeta();
                    List<String> toolLore = toolMeta.getLore();
                    String toolType;
                    //Get the level of sand from the tool lore
                    if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-1.unique")))) {
                        toolType = "sand-wand-1";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-2.unique")))) {
                        toolType = "sand-wand-2";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-3.unique")))) {
                        toolType = "sand-wand-3";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-4.unique")))) {
                        toolType = "sand-wand-4";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-5.unique")))) {
                        toolType = "sand-wand-5";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-6.unique")))) {
                        toolType = "sand-wand-6";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-7.unique")))) {
                        toolType = "sand-wand-7";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-8.unique")))) {
                        toolType = "sand-wand-8";
                    } else if (toolLore.contains(
                            ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString("sand-wand-9.unique")))) {
                        toolType = "sand-wand-9";
                    } else {
                        return;
                    }
                    boolean wg = false;
                    boolean fac = false;
                    //Figure out which plugins are being used and what to support
                    if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                        wg = true;
                        if (!WorldGuard.allowsBreak(e.getClickedBlock().getLocation())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    if (Bukkit.getPluginManager().getPlugin("MassiveCore") != null) {
                        MassiveCore.canBreakBlock(p, e.getClickedBlock());
                        fac = true;
                        if (!MassiveCore.canBreakBlock(p, e.getClickedBlock())) {
                            e.setCancelled(true);
                            return;
                        }
                    } else if (Bukkit.getServer().getPluginManager().getPlugin("Factions") != null) {
                        fac = true;
                        if (!Factions.canBreakBlock(p, e.getClickedBlock())) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                    int cooldown = lpf.getSand().getInt(toolType + ".cooldown");
                    //Make sure that the block can be broken
                    if (checkBlock(e.getClickedBlock().getType().toString())) {
                        if (cooldown != -1 && cooldown >= 0) {
                            if (toolCDT.containsKey(p.getUniqueId())) {
                                long CDT = ((toolCDT.get(p.getUniqueId()) / 1000) + cooldown)
                                        - (System.currentTimeMillis() / 1000);
                                if (CDT > 0) {
                                    for (String line : lpf.getMessages().getStringList("cooldown")) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', line)
                                                .replace("%cooldown%", String.valueOf(CDT)));
                                    }
                                } else {
                                    toolCDT.remove(p.getUniqueId());
                                }
                                e.setCancelled(true);
                                return;
                            } else {
                                toolCDT.put(p.getUniqueId(), System.currentTimeMillis());
                            }
                        }
                        //Get the blocks location
                        int x = e.getClickedBlock().getX();
                        int z = e.getClickedBlock().getZ();
                        int y = 255;
                        //Remove the blocks from the top to the bottom
                        while (y >= lpf.getSand().getInt(toolType + ".lowest-break-level")) {
                            String current = p.getWorld().getBlockAt(x, y, z).getType().toString();
                            if (wg && !WorldGuard.allowsBreak(e.getClickedBlock().getRelative(x, y, z).getLocation())) {
                                y--;
                            } else if (fac && !Factions.canBreakBlock(p, e.getClickedBlock().getRelative(x, y, z))) {
                                y--;
                            } else if (!checkBlock(current)) {
                                y--;
                            } else if (lpf.getConfig().getBoolean("enable-natural-drops")) {
                                p.getWorld().getBlockAt(x, y, z).breakNaturally();
                                y--;
                            } else {
                                for (ItemStack item : p.getWorld().getBlockAt(x, y, z).getDrops()) {
                                    p.getInventory().addItem(item);
                                }
                                p.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                                y--;
                            }
                        }
                        //Get the id for the uses line
                        String uID = ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString(toolType + ".uses-line-id"));
                        try {
                            for (int i = 0; i < toolMeta.getLore().size(); i++) {
                                String l = toolMeta.getLore().get(i);
                                if (l.contains(uID)) {
                                    String uses = "";
                                    for (int m = 0; m < toolLore.get(i).length(); m++) {
                                        if (Character.isDigit(toolLore.get(i).charAt(m))) {
                                            if (m != 0) {
                                                if (toolLore.get(i).charAt(m - 1) != ChatColor.COLOR_CHAR) {
                                                    uses += toolLore.get(i).charAt(m);
                                                }
                                            } else {
                                                uses += toolLore.get(i).charAt(m);
                                            }
                                        }
                                    }
                                    int temp = Integer.parseInt(uses) - 1;
                                    if (temp <= 0) {
                                        p.getInventory().removeItem(p.getItemInHand());
                                        for (String message : lpf.getMessages().getStringList("break")) {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    } else {
                                        String uI = ChatColor.translateAlternateColorCodes('&', lpf.getSand().getString(toolType + ".uses-id").replace("%uses%", String.valueOf(temp)));
                                        toolLore.set(i, (uID + " " + uI));
                                        toolMeta.setLore(toolLore);
                                        p.getItemInHand().setItemMeta(toolMeta);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            //Do nothing, tool isn't recording uses
                        }
                    }
                }
            }
        }
    }

    private boolean checkBlock(String blockToCheck) {
        List<String> blocks = new ArrayList<>();
        if (lpf.getConfig().getBoolean("enable-block-whitelist")) {
            for (String line : lpf.getSand().getStringList("whitelisted-block-list")) {
                String block = line.toUpperCase();
                blocks.add(block);
            }
        }
        return blocks.contains(blockToCheck);
    }
}
