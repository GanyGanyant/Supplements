package me.ganyganyant.supplements.tabComplete;

import me.ganyganyant.supplements.files.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (command.getName().equalsIgnoreCase("warp")) {
                List<String> warpList = Warps.WarpsList();
                return warpList;
            }
            if (command.getName().equalsIgnoreCase("delwarp")) {
                if (sender.hasPermission("supplements.warp.delany")) {
                    List<String> warpList = Warps.WarpsList();
                    return warpList;
                }
                List<String> warpList = Warps.WarpsList(((Player)sender).getUniqueId());
                return warpList;
            }
        }
        return null;
    }
}
