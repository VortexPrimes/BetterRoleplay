package me.vortexprimes.betterroleplay;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpyCommand implements CommandExecutor {
;
	String prefix = ChatColor.DARK_RED + "" + ChatColor.BOLD + "- ";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(args.length == 0) {
				Player player = (Player) sender;
				if(Main.getSpyPlayers().contains(player.getUniqueId())) {
					Main.removeFromSpy(player);
					player.sendMessage(prefix + ChatColor.YELLOW + "You are no longer a spy!");
				} else {
					Main.addToSpy(player);
					player.sendMessage(prefix + ChatColor.YELLOW + "You are now a spy!");
				}
			}
		}
		return false;
	}

}

