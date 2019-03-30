package dev.nuer.sand.command;

import dev.nuer.sand.SandWands;
import dev.nuer.sand.file.LoadProvidedFiles;
import dev.nuer.sand.gui.SandGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SandCommand implements CommandExecutor {
    //Register the class so that the command will work
    public SandCommand(SandWands pl) {
        this.pl = pl;
    }

    //Register the main class
    private Plugin pl = SandWands.getPlugin(SandWands.class);
    //Register LoadProvidedFiles instance
    private LoadProvidedFiles lpf = ((SandWands) pl).getFiles();

    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if (c.getName().equalsIgnoreCase("sand") || c.getName().equalsIgnoreCase("sw")) {
            if (args.length == 0) {
                if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                    if (s instanceof Player) {
                        if (s.hasPermission("sand.gui")) {
                         SandGui i = new SandGui();
                            i.gui((Player) s);
                            for (String line : lpf.getMessages().getStringList("open-gui")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    } else {
                        pl.getLogger().info("The GUI can only be viewed in game.");
                    }
                } else {
                    if (s instanceof Player) {
                        if (s.hasPermission("sand.help")) {
                            for (String line : lpf.getMessages().getStringList("help")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    } else {
                        pl.getLogger().info("The help message can only be seen using game chat.");
                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                    if (s instanceof Player) {
                        if (s.hasPermission("sand.help")) {
                            for (String line : lpf.getMessages().getStringList("help")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    } else {
                        pl.getLogger().info("The help message can only be seen using game chat.");
                    }
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                    if (s instanceof Player) {
                        if (s.hasPermission("sand.reload")) {
                            lpf.reload();
                            for (String line : lpf.getMessages().getStringList("reload")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    } else {
                        lpf.reload();
                        pl.getLogger().info("You have successfully reloaded all files.");
                    }
                } else {
                    if (s instanceof Player) {
                        for (String line : lpf.getMessages().getStringList("invalid-command")) {
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                        }
                    } else {
                        pl.getLogger().info("The command you entered is invalid.");
                    }
                }
            } else if (args.length == 5 || args.length == 6) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                    if (s instanceof Player) {
                        if (!s.hasPermission("sand.give")) {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                            return true;
                        }
                    }
                    // Initializing variables for the tool
                    Player target = null;
                    int size = 0;
                    int amount = 1;
                    int x = 0;
                    boolean infinite = false;
                    String item = args[2].toUpperCase();
                    String level = "sand-wand-" + args[3];
                    try {
                        target = pl.getServer().getPlayer(args[1]);
                    } catch (Exception e) {
                        if (s instanceof Player) {
                            for (String line : lpf.getMessages().getStringList("invalid-player")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            pl.getLogger().info("The command you entered is invalid");
                        }
                    }
                    try {
                        size = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        if (s instanceof Player) {
                            for (String line : lpf.getMessages().getStringList("invalid-level")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            pl.getLogger().info("The level of sand you entered is invalid, enter a int between 1-9.");
                        }
                    }
                    try {
                        Material.valueOf(item);
                    } catch (Exception e) {
                        if (s instanceof Player) {
                            for (String line : lpf.getMessages().getStringList("invalid-item")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            pl.getLogger().info("The item you entered is invalid.");
                        }
                    }
                    if (args[4].equalsIgnoreCase("i") || args[4].equalsIgnoreCase("infinite")) {
                        infinite = true;
                    } else {
                        try {
                            Integer.parseInt(args[4]);
                        } catch (Exception e) {
                            for (String line : lpf.getMessages().getStringList("invalid-uses")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    }
                    if (args.length == 6) {
                        try {
                            amount = Integer.parseInt(args[5]);
                        } catch (Exception e) {
                            if (s instanceof Player) {
                                for (String line : lpf.getMessages().getStringList("invalid-amount")) {
                                    s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                                }
                            } else {
                                pl.getLogger().info("The amount you entered is invalid.");
                            }
                        }
                    }
                    if (size <= 9 && size >= 1) {
                        while (x < amount) {
                            ItemStack tool = new ItemStack(Material.valueOf(item));
                            ItemMeta toolMeta = tool.getItemMeta();
                            List<String> toolLore = new ArrayList<>();
                            toolMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                                    lpf.getSand().getString(level + ".name")));
                            if (infinite) {
                                for (String lore : lpf.getSand().getStringList(level + ".lore")) {
                                    toolLore.add(ChatColor.translateAlternateColorCodes('&', lore).replace("%uses%",
                                            String.valueOf(lpf.getSand().getString(level + ".infinite-use-id"))));
                                }
                            } else {
                                for (String lore : lpf.getSand().getStringList(level + ".lore")) {
                                    toolLore.add(ChatColor.translateAlternateColorCodes('&', lore).replace("%uses%",
                                            args[4]));
                                }
                            }
                            for (String ench : lpf.getSand().getStringList(level + ".enchantments")) {
                                String[] parts = ench.split("-");
                                toolMeta.addEnchant(Enchantment.getByName(parts[0]), Integer.parseInt(parts[1]), true);
                            }
                            toolMeta.setLore(toolLore);
                            tool.setItemMeta(toolMeta);
                            target.getInventory().addItem(tool);
                            x++;
                        }
                    } else {
                        if (s instanceof Player) {
                            for (String line : lpf.getMessages().getStringList("invalid-level")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            pl.getLogger().info("The level you entered is invalid, the level must be between 1-9.");
                        }
                    }
                } else {
                    if (s instanceof Player) {
                        if (s.hasPermission("sand.give")) {
                            for (String line : lpf.getMessages().getStringList("invalid-command")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        } else {
                            for (String line : lpf.getMessages().getStringList("no-permission")) {
                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                            }
                        }
                    } else {
                        pl.getLogger().info("the command you entered is invalid.");
                    }
                }
            } else {
                if (s instanceof Player) {
                    if (s.hasPermission("sand.give") || s.hasPermission("sand.reload")
                            || s.hasPermission("sand.help")) {
                        for (String line : lpf.getMessages().getStringList("invalid-command")) {
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                        }
                    } else {
                        for (String line : lpf.getMessages().getStringList("no-permission")) {
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                        }
                    }
                } else {
                    pl.getLogger().info("the command you entered is invalid.");
                }
            }
        }
        return true;
    }
}
