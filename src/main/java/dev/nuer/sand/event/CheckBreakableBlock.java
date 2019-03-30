package dev.nuer.sand.event;

import dev.nuer.sand.file.LoadProvidedFiles;

import java.util.ArrayList;
import java.util.List;

public class CheckBreakableBlock {

    /**
     * Method to check whether the block should be broken or not.
     *
     * @param blockToCheck the block being checked
     * @return boolean, true if the block should be broken
     */
    public static boolean checkBlock(String blockToCheck, LoadProvidedFiles lpf) {
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
