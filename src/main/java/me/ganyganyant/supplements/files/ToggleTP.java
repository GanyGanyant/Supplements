package me.ganyganyant.supplements.files;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import me.ganyganyant.supplements.Supplements;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

public class ToggleTP {

    public static class player {

        private final String uuid;
        private boolean recieve = true;
        private ArrayList<String> blockedPlayers = new ArrayList<>();

        public player(String id) {
            uuid = id;
        }

        public boolean uuidEquals(Player p){
            return uuidEquals(p.getUniqueId());
        }

        public boolean uuidEquals(UUID id){
            return uuidEquals(id.toString());
        }

        public boolean uuidEquals(String id){
            return uuid.equals(id);
        }

        public boolean canTP(Player p) {
            return canTP(p.getUniqueId());
        }

        public boolean canTP(UUID id) {
            return canTP(id.toString());
        }

        public boolean canTP(String uuid) {
            if (!recieve) {
                return false;
            }
            for (String player:blockedPlayers) {
                if (player.equals(uuid)){
                    return false;
                }
            }
            return true;
        }

        public boolean notBlocked(Player p) {
            return notBlocked(p.getUniqueId());
        }

        public boolean notBlocked(UUID id) {
            return notBlocked(id.toString());
        }

        public boolean notBlocked(String uuid) {
            for (String player:blockedPlayers) {
                if (player.equals(uuid)){
                    return false;
                }
            }
            return true;
        }

        public boolean canTP() {
            return recieve;
        }

        public void toggle(Player p) {
            toggle(p.getUniqueId());
        }

        public void toggle(UUID id) {
            toggle(id.toString());
        }


        public void toggle(String uuid){
            if (blockedPlayers.contains(uuid)){
                blockedPlayers.remove(uuid);
                save();
                return;
            }
            blockedPlayers.add(uuid);
            save();
        }

        public void toggle(){
            recieve = !recieve;
            save();
        }
    }

    public static ArrayList<player> playerToggles = new ArrayList<>();

    public static player getPlayer(Player p) {
        return getPlayer(p.getUniqueId());
    }

    public static player getPlayer(UUID id) {
        return getPlayer(id.toString());
    }

    public static player getPlayer(String id) {
        if (playerToggles != null) {
            for (player player : playerToggles) {
                if (player.uuidEquals(id)) {
                    return player;
                }
            }
        }
        else {
            playerToggles = new ArrayList<player>();
        }
        player p = new player(id);
        playerToggles.add(p);
        return p;
    }

    private static final File toggleFile = new File(Supplements.getPlugin().getDataFolder().getPath() + "/tpToggles.json");

    public static void load() throws IOException {

        if (!toggleFile.exists()) {
            toggleFile.createNewFile();
        }
        String content = Files.readString(Path.of(toggleFile.getPath()));
        Type PlayerToggles = new TypeToken<ArrayList<player>>(){}.getType();
        playerToggles = new Gson()
                .fromJson(content, PlayerToggles);
    }

    public static void save(){
        try {
            Writer writer = new FileWriter(toggleFile, false);
            new Gson().toJson(playerToggles, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
