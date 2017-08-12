package me.evanog.revolutioncore.crates;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Crate {

	private String name;
	private List<Reward> rewards;
	private ItemStack key;
	private List<Location> crateLocations;

	public Crate(String name, List<Reward> rewards, ItemStack key, List<Location> crateLocations) {
		super();
		this.name = name;
		this.rewards = rewards;
		this.key = key;
		this.crateLocations = crateLocations;
	}

	public String getName() {
		return name;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public ItemStack getKey() {
		return key;
	}

	public List<Location> getCrateLocations() {
		return crateLocations;
	}

	public void addLocation(Location loc) {
		if (!this.crateLocations.contains(loc)) 
		this.crateLocations.add(loc);
	}
	public void remove(Location loc) {
		if (this.crateLocations.contains(loc)) 
		this.crateLocations.remove(loc);
	}
	
	
	
	
	
	public static class Reward {

		private ItemStack item;
		private double chance;

		public Reward(ItemStack item, double chance) {
			this.item = item;
			this.chance = chance;
		}

		public static List<Reward> chooseRandomRewards(int amount, Crate crate) {
			return null;
		}

	}

}
