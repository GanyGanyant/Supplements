package me.ganyganyant.supplements.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventHandlers implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()){
            event.setJoinMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.YELLOW + " se připojil na server.");
        }
        else {
            event.setJoinMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.YELLOW + " je nový na serveru, zdravíme tě!");
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        event.setQuitMessage(ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.YELLOW + " opustil server.");
    }

}
