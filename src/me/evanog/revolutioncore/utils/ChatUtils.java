package me.evanog.revolutioncore.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class ChatUtils {

	public static String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static Iterable<String> formatList(Iterable<String> list) {
		List<String> toReturn = new ArrayList<>();
		for (String s : list) {
			toReturn.add(format(s));
		}
		return toReturn;
	}
	
}
