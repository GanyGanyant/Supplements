package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import me.ganyganyant.supplements.files.HomeData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;
import static me.ganyganyant.supplements.commands.Back.TP;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            Supplements plugin = Supplements.getPlugin();
            int homes = plugin.getConfig().getInt("homes");
            HomeData.player player = HomeData.getPlayer(p.getUniqueId().toString());

            if (command.getName().equalsIgnoreCase("sethome")) {
                if (player.numberOfHomes() >= homes){
                    sendFromConfig(p,"maxHomes");
                    return true;
                }
                if (args.length == 0 && player.numberOfHomes() != 0){
                    sendFromConfig(p,"missingHomeName");
                    return true;
                }
                if (args.length == 0 && player.numberOfHomes() == 0) {
                    player.addHome("home", p.getLocation());
                    sendFromConfig(p,"homeSet");
                    return true;
                }
                player.addHome(args[0], p.getLocation());
                sendFromConfig(p,"homeSet");
                return true;

            } else if (command.getName().equalsIgnoreCase("home")) {

                if (player.numberOfHomes() == 0){
                    sendFromConfig(p,"noHomes");
                    return true;
                }
                if (args.length == 0){
                    if (plugin.getConfig().getInt("homeTime") != 0) {
                        // sends a message that teleport will be delayed
                        p.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                        Objects.requireNonNull(    // check if not null
                                                plugin.getConfig().getString("homeWait")   // get message from config
                                        ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("homeTime")))
                                )
                        );  // changes {TIME} to delay in seconds
                        // delays the teleport
                                TP(p,player.firstHome(), plugin.getConfig().getInt("homeTime") * 20L, "tpHome");
                        return true;
                    }
                    TP(p,player.firstHome());
                    sendFromConfig(p,"tpHome");
                    return true;
                }
                if (player.homeByName(args[0]) != null){
                    if (plugin.getConfig().getInt("homeTime") != 0) {
                        // sends a message that teleport will be delayed
                        p.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                        Objects.requireNonNull(    // check if not null
                                                plugin.getConfig().getString("homeWait")   // get message from config
                                        ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("homeTime")))
                                )
                        );  // changes {TIME} to delay in seconds
                        // delays the teleport
                        TP(p,player.homeByName(args[0]), plugin.getConfig().getInt("homeTime") * 20L, "tpHome");
                        return true;
                    }
                    TP(p,player.homeByName(args[0]));
                    sendFromConfig(p,"tpHome");
                    return true;
                }
                sendFromConfig(p,"homeNotFound");
                return true;

            } else if (command.getName().equalsIgnoreCase("delhome")) {
                if (player.numberOfHomes() == 0) {
                    sendFromConfig(p,"noHomeToRemove");
                    return true;
                }
                if (player.numberOfHomes() > 1 && args.length == 0){
                    sendFromConfig(p,"specifyHomeToRemove");
                    return true;
                }
                if (player.numberOfHomes() == 1 && args.length == 0){
                    player.removeFirstHome();
                    sendFromConfig(p,"homeRemoved");
                    return true;
                }
                if (player.homeByName(args[0]) != null){
                    player.removeHome(args[0]);
                    sendFromConfig(p,"homeRemoved");
                    return true;
                }
                sendFromConfig(p,"homeToRemoveNotFound");
                return true;

            } else if (command.getName().equalsIgnoreCase("homes")) {
                if (player.numberOfHomes() != 0) {
                    p.sendMessage(
                    ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                            Objects.requireNonNull(    // check if not null
                                    plugin.getConfig().getString("listHomes")   // get message from config
                            ).replace("{NUM}", String.valueOf(player.numberOfHomes())  // changes {NUM} string to number of homes
                            ).replace("{LIST}", player.listHomes()))
                    );   // change {LIST} string to list of player homes
                    return true;
                }
                sendFromConfig(p, "zeroHomes");
                return true;
            }
        }
        return  true;
    }

}
