package me.evanog.revolutioncore.crates.cmd;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.evanog.revolutioncore.Core;
import me.evanog.revolutioncore.crates.Crate;
import me.evanog.revolutioncore.utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;

public class CrateCommand implements CommandExecutor {
	
	
	private List<String> help = Arrays.asList( 
			 "&e&lCrate Commands:"
			,"&e/crate : shows commands"
			,"&e/crate give <crateName> <player> : gives player a crate key."
			,"&e/crate giveall <crateName> : gives all players on the server a crate key.");
			

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player == false) {
			return false;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("crate")) {
			if (args.length == 0) {
				for (String s : ChatUtils.formatList(help)) {
					p.sendMessage(s);
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("giveall")) {
					if (p.hasPermission("Revolution.crates.giveall")) {
						Crate crate = Core.getInstance().getCrateManager().getCrateByName(args[1]);
						if (crate == null) {
							p.sendMessage(ChatColor.RED + "Crate not found.");
							return false;
						}
						Core.getInstance().getCrateManager().giveAll(crate);
					} else {
						p.sendMessage("&cYou do not have Permission!");
					}
				}
				//crate givekey evanog 
				
			}else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("give")) {
					Player player = Bukkit.getPlayer(args[1]);
					if (player.isOnline() == false) {
						p.sendMessage(ChatColor.RED + "Player not found.");
						return false;
					}
					Crate crate = Core.getInstance().getCrateManager().getCrateByName(args[2]);
					if (crate == null) {
						p.sendMessage(ChatColor.RED + "Crate not found.");
						return false;
					}
					try {
						Integer.valueOf(args[3]);
						Core.getInstance().getCrateManager().giveKey(player, crate, Integer.valueOf(args[3]));
						player.sendMessage(ChatColor.YELLOW + "You have been given " + args[3] + " " + crate.getName() + " keys!");
						p.sendMessage(ChatColor.YELLOW + "Gave keys to " + player.getName() + ".");
					}catch(NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "3rd argument must be an integer!");
					}
					
				}
			}
		}
		return false;
	}

	
	
}
