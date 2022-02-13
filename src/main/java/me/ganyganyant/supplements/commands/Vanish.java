package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Vanish implements CommandExecutor, Listener {

    Supplements plugin = Supplements.getPlugin();

    private static ArrayList<Player> invisible = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        for (Player p : invisible) {
            player.hidePlayer(plugin, p);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 0 || !player.hasPermission("supplements.feed.player")) {
                if (invisible.contains(player)) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.showPlayer(plugin, player);
                    }
                    invisible.remove(player);
                    sendFromConfig(player, "vanishFalse");
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.hidePlayer(plugin, player);
                    }
                    invisible.add(player);
                    sendFromConfig(player, "vanishTrue");
                }
            }
            return true;
        }
        if (args.length == 0) { return true; }
        if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            sendFromConfig(sender, "playerNotOnline");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        assert target != null;
        if (invisible.contains(target)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, target);
            }
            invisible.remove(target);
            sendFromConfig(target, "vanishFalse");
            sendFromConfig(sender, "vanishFalsePlayer", target);
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(plugin, target);
            }
            invisible.add(target);
            sendFromConfig(target, "vanishTrue");
            sendFromConfig(sender, "vanishTruePlayer", target);
        }
        return true;

    }
}
