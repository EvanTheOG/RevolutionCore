package me.evanog.revolutioncore.crates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.evanog.revolutioncore.ConfigFile;
import me.evanog.revolutioncore.Core;
import me.evanog.revolutioncore.Manager;
import me.evanog.revolutioncore.crates.Crate.Reward;
import me.evanog.revolutioncore.crates.cmd.CrateCommand;
import me.evanog.revolutioncore.utils.ChatUtils;
import me.evanog.revolutioncore.utils.ItemBuilder;
import me.evanog.revolutioncore.utils.LocationUtils;

public class CrateManager extends Manager {

	private ConfigFile crateFile;
	private final Set<Crate> crates = new HashSet<>();

	@Override
	public void enable() {
		ConfigFile file = new ConfigFile("crates");
		file.register();
		this.crateFile = file;
		this.initDefaultCrateFile();
		this.loadCrates();
		Core.getInstance().getCommand("crate").setExecutor(new CrateCommand());
		Core.getInstance().getServer().getPluginManager().registerEvents(new CrateListeners(), Core.getInstance());
	}
	
	private void saveLocations() {
		FileConfiguration config = crateFile.getConfig();
		for (Crate crate : crates) {
			List<String> current = null;
			for (Location loc : crate.getCrateLocations()) {
				String locS = LocationUtils.locationToString(loc);
				current = config.getStringList("Crates." + crate.getName() + ".Locations");
				current.add(locS);
			}
			config.set("Crates." + crate.getName() + ".Locations", current);
			crateFile.save();
		}
	}

	private void loadCrates() {
		FileConfiguration config = crateFile.getConfig();
		for (String s : config.getConfigurationSection("Crates").getKeys(false)) {
			Crate crate = null;
			String name = s;
			List<Reward> rewards = new ArrayList<>();
			for (String rewardS : config.getConfigurationSection("Crates." + s + ".Rewards").getKeys(false)) {
				ItemStack item = new ItemStack(
						Material.matchMaterial(config.getString("Crates." + s + ".Rewards" + rewardS + ".Item")),
						config.getInt("Crates." + s + ".Rewards" + rewardS + ".Amount"));
				Reward reward = new Reward(item,config.getDouble("Crates." + s + ".Rewards" + rewardS + ".Chance"));
				rewards.add(reward);

			}
			ItemStack key = new ItemBuilder(Material.TRIPWIRE_HOOK, 1)
					.setName(ChatUtils.format(config.getString("Crates." + s + ".Key.Name")))
					.setLore(ChatUtils.formatList(config.getStringList("Crates." + s + ".Key.Lore"))).toItemStack();
			List<Location> locations = new ArrayList<Location>();
			
			for (String loc : config.getStringList("Crates." + s + ".Locations")) {
				locations.add(LocationUtils.locationFromString(loc));
			}
			
			int amount = config.getInt("Crates." + s + ".Items_Per_Open");
			crate = new Crate(name, rewards, key, locations, amount);
			crates.add(crate);
		}
	}

	@Override
	public void disable() {
		this.saveLocations();
	}

	private void initDefaultCrateFile() {
		FileConfiguration config = crateFile.getConfig();
		config.addDefault("Crates.Default.Key.Name", "&eDefault Crate Key");
		config.addDefault("Crates.Default.Key.Lore", new String[] { "&7Click on a default crate at spawn to open." });
		config.addDefault("Crates.Default.Key.Glow", Boolean.valueOf(true));
		config.addDefault("Crates.Default.Key.Glow", Boolean.valueOf(true));
		config.addDefault("Crates.Default.Items_Per_Open", Integer.valueOf(2));
		config.addDefault("Crates.Default.Locations", new String[]{});

		// reward 1
		config.addDefault("Crates.Default.Rewards.1.Chance", Double.valueOf(20.0));
		config.addDefault("Crates.Default.Rewards.1.Item", "ENDER_PEARL");
		config.addDefault("Crates.Default.Rewards.1.Amount", 16);

		// reward 2
		config.addDefault("Crates.Default.Rewards.2.Chance", Double.valueOf(50.0));
		config.addDefault("Crates.Default.Rewards.2.Item", "GOLDEN_APPLE");
		config.addDefault("Crates.Default.Rewards.2.Amount", 1);

		// reward 3
		config.addDefault("Crates.Default.Rewards.3.Chance", Double.valueOf(70.0));
		config.addDefault("Crates.Default.Rewards.3.Item", "SUGAR_CANE");
		config.addDefault("Crates.Default.Rewards.3.Amount", 16);
		config.options().copyDefaults(true);
		crateFile.save();
	}

	public Crate getCrateByName(String name) {
		for (Crate crate : crates) {
			if (crate.getName().equalsIgnoreCase(name)) {
				return crate;
			}
		}
		return null;
	}

	public void giveAll(Crate crate, int key) {

		for (Player p : Bukkit.getOnlinePlayers()) {
			for (int i = 0; i < key; i++) {
				p.getInventory().addItem(crate.getKey());
			}
			p.sendMessage(ChatColor.YELLOW + "You were given a " + crate.getName() + " key in a GiveAll!");
		}
	}

	public void giveKey(Player p, Crate crate, int key) {
		for (int i = 0; i < key; i++) {
			p.getInventory().addItem(crate.getKey());
		}
	}

	public void giveCrate(Player p, Crate crate) {
		p.getInventory()
				.addItem(new ItemBuilder(Material.CHEST, 1)
						.setName(ChatUtils.format("&e&n" + crate.getName() + "&r&e Crate"))
						.setLore(ChatUtils.format("&7Place me somewhere to register me!"), " ", "Official Crate")
						.toItemStack());
	}

	public boolean isCrate(ItemStack item) {
		if (!item.hasItemMeta()) {
			return false;
		}
		if (item.getItemMeta().getLore().contains("Official Crate")) {
			return true;
		}
		return false;
	}

	protected void attemptRemove(Location loc) {
		for (Crate crate : crates) {
			for (Location l : crate.getCrateLocations()) {
				if (l.equals(loc)) {
					crate.removeLocation(loc);
				}
			}

		}
	}

	private List<Reward> getRandomRewards(Crate crate, int rewardsAmount) {
		List<Reward> toReturn = new ArrayList<>();
		for (int i = 0; i < rewardsAmount; i++) {
			toReturn.add(this.getRandomReward(crate));
		}
		return toReturn;
	}

	private Reward getRandomReward(Crate crate) {
		final List<Reward> allRewards = crate.getRewards();
		double totalAm = 0;

		for (Reward r : allRewards) {
			totalAm += r.getChance();
		}
		int randomIndex = -1;
		double random = Math.random() * totalAm;
		for (int i = 0; i < allRewards.size(); i++) {
			random -= allRewards.get(i).getChance();
			if (random <= 0.0d) {
				randomIndex = i;
				break;
			}
		}
		Reward myRandomItem = allRewards.get(randomIndex);
		return myRandomItem;
	}

	public void displayRewardsInventory(Player p, Crate crate, int items) {
		Inventory inv = Bukkit.createInventory(null, 27,
				ChatUtils.format("&eRewards from &&a&n" + crate.getName() + "&r&e crate opening!"));
		for (Reward reward : this.getRandomRewards(crate, crate.getRewardsAmount())) {
			inv.addItem(reward.getItemStack());
		}
		p.openInventory(inv);
	}

}
