package me.evanog.revolutioncore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigFile {

	private String name;
	private File file;
	private FileConfiguration config;
	
	private final String ext = ".yml";
	
	private static Map<String, ConfigFile> configs = new HashMap<>();
	
	
	public ConfigFile(String name) {
		this.name = name;
		 if (!Core.getInstance().getDataFolder().exists()) {
			 Core.getInstance().getDataFolder().mkdirs();
	     }
	}
	
	public void register() {
		if (configs.containsKey(name)) {
			Bukkit.getServer().getConsoleSender().sendMessage(Core.prefix + ChatColor.RED + " File with name " + this.name + " already exists!");
		}
		this.file = new File(Core.getInstance().getDataFolder(), this.name + ext);
		this.config = YamlConfiguration.loadConfiguration(file);
		this.save();
		configs.put(this.name, this);
		
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public void save() {
		try {
			config.save(file);
		} catch(IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(Core.prefix + ChatColor.RED + " Failed to save file named " + this.name + "!");
		}
	}
	
	
}