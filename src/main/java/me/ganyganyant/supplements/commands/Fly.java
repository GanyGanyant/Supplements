package me.ganyganyant.supplements.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Fly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                sendFromConfig(player, "flyFalse");
            } else {
                player.setAllowFlight(true);
                sendFromConfig(player, "flyTrue");
            }
            return true;
        }
        return true;
    }
}
