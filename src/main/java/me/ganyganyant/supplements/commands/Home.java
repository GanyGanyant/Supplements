package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import me.ganyganyant.supplements.files.HomeData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

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
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("maxHomes"))));
                    return true;
                }
                if (args.length == 0 && player.numberOfHomes() != 0){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("missingHomeName"))));
                    return true;
                }
                if (args.length == 0 && player.numberOfHomes() == 0) {
                    player.addHome("home", p.getLocation());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeSet"))));
                    return true;
                }
                player.addHome(args[0], p.getLocation());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeSet"))));
                return true;

            } else if (command.getName().equalsIgnoreCase("home")) {

                if (player.numberOfHomes() == 0){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("noHomes"))));
                    return true;
                }
                if (args.length == 0){
                    p.teleport(player.firstHome());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("tpHome"))));
                    return true;
                }
                if (player.homeByName(args[0]) != null){
                    p.teleport(player.homeByName(args[0]));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("tpHome"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeNotFound"))));
                return true;

            } else if (command.getName().equalsIgnoreCase("delhome")) {
                if (player.numberOfHomes() == 0) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("noHomeToRemove"))));
                    return true;
                }
                if (player.numberOfHomes() > 1 && args.length == 0){
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("specifyHomeToRemove"))));
                    return true;
                }
                if (player.numberOfHomes() == 1 && args.length == 0){
                    player.removeFirstHome();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeRemoved"))));
                    return true;
                }
                if (player.homeByName(args[0]) != null){
                    player.removeHome(args[0]);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeRemoved"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Objects.requireNonNull(plugin.getConfig().getString("homeToRemoveNotFound"))));
                return true;


            }
        }
        return  true;
    }
}
