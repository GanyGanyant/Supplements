package me.ganyganyant.supplements;

import me.ganyganyant.supplements.Handlers.PlayerJoining;
import me.ganyganyant.supplements.Handlers.PlayerTeleportSpawn;
import me.ganyganyant.supplements.commands.*;
import me.ganyganyant.supplements.files.HomeData;
import me.ganyganyant.supplements.files.ToggleTP;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class Supplements extends JavaPlugin {

    private static Supplements plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        // plugin config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // plugin data
        try {
            HomeData.load();
            ToggleTP.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // events
        getServer().getPluginManager().registerEvents(new PlayerJoining(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportSpawn(),this);
        getServer().getPluginManager().registerEvents(new Back(),this);

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
        // [SPAWN, HOME, TPA]

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HomeData.save();
    }

    public static Supplements getPlugin() {
        return plugin;
    }

    // reads message from config adds color and sends it to player
    public static void sendFromConfig(Player user, String path ){
        user.sendMessage( // send edited message to user
                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                        Objects.requireNonNull( // check if not null
                                Supplements.getPlugin().getConfig().getString(path)))); // get message from config
    }
}
