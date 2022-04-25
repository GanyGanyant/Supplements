package me.ganyganyant.supplements.files;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import me.ganyganyant.supplements.Supplements;
import org.bukkit.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warps {
    public static class warp {

        private String name;
        private Location location;
        private UUID owner;

        public warp(String name, Location location, UUID owner) {
            this.name = name;
            this.location = location;
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public Location getLocation() {
            return location;
        }

        public UUID getOwner() {
            return owner;
        }

        public void changeLocation(Location loc){
            location = loc;
        }

        public boolean nameEquals(String warpName){
            return name.equalsIgnoreCase(warpName);
        }
    }
    public static ArrayList<warp> warps;

    public static warp getWarp(String name) {
        if (warps != null) {
            for (warp w : warps) {
                if (w.nameEquals(name)) {
                    return w;
                }
            }
        }
        return null;
    }

    public static void addWarp(String warpName, Location loc, UUID owner){
        if (getWarp(warpName) == null) {
            warps.add(new warp(warpName, loc , owner));
        }
        else {
            getWarp(warpName).changeLocation(loc);
        }
        save();
    }

    public static void removeWarp(String homeName){
        for (int i = 0; i < warps.size(); i++){
            if (warps.get(i).nameEquals(homeName)){
                warps.remove(i);
                save();
                return;
            }
        }
    }

    public static String listWarps() {
        StringBuilder list = new StringBuilder();
        for (warp w : warps) {
            if (list.length() != 0) {
                list.append(", ");
            }
            list.append(w.getName());
        }
        return list.toString();
    }

    public static List<String> WarpsList() {
        List<String> list = new ArrayList<>();
        for (warp w : warps) {
            list.add(w.getName());
        }
        return list;
    }

    public static List<String> WarpsList(UUID uuid) {
        List<String> list = new ArrayList<>();
        for (warp w : warps) {
            if (w.getOwner() == uuid) {
                list.add(w.getName());
            }
        }
        return list;
    }

    private static File warpsFile;

    public static void load() throws IOException {

        warpsFile = new File(Supplements.getPlugin().getDataFolder().getPath() + "/warps.json");
        if (!warpsFile.exists()) {
            warpsFile.createNewFile();
        }
        String content = Files.readString(Path.of(Supplements.getPlugin().getDataFolder().getPath() + "/warps.json"), StandardCharsets.US_ASCII);
        Type WarpsType = new TypeToken<ArrayList<warp>>(){}.getType();
        warps = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create()
                .fromJson(content, WarpsType);
        if (warps == null) {
            warps = new ArrayList<>();
        }
    }

    public static void save(){
        try {
            Writer writer = new FileWriter(warpsFile, false);
            new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .create()
                    .toJson(warps, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
