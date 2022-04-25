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

public class HomeData {

    public static class home {

        private final String name;
        private Location location;

        public home(String Name, Location loc){
            name = Name;
            location = loc;
        }
        public boolean nameEquals(String homeName){
            return name.equalsIgnoreCase(homeName);
        }
        public String getName() { return name; }
        public Location getLocation() {
            return location;
        }
        public void changeLocation(Location loc){
            location = loc;
        }
    }
    public static class player {

        private String uuid;
        private ArrayList<home> homes;

        public player(String id){
            uuid = id;
            homes = new ArrayList<>();
        }

        public home findHome(String homeName){
            if (homes != null) {
                for (HomeData.home home : homes) {
                    if (home.nameEquals(homeName)) {
                        return home;
                    }
                }
            }
            return null;
        }

        public void addHome(String homeName, Location loc){
            if (findHome(homeName) == null) {
                homes.add(new home(homeName, loc));
            }
            else {
                findHome(homeName).changeLocation(loc);
            }
            save();
        }

        public void removeHome(String homeName){
            for (int i = 0; i < homes.size(); i++){
                if (homes.get(i).nameEquals(homeName)){
                    homes.remove(i);
                    save();
                    return;
                }
            }
        }
        public boolean uuidEquals(String id){
            return uuid.equalsIgnoreCase(id);
        }
        public Location homeByName(String homeName) {
            for (HomeData.home home : homes) {
                if (home.nameEquals(homeName)) {
                    return home.getLocation();
                }
            }
            return null;
        }
        public Location firstHome(){
            return homes.get(0).getLocation();
        }
        public void removeFirstHome(){
            homes.remove(0);
            save();
        }
        public int numberOfHomes(){
            if (homes == null){
                return 0;
            }
            return homes.size();
        }
        public String listHomes() {
            StringBuilder list = new StringBuilder();
            for (HomeData.home home : homes) {
                if (list.length() != 0) {
                    list.append(", ");
                }
                list.append(home.getName());
            }
            return list.toString();
        }
        public List<String> HomesList() {
            List<String> list = new ArrayList<>();
            for (HomeData.home home : homes) {
                list.add(home.getName());
            }
            return list;
        }
    }

    public static ArrayList<player> players;

    public static player getPlayer(String id) {
        if (players != null) {
            for (HomeData.player player : players) {
                if (player.uuidEquals(id)) {
                    return player;
                }
            }
        }
        else {
            players = new ArrayList<player>();
        }
        player p = new player(id);
        players.add(p);
        return p;
    }


    private static File homesFile;

    public static void load() throws IOException {

        homesFile = new File(Supplements.getPlugin().getDataFolder().getPath() + "/homes.json");
        if (!homesFile.exists()) {
            homesFile.createNewFile();
        }
        String content = Files.readString(Path.of(Supplements.getPlugin().getDataFolder().getPath() + "/homes.json"), StandardCharsets.US_ASCII);
        Type HomesType = new TypeToken<ArrayList<player>>(){}.getType();
        players = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create()
                .fromJson(content, HomesType);
    }

    public static void save(){
        try {
            Writer writer = new FileWriter(homesFile, false);
            new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .create()
                    .toJson(players, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
