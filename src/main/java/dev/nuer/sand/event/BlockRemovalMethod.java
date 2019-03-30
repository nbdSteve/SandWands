package dev.nuer.sand.event;

import dev.nuer.sand.file.LoadProvidedFiles;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Class that handles removing the stacks of blocks, this adds the delay required
 */
public class BlockRemovalMethod {

    /**
     * Runnable that actually removes the blocks
     *
     * @param toolType       the sand wand to get from the sand.yml
     * @param player         the player breaking
     * @param blocksToRemove the array of blocks that should be removed
     * @param plugin         providing plugin
     * @param files          plugin files instance
     */
    public static void genericRemoval(String toolType, Player player,
                                      ArrayList<Block> blocksToRemove,
                                      Plugin plugin,
                                      LoadProvidedFiles files) {
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < blocksToRemove.size()) {
                    //If the block isn't one that is supposed to be broken stop the task.
                    if (CheckBreakableBlock.checkBlock(blocksToRemove.get(index).getType().toString(), files)) {
                        if (files.getConfig().getBoolean("enable-natural-drops")) {
                            blocksToRemove.get(index).breakNaturally();
                        } else {
                            for (ItemStack drop : blocksToRemove.get(index).getDrops()) {
                                player.getInventory().addItem(drop);
                            }
                            blocksToRemove.get(index).setType(Material.AIR);
                            index++;
                        }
                    } else {
                        //If the block isn't sand / other it shouldn't be removed
                        this.cancel();
                    }
                } else {
                    //When all the blocks have been changed, cancel the event
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, files.getSand().getInt(toolType + ".break-delay"));
    }
}
