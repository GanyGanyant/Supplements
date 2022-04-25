package me.ganyganyant.supplements.tabComplete;

import me.ganyganyant.supplements.files.HomeData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> homeList = HomeData.getPlayer(((Player) sender).getUniqueId().toString()).HomesList();
            return homeList;
        }
        return null;
    }
}
