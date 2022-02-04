package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        Supplements plugin = Supplements.getPlugin();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("setspawn")) {
                // Set spawn command

                Location spawn = player.getLocation();
                plugin.getConfig().set("spawn", spawn);
                plugin.saveConfig();
                sendFromConfig(player, "setSpawn");
            } else if (command.getName().equalsIgnoreCase("spawn")) {
                // Spawn command

                Location spawn = plugin.getConfig().getLocation("spawn");

                if (spawn == null){
                    // Spawn does not exist

                    sendFromConfig(player, "spawnNotExist");
                }
                else {
                    // teleport to spawn

                    if (plugin.getConfig().getInt("spawnTime") != 0) {
                        // sends a message that teleport will be delayed
                        player.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                        Objects.requireNonNull(    // check if not null
                                                plugin.getConfig().getString("spawnWait")   // get message from config
                                        ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("spawnTime")))));  // changes {TIME} to delay in seconds
                        // delays the teleport
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                player.teleport(spawn);
                                sendFromConfig(player, "teleportedToSpawn");
                            }
                        }.runTaskLater(plugin, plugin.getConfig().getInt("homeTime") * 20L);
                        return true;
                    }
                    player.teleport(spawn);
                    sendFromConfig(player, "teleportedToSpawn");
                }
            }
        }
        return true;
    }
}
