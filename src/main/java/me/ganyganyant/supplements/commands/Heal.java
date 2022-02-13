package me.ganyganyant.supplements.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 0 || !player.hasPermission("supplements.heal.player")){
                player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
                sendFromConfig(player, "healed");
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
        target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        sendFromConfig(target, "healed");
        sendFromConfig(sender, "healedPlayer", target);
        return true;
    }
}
