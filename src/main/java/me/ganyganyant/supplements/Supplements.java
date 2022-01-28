package me.ganyganyant.supplements;

import me.ganyganyant.supplements.Handlers.PlayerJoining;
import me.ganyganyant.supplements.Handlers.PlayerTeleportSpawn;
import me.ganyganyant.supplements.commands.Discord;
import me.ganyganyant.supplements.commands.KillYourSelf;
import me.ganyganyant.supplements.commands.Spawn;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Supplements extends JavaPlugin implements Listener {

    private static Supplements plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoining(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportSpawn(),this);

        getCommand("kys").setExecutor(new KillYourSelf());
        getCommand("discord").setExecutor(new Discord());
        getCommand("setspawn").setExecutor(new Spawn());
        getCommand("spawn").setExecutor(new Spawn());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Supplements getPlugin() {
        return plugin;
    }
}
