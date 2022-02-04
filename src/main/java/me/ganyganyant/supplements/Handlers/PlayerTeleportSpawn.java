package me.ganyganyant.supplements.Handlers;

import me.ganyganyant.supplements.Supplements;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerTeleportSpawn implements Listener {

    Supplements plugin = Supplements.getPlugin();
    Location spawn = plugin.getConfig().getLocation("spawn");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        boolean teleportAll = plugin.getConfig().getBoolean("teleportOnJoin");
        boolean teleportNew = plugin.getConfig().getBoolean("teleportNewPlayers");
        Player player = event.getPlayer();

        if ((teleportNew && !player.hasPlayedBefore() || teleportAll) && spawn != null){
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        boolean noRespawnPoint = plugin.getConfig().getBoolean("noRespawnPoint");
        boolean always = plugin.getConfig().getBoolean("always");
        boolean Bed = event.isBedSpawn();
        boolean Anchor = event.isAnchorSpawn();

        if ((noRespawnPoint && (!Bed && !Anchor) || always) && spawn != null){
            event.setRespawnLocation(spawn);
        }

    }

}
