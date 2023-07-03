package me.ganyganyant.supplements;

import me.ganyganyant.supplements.Handlers.PlayerJoining;
import me.ganyganyant.supplements.Handlers.PlayerLeash;
import me.ganyganyant.supplements.Handlers.PlayerTeleportSpawn;
import me.ganyganyant.supplements.commands.*;
import me.ganyganyant.supplements.files.HomeData;
import me.ganyganyant.supplements.files.ToggleTP;
import me.ganyganyant.supplements.files.Warps;
import me.ganyganyant.supplements.tabComplete.HomeTab;
import me.ganyganyant.supplements.tabComplete.WarpTab;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Objects;

public final class Supplements extends JavaPlugin {

    private static Supplements plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // plugin data
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getLogger().info("loading data!");
                // plugin config
                getConfig().options().copyDefaults();
                saveDefaultConfig();
                try {
                    Spawn.load();
                    HomeData.load();
                    ToggleTP.load();
                    Warps.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(this.plugin, 1L);

        // events
        getServer().getPluginManager().registerEvents(new PlayerJoining(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportSpawn(),this);
        getServer().getPluginManager().registerEvents(new Back(),this);
        getServer().getPluginManager().registerEvents(new Vanish(),this);
        getServer().getPluginManager().registerEvents(new PlayerLeash(), this);

        // commands
        // misc
        getCommand("kys").setExecutor(new KillYourSelf());
        getCommand("discord").setExecutor(new Discord());
        // spawn
        getCommand("setspawn").setExecutor(new Spawn());
        getCommand("spawn").setExecutor(new Spawn());
        // home
        getCommand("home").setExecutor(new Home());
        getCommand("sethome").setExecutor(new Home());
        getCommand("delhome").setExecutor(new Home());
        getCommand("homes").setExecutor(new Home());
        // tpa
        getCommand("tpa").setExecutor(new Teleport());
        getCommand("tpaccept").setExecutor(new Teleport());
        getCommand("tpdeny").setExecutor(new Teleport());
        getCommand("tpcancel").setExecutor(new Teleport());
        getCommand("tpall").setExecutor(new Teleport());
        getCommand("tptoggle").setExecutor(new Teleport());
        // back
        getCommand("back").setExecutor(new Back());
        // warp
        getCommand("warp").setExecutor(new Warp());
        getCommand("setwarp").setExecutor(new Warp());
        getCommand("delwarp").setExecutor(new Warp());
        getCommand("warps").setExecutor(new Warp());
        // VIP lvl commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("god").setExecutor(new God());
        getCommand("heal").setExecutor(new Heal());
        getCommand("feed").setExecutor(new Feed());
        getCommand("vanish").setExecutor(new Vanish());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("fix").setExecutor(new Fix());
        getCommand("fixall").setExecutor(new Fix());
        getCommand("milk").setExecutor(new Milk());

        // TAB complete
        // home
        getCommand("home").setTabCompleter(new HomeTab());
        getCommand("delhome").setTabCompleter(new HomeTab());
        // warp
        getCommand("warp").setTabCompleter(new WarpTab());
        getCommand("delwarp").setTabCompleter(new WarpTab());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HomeData.save();
        ToggleTP.save();
        Warps.save();
    }

    public static Supplements getPlugin() {
        return plugin;
    }

    // reads message from config adds color and sends it to player
    public static void sendFromConfig(Player user, String path ){
        user.sendMessage( // send edited message to user
                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                        Objects.requireNonNull( // check if not null
                                plugin.getConfig().getString(path)))); // get message from config
    }

    public static void sendFromConfig(CommandSender user, String path ){
        user.sendMessage( // send edited message to user
                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                        Objects.requireNonNull( // check if not null
                                plugin.getConfig().getString(path)))); // get message from config
    }

    public static void sendFromConfig(CommandSender user, String path, Player name ){
        user.sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(
                                plugin.getConfig().getString(path))
                                .replace("{PLAYER}",name.getDisplayName())));
    }
}
