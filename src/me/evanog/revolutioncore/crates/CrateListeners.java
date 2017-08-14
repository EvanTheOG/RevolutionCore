package me.evanog.revolutioncore.crates;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.evanog.revolutioncore.Core;

public class CrateListeners implements Listener {

	private CrateManager crateManager = Core.getInstance().getCrateManager();

	private String toLocationString(Location loc) {
		String s = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getWorld().getName();
		return s;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack inHand = p.getItemInHand();
		if (inHand.getType() != Material.CHEST) {
			return;
		}
		if (!crateManager.isCrate(inHand)) {
			return;
		}
		String name = ChatColor.stripColor(inHand.getItemMeta().getDisplayName().split(" ")[0]);
		Crate crate = crateManager.getCrateByName(name);
		crate.addLocation(e.getBlockPlaced().getLocation());
		p.sendMessage("&Registered a " + crate.getName() + " crate at "
				+ this.toLocationString(e.getBlockPlaced().getLocation()));
	} 
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Location loc = e.getBlock().getLocation();
		crateManager.attemptRemove(loc);
	}

}
