package me.ganyganyant.supplements.commands;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

import static me.ganyganyant.supplements.Handlers.PlayerLeash.TPleashed;
import static me.ganyganyant.supplements.Supplements.getPlugin;
import static me.ganyganyant.supplements.Supplements.sendFromConfig;

public class Back implements CommandExecutor, Listener {

    static Supplements plugin = Supplements.getPlugin();

    static HashMap<UUID,Location> lastLocation = new HashMap<>();

    public static void TP (Entity p, Location loc) {
        TP(p,loc,null);
    }

    public static void TP (Entity p, Location loc, String message) {
        plugin.getLogger().info("Teleporting " + p.getName() + " to " + loc.getWorld().getName() + " at " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
        if (p.isInsideVehicle()){
            Entity vehicle = p.getVehicle();
            List<Entity> pass = vehicle.getPassengers();
            if (pass.get(0) != p && !p.hasPermission("supplements.passengerTp")) {
                if (p instanceof Player) {
                    addLastLoc((Player) p);
                }
                p.teleport(loc);
                TPleashed(p.getUniqueId(), loc);
                if (message != null && p instanceof Player) {
                    sendFromConfig(p, message);
                }
                return;
            }

            loc.getChunk().setForceLoaded(true);
            vehicle.getLocation().getChunk().setForceLoaded(true);

            new BukkitRunnable(){
                @Override
                public void run() {
                    vehicle.eject();
                    TP(vehicle, loc);
                    for (Entity passenger : pass ) {
                        if (passenger instanceof Player) {
                            addLastLoc((Player) passenger);
                            TPleashed(passenger.getUniqueId(), loc);
                        }
                        vehicle.addPassenger(passenger);
                    }
                    loc.getChunk().setForceLoaded(false);
                    vehicle.getLocation().getChunk().setForceLoaded(false);
                }
            }.runTaskLater(plugin, 1L);

        } else {
            if (p instanceof Player) {
                addLastLoc((Player) p);
            }
            p.teleport(loc);
            TPleashed(p.getUniqueId(), loc);
        }
        if (message != null && p instanceof Player) {
            sendFromConfig(p, message);
        }
    }

    public static void TP (Entity p, Location loc, Long delay, String message) {
        int x = (int)Math.floor(p.getLocation().getX());
        int y = (int)Math.floor(p.getLocation().getY());
        int z = (int)Math.floor(p.getLocation().getZ());
        new BukkitRunnable(){
            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("cancelTPOnMove")) {
                    int x2 = (int)Math.floor(p.getLocation().getX());
                    int y2 = (int)Math.floor(p.getLocation().getY());
                    int z2 = (int)Math.floor(p.getLocation().getZ());
                    if (x != x2 || y != y2 || z != z2) {
                        sendFromConfig(p, "cancelTPMSG");
                        cancel();
                        return;
                    }
                }
                TP(p,loc,message);
            }
        }.runTaskLater(plugin, delay);
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
