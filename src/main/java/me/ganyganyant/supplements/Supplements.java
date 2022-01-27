package me.ganyganyant.supplements;

import me.ganyganyant.supplements.Handlers.EventHandlers;
import me.ganyganyant.supplements.commands.Discord;
import me.ganyganyant.supplements.commands.KillYourSelf;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Supplements extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EventHandlers(),this);
        getCommand("kys").setExecutor(new KillYourSelf());
        getCommand("discord").setExecutor(new Discord());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
