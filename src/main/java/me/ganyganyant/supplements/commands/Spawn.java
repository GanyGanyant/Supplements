package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                String msg = plugin.getConfig().getString("setSpawn");
                assert msg != null;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

            } else if (command.getName().equalsIgnoreCase("spawn")) {
                // Spawn command

                Location spawn = plugin.getConfig().getLocation("spawn");

                if (spawn == null){
                    // Spawn does not exist

                    String msg = plugin.getConfig().getString("spawnNotExist");
                    assert msg != null;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

                }
                else {
                    // teleport to spawn

                    player.teleport(spawn);
                    String msg = plugin.getConfig().getString("teleportedToSpawn");
                    assert msg != null;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

                }
            }
        }
        return true;
    }
}
