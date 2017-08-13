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
import org.bukkit.inventory.ItemStack;

import me.evanog.revolutioncore.ConfigFile;
import me.evanog.revolutioncore.Core;
import me.evanog.revolutioncore.Manager;
import me.evanog.revolutioncore.crates.Crate.Reward;
import me.evanog.revolutioncore.crates.cmd.CrateCommand;
import me.evanog.revolutioncore.utils.ChatUtils;
import me.evanog.revolutioncore.utils.ItemBuilder;

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

	private void loadCrates() {
		FileConfiguration config = crateFile.getConfig();
		for (String s : config.getConfigurationSection("Crates").getKeys(false)) {
			Crate crate = null;
			String name = s;
			List<Reward> rewards = new ArrayList<>();
			ItemStack key = new ItemBuilder(Material.TRIPWIRE_HOOK, 1)
					.setName(ChatUtils.format(config.getString("Crates." + s + ".Key.Name")))
					.setLore(ChatUtils.formatList(config.getStringList("Crates." + s + ".Key.Lore"))).toItemStack();
			List<Location> locations = new ArrayList<Location>();

			crate = new Crate(name, rewards, key, locations);
			crates.add(crate);
		}
	}

	@Override
	public void disable() {

	}

	private void initDefaultCrateFile() {
		FileConfiguration config = crateFile.getConfig();
		config.addDefault("Crates.Default.Key.Name", "&eDefault Crate Key");
		config.addDefault("Crates.Default.Key.Lore", new String[] { "&7Click on a default crate at spawn to open." });
		config.addDefault("Crates.Default.Key.Glow", Boolean.valueOf(true));
		config.addDefault("Crates.Default.Key.Glow", Boolean.valueOf(true));
		config.addDefault("Crates.Default.Items_Per_Open", Integer.valueOf(2));

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
						.setLore(ChatUtils.format("&7Place me somewhere to register me!"), " ", "Official Crate").toItemStack());
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
	
	
}
