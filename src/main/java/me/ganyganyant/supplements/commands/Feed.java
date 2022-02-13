package me.ganyganyant.supplements.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Feed implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 0 || !player.hasPermission("supplements.feed.player")){
                player.setFoodLevel(20);
                sendFromConfig(player, "fed");
                return true;
            }
        }
        if (args.length == 0) { return true; }
        if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            sendFromConfig(sender, "playerNotOnline");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        assert target != null;
        target.setFoodLevel(20);
        sendFromConfig(target, "fed");
        sendFromConfig(sender, "fedPlayer", target);
        return true;
    }
}
