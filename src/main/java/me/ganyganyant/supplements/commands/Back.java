package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Back implements CommandExecutor, Listener {

    static HashMap<UUID,Location> lastLocation = new HashMap<>();

    public static void TP (Player p, Location loc) {
        lastLocation.put(p.getUniqueId(),p.getLocation());
        p.teleport(loc);
    }

    public static void addLastLoc(Player p, Location loc){
        lastLocation.put(p.getUniqueId(),loc);
    }
    public static void addLastLoc(Player p){
        lastLocation.put(p.getUniqueId(),p.getLocation());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        addLastLoc(event.getEntity());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            Supplements plugin = Supplements.getPlugin();

            if (lastLocation.containsKey(player.getUniqueId())) {
                if (plugin.getConfig().getInt("backTime") != 0) {
                    // sends a message that teleport will be delayed
                    player.sendMessage(
                            ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                    Objects.requireNonNull(    // check if not null
                                            plugin.getConfig().getString("backWait")   // get message from config
                                    ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("backTime")))));  // changes {TIME} to delay in seconds
                    // delays the teleport
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            TP(player, lastLocation.get(player.getUniqueId()));
                            sendFromConfig(player, "backTP");
                        }
                    }.runTaskLater(plugin, plugin.getConfig().getInt("backTime") * 20L);
                    return true;
                }
                TP(player, lastLocation.get(player.getUniqueId()));
                sendFromConfig(player, "backTP");
                return true;
            }
            sendFromConfig(player,"noBackTP");
            return true;
        }
        return true;
    }

}
