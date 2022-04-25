package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import me.ganyganyant.supplements.files.Warps;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;
import static me.ganyganyant.supplements.commands.Back.TP;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Supplements plugin = Supplements.getPlugin();
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("warp")) {
                if (args.length == 0) {
                    sendFromConfig(player, "specifyWarp");
                    return true;
                }
                if (Warps.getWarp(args[0]) != null){
                    if (plugin.getConfig().getInt("warpTime") != 0) {
                        // sends a message that teleport will be delayed
                        player.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                        Objects.requireNonNull(    // check if not null
                                                plugin.getConfig().getString("warpWait")   // get message from config
                                        ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("warpTime")))));  // changes {TIME} to delay in seconds
                        // delays the teleport
                        TP(player,Warps.getWarp(args[0]).getLocation(), plugin.getConfig().getInt("warpTime") * 20L,"tpWarp");
                        return true;
                    }
                    TP(player,Warps.getWarp(args[0]).getLocation());
                    sendFromConfig(player,"tpWarp");
                    return true;
                }
                sendFromConfig(player, "warpNotFound");
                // WARP
                return true;
            }
            else if (command.getName().equalsIgnoreCase("setwarp")) {
                if (args.length == 0) {
                    sendFromConfig(player,"missingWarpName");
                    return true;
                }
                if (Warps.getWarp(args[0]) != null) {
                    sendFromConfig(player, "warpAlreadyExists");
                    return true;
                }
                Warps.addWarp(args[0], player.getLocation(), player.getUniqueId());
                sendFromConfig(player,"warpSet");
                return true;
                }
            else if (command.getName().equalsIgnoreCase("delwarp")) {
                if (args.length == 0){
                    sendFromConfig(player,"specifyWarpToRemove");
                    return true;
                }
                if (Warps.getWarp(args[0]) != null){
                    if (Warps.getWarp(args[0]).getOwner().equals(player.getUniqueId()) || player.hasPermission("supplements.warp.delany")){
                        Warps.removeWarp(args[0]);
                        sendFromConfig(player,"warpRemoved");
                        return true;
                    }
                    sendFromConfig(player,"cannotRemoveWarp");
                    return true;
                }
                sendFromConfig(player,"warpToRemoveNotFound");
                return true;
            }
            else if (command.getName().equalsIgnoreCase("warps")) {
                if (Warps.warps.size() != 0) {
                    player.sendMessage(
                            ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                    Objects.requireNonNull(    // check if not null
                                            plugin.getConfig().getString("listWarps")   // get message from config
                                    ).replace("{NUM}", String.valueOf(Warps.warps.size())  // changes {NUM} string to number of warps
                                    ).replace("{LIST}", Warps.listWarps())));   // change {LIST} string to list of player warps
                    return true;
                }
                sendFromConfig(player, "zeroWarps");
                return true;
            }
        }
        return true;
    }
}
