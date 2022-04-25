package me.ganyganyant.supplements.commands;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import me.ganyganyant.supplements.Supplements;
import me.ganyganyant.supplements.files.LocationAdapter;
import me.ganyganyant.supplements.files.Warps;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

import static me.ganyganyant.supplements.Supplements.sendFromConfig;
import static me.ganyganyant.supplements.commands.Back.TP;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        Supplements plugin = Supplements.getPlugin();
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("setspawn")) {
                // Set spawn command

                spawn = player.getLocation();
                save();
                sendFromConfig(player, "setSpawn");
            } else if (command.getName().equalsIgnoreCase("spawn")) {
                // Spawn command

                if (spawn == null){
                    // Spawn does not exist

                    sendFromConfig(player, "spawnNotExist");
                    return true;
                }
                    // teleport to spawn

                    if (plugin.getConfig().getInt("spawnTime") != 0) {
                        // sends a message that teleport will be delayed
                        player.sendMessage(
                                ChatColor.translateAlternateColorCodes('&', // changes &color to ChatColor.color
                                        Objects.requireNonNull(    // check if not null
                                                plugin.getConfig().getString("spawnWait")   // get message from config
                                        ).replace("{TIME}", String.valueOf(plugin.getConfig().getInt("spawnTime")))));  // changes {TIME} to delay in seconds
                        // delays the teleport
                        TP(player, spawn, plugin.getConfig().getInt("spawnTime") * 20L,"teleportedToSpawn");
                        return true;
                    }
                    TP(player,spawn);
                    sendFromConfig(player, "teleportedToSpawn");
            }
        }
        return true;
    }

    public static Location spawn;

    private static File spawnFile;

    public static void load() throws IOException {

        spawnFile = new File(Supplements.getPlugin().getDataFolder().getPath() + "/spawn.json");
        if (!spawnFile.exists()) {
            spawnFile.createNewFile();
        }
        String content = Files.readString(Path.of(Supplements.getPlugin().getDataFolder().getPath() + "/spawn.json"), StandardCharsets.US_ASCII);
        spawn = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create()
                .fromJson(content, Location.class);
    }

    public static void save(){
        try {
            Writer writer = new FileWriter(spawnFile, false);
            new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .create()
                    .toJson(spawn, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
