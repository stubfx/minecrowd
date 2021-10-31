package com.stubfx.plugin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public Player player = null;
    public BukkitTask myTask = null;

    @Override
    public void onEnable() {
        //noinspection ConstantConditions
        getCommand("clearchunk").setTabCompleter(new MyMaterialTabCompleter());
    }

    public void removeBlock(Block block) {
        block.setType(Material.STONE);
    }

    @Override
    public void onDisable() {
        clearTask();
    }


    public void clearTask() {
        if (myTask != null) {
            myTask.cancel();
        }
    }


    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        clearTask();
        player = ((Player) sender);
        if (player == null) {
            return false;
        }
        // in this case we found me.
        checkCommand(command, args);
        return false;
    }

    private void checkCommand(Command command, String[] args) {
        Location location = player.getLocation();
        // command received, is this our command tho?
        switch (command.getName().toLowerCase()) {
            case "draw" -> draw();
            case "jesus" -> jesus();
            case "drill" -> drill();
            case "clearchunk" -> clearchunk(command, args);
            case "stopcommand" -> clearTask();
        }
    }

    private void clearchunk(Command command, String[] arg) {
        Location location = player.getLocation();
        int playerHeight = ((int) location.getY());
        Chunk chunk = location.getChunk();
        myTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<Material> materialsToExclude = new ArrayList<>();
                for (String argument : arg) {
                    materialsToExclude.add(Material.valueOf(argument.toUpperCase()));
                }
                if (player != null) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = playerHeight; y > 0; y--) {
                                // this runs for any selected block of the chunk
                                // we need to convert to air, only if they are not ores
                                Block block = chunk.getBlock(x, y, z);
                                // check if block type is not contained in the exclude list
                                if (!materialsToExclude.contains(block.getType())) {
                                    // here if the block is not what we are looking for
                                    // so we convert that into air
                                    block.setType(Material.AIR);
                                }
                            }
                        }
                    }
                }
            }
        }.runTask(this);
    }

    private void drill() {
        myTask = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                if (player != null) {
                    for (int z = 0; z < 3; z++) {
                        for (int x = 0; x < 3; x++) {
                            for (int y = 0; y < 2; y++) {
                                location.clone().add(x - 1, y, z - 1).getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }
                location.getBlock().setType(Material.TORCH);
            }
        }.runTaskTimer(this, 1, 1);
    }

    private void jesus() {
        myTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (player != null) {
                    player.getLocation().add(0, -1, 0).getBlock().setType(Material.STONE);
                }
            }
        }.runTaskTimer(this, 1, 1);
    }

    private void draw() {
        // in this case we want to activate the command.
        myTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (player != null) {
                    Block targetBlockExact = player.getTargetBlockExact(20);
                    if (targetBlockExact != null) {
                        removeBlock(targetBlockExact);
                    }
                }
            }
        }.runTaskTimer(this, 1, 1);
    }


}