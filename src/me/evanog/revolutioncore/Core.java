package me.evanog.revolutioncore;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class Core extends JavaPlugin {

	/*
	 * RevolutionCore v1.0
	 * Being Developed and maintained by EvanOG.
	 * 
	 *
	 * CheckList:
	 * - Crates
	 * - 
	 * -
	 * -
	 * -
	 */
	
	public static final String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&eRevolution&7]");
	
	
	
	private static Core instance;
	
	public static Core getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
	}
	
	@Override
	public void onDisable() {
		instance = null;
	}
	
}
