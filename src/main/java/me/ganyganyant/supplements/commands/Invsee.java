package me.ganyganyant.supplements.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Invsee implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){

            Player player = (Player) sender;

            if (args.length == 0){
                sendFromConfig(player, "noArgsInvsee");
                return true;
            }
            if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                sendFromConfig(player, "playerNotOnlineInvsee");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            assert target != null;
            if (target.getUniqueId().equals(player.getUniqueId())) {
                sendFromConfig(player, "noSelfInvsee");
                return true;
            }
            player.openInventory(target.getInventory());

            return true;
        }
        return true;
    }
}
