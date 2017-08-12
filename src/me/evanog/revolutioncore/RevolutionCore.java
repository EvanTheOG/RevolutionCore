package me.evanog.revolutioncore;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class RevolutionCore extends JavaPlugin {

	/*
	 * RevolutionCore v1.0
	 * Being Developed and maintained by EvanOG.
	 * 
	 *
	 * CheckList:
	 * - Deathbans
	 * - Classes
	 * - EnchantLimiter
	 * - MapKit
	 * - Crates
	 * - ....
	 * -
	 * -
	 * -
	 */
	
	public static final String prefix = ChatColor.translateAlternateColorCodes('&', "&7[&eRevolution&7]");
	
	
	
	private static RevolutionCore instance;
	
	public static RevolutionCore getInstance() {
		return instance;
	}
	
}
