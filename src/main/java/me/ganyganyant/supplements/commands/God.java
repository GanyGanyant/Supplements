package me.ganyganyant.supplements.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class God implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 0 || !player.hasPermission("supplements.god.player")){
                if (player.isInvulnerable()) {
                    player.setInvulnerable(false);
                    sendFromConfig(player, "godFalse");
                } else {
                    player.setInvulnerable(true);
                    sendFromConfig(player, "godTrue");
                }
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
        if (target.isInvulnerable()) {
            target.setInvulnerable(false);
            sendFromConfig(target, "godFalse");
            sendFromConfig(sender, "godFalsePlayer", target);
        } else {
            target.setInvulnerable(true);
            sendFromConfig(target, "godTrue");
            sendFromConfig(sender, "godTruePlayer", target);
        }
        return true;
    }
}
