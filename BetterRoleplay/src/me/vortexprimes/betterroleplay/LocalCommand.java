package me.vortexprimes.betterroleplay;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocalCommand implements CommandExecutor {
	
	String prefix = ChatColor.DARK_RED + "" + ChatColor.BOLD + "- ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length == 0) {
				Player player = (Player) sender;
				if(Main.getLocalPlayers().contains(player.getUniqueId())) {
					Main.removeFromLocal(player);
					player.sendMessage(prefix + ChatColor.YELLOW + "You are no longer talking in local chat!");
				} else {
					Main.addToLocal(player);
					player.sendMessage(prefix + ChatColor.YELLOW + "You are now talking in local chat!");
				}
			}
		}
		return false;
	}

}
