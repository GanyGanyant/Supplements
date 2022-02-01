package me.ganyganyant.supplements.files;

import java.io.IOException;

import me.ganyganyant.supplements.Supplements;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LocationAdapter extends TypeAdapter<Location> {

    Supplements plugin = Supplements.getPlugin();

    @Override
    public void write(JsonWriter out, Location location) throws IOException {
        if (location == null || location.getWorld() == null) {
            out.nullValue();
            return;
        }
        out.beginArray();
        out.value(location.getWorld().getName());
        out.value(location.getX());
        out.value(location.getY());
        out.value(location.getZ());
        out.value(location.getYaw());
        out.value(location.getPitch());
        out.endArray();
    }

    @Override
    public Location read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginArray();
        World world = plugin.getServer().getWorld(in.nextString());
        double x = in.nextDouble();
        double y = in.nextDouble();
        double z = in.nextDouble();
        float yaw = (float)in.nextDouble();
        float pitch = (float)in.nextDouble();
        in.endArray();
        return new Location(world, x, y, z, yaw, pitch);
    }
}