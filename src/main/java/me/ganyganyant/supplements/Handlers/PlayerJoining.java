package me.ganyganyant.supplements.Handlers;

import me.ganyganyant.supplements.Supplements;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoining implements Listener {

    Supplements plugin = Supplements.getPlugin();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        boolean unique = plugin.getConfig().getBoolean("uniqueFirstJoin");
        Player player = event.getPlayer();
        String NAME = player.getDisplayName();
        if (!player.hasPlayedBefore() && unique){
            String message = plugin.getConfig().getString("uniqueJoin");
            assert message != null;
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',message.replace("{PLAYER}", NAME)));
        }
        else {
            String message = plugin.getConfig().getString("playerJoin");
            assert message != null;
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',message.replace("{PLAYER}", NAME)));
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        String NAME = event.getPlayer().getDisplayName();
        String message = plugin.getConfig().getString("playerLeft");
        assert message != null;
        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&',message.replace("{PLAYER}", NAME)));
    }

}
