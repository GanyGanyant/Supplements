package me.ganyganyant.supplements.Handlers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.util.function.Predicate;

public class PlayerLeash implements Listener {

    public static HashMap<UUID, Vector<LivingEntity>> leashedEntities = new HashMap<>();
    @EventHandler
    public void onPlayerLeash(PlayerLeashEntityEvent event){
        UUID playerUUID = event.getPlayer().getUniqueId();
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (event.getPlayer() == event.getLeashHolder()) {
            addEntity(playerUUID, entity);
        }
        else {
            removeEntity(playerUUID, entity);
        }
    }

    @EventHandler
    public void onPlayerUnleash(PlayerUnleashEntityEvent event){
        UUID playerUUID = event.getPlayer().getUniqueId();
        LivingEntity entity = (LivingEntity) event.getEntity();
        removeEntity(playerUUID, entity);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event){
        UUID playerUUID = event.getPlayer().getUniqueId();
        leashedEntities.remove(playerUUID);
    }

    static void addEntity(UUID playerUUID, LivingEntity entity) {
        Vector<LivingEntity> entities = leashedEntities.get(playerUUID);
        if (entities == null) {
            entities = new Vector<>();
        }
        if (!entities.contains(entity)){
        entities.add(entity);
        leashedEntities.put(playerUUID, entities);}
    }
    static void removeEntity(UUID playerUUID, LivingEntity entity) {
        Vector<LivingEntity> entities = leashedEntities.get(playerUUID);
        entities.remove(entity);
        leashedEntities.put(playerUUID, entities);
    }

    public static void TPleashed(UUID playerUUID, Location loc) {
        Vector<LivingEntity> entities = leashedEntities.get(playerUUID);
        if (entities == null) {
            return;
        }
        entities.removeIf(Predicate.not(LivingEntity::isLeashed));
        for (Entity entity : entities) {
            entity.teleport(loc);
        }
    }
}
