package me.evanog.revolutioncore;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.evanog.revolutioncore.crates.CrateManager;


public class Core extends JavaPlugin {

	/*
	 * RevolutionCore v1.0
	 * Being Developed and maintained by EvanOG.
	 */
	
	public static final String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&eRevolution&7]");
	private CrateManager crateManager;
	private Set<Manager> managers = new HashSet<>();
	
	
	 
	private static Core instance;
	
	public static Core getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		this.initManagers();
	}
	
	@Override
	public void onDisable() {
		instance = null;
		this.closeManagers();
	}
	
	private void initManagers() {
		this.crateManager = new CrateManager();
		managers.add(crateManager);
		
		for (Manager man : managers) {
			man.enable();
		}
	}
	
	private void closeManagers() {
		for (Manager man : managers) {
			man.disable();
			managers.clear();
		}
	}
	
	public CrateManager getCrateManager() {
		return crateManager;
	}
	
}
