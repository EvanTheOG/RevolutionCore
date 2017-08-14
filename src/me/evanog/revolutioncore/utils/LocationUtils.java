package me.evanog.revolutioncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

	public static String locationToString(Location loc) {
		String s = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName();
		return s;
	}

	public static Location locationFromString(String s) {
		double x, y, z;
		World world;
		String[] parts = s.split(",");
		x = Double.valueOf(parts[0]);
		y = Double.valueOf(parts[1]);
		z = Double.valueOf(parts[2]);
		world = Bukkit.getWorld(parts[3]);
		return new Location(world, x, y, z);
	}
}
