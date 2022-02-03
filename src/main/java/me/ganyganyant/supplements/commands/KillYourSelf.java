package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class KillYourSelf implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if (sender instanceof Player){
            Player p = (Player) sender;
            p.setHealth(0.0d);
        } else if (sender instanceof ConsoleCommandSender) {
            Supplements.getPlugin().getLogger().log(Level.INFO, "KYS NIGGA");
        }
        return true;
    }
}
