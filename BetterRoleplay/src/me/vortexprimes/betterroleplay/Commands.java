package me.vortexprimes.betterroleplay;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class Commands implements CommandExecutor, TabCompleter{
	
	
	String prefix = ChatColor.DARK_AQUA + "- ";

	
	// /roleplay or /rp
	// /roleplay party (create/invite/enable/tpall(CONSENT ONLY)) (turns/rolls/local)
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
			if(args.length == 0) {
				player.sendMessage(ChatColor.BLUE + "--------------------------------");
				player.sendMessage(ChatColor.YELLOW + " ROLEPLAY COMMANDS / FEATURES");
				player.sendMessage(ChatColor.BLUE + "--------------------------------");
				player.sendMessage(prefix + "/Roleplay create");
				player.sendMessage(prefix + "/Roleplay disband");
				player.sendMessage(prefix + "/Roleplay settings (rolling/pvp) (on/off)");
				player.sendMessage(prefix + "/Roleplay invite (IGN)");
				player.sendMessage(prefix + "/Roleplay kick (IGN)");
				player.sendMessage(prefix + "/Roleplay promote (IGN)");
				player.sendMessage(ChatColor.BLUE + "--------------------------------");
				player.sendMessage(prefix + "/Roleplay roll");
				player.sendMessage(prefix + "/Roleplay teleport");
				player.sendMessage(ChatColor.BLUE + "--------------------------------");
				
			} else {
				if(args.length > 0 ) {
					if(args[0].equalsIgnoreCase("create")) {
						if(!PartyManager.isInParty(player)) {
							PartyManager.createParty(player);
							player.sendMessage(ChatColor.AQUA + "Created a new Roleplay Party. Use /Roleplay invite (IGN). To invite a player to your group!");
							
						} else {
							player.sendMessage(ChatColor.RED + "You are already in a party.");
						}
					} else if(args[0].equalsIgnoreCase("disband")) {
						if(PartyManager.isInParty(player) && PartyManager.getParty(player).getLeader() == uuid) {
							PartyManager.disbandParty(player);
							player.sendMessage(ChatColor.RED + "Disbanded party!");
						} else {
							player.sendMessage(ChatColor.RED + "You are either not the party leader. Or are not in a party.");
						}
					} else if(args[0].equalsIgnoreCase("invite")) {
						if(PartyManager.isInParty(player) && PartyManager.getParty(player).getLeader() == uuid) {
							if(Bukkit.getPlayer(args[1]) instanceof Player) {
								PartyManager.invitePlayer(player, Bukkit.getPlayer(args[1]));
							} else {
								player.sendMessage(ChatColor.RED + "Player is not online/does not exist.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are either not the party leader. Or are not in a party!");
						}
						
						
					} else if(args[0].equalsIgnoreCase("teleport")) {
						if(PartyManager.isInParty(player) && PartyManager.getParty(player).getLeader() == uuid) {
							player.sendMessage(ChatColor.AQUA + "Teleported Party.");
							CreateParty party = PartyManager.getParty(player);
							party.teleportPlayers(player);
						} else {
							player.sendMessage(ChatColor.RED + "You are either not the party leader. Or are not in a party!");
						}
						
					} else if(args[0].equalsIgnoreCase("accept")) {
						PartyManager.joinPlayer(player);
					} else if(args[0].equalsIgnoreCase("leave")) {
						if(PartyManager.isInParty(player)) {
							if(PartyManager.getParty(player).getLeader() == uuid) {
								player.sendMessage(ChatColor.RED + "You cannot leave this party. Either promote a new leader or disband.");
							} else {
								player.sendMessage(ChatColor.AQUA + "You have left the party!");
								CreateParty party = PartyManager.getParty(player);
								for(UUID puuid : party.getPlayers()) {
									Player user = Bukkit.getPlayer(puuid);
									user.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + " has left the party!");
								}
								PartyManager.leavePlayer(player);
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are not in a party!");
						}
					} else if(args[0].equalsIgnoreCase("promote")) {
						if(PartyManager.isInParty(player)) {
							if(PartyManager.getParty(player).getLeader() == uuid) {
								if(Bukkit.getPlayer(args[1]) instanceof Player) {
									PartyManager.promotePlayer(player, Bukkit.getPlayer(args[1]));
								}
							} else {
								player.sendMessage(ChatColor.RED + "You are not the party leader!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are not in a party!");
						}
					} else if(args[0].equalsIgnoreCase("settings")) {
						if(PartyManager.isInParty(player)) {
							if(PartyManager.getParty(player).getLeader() == uuid) {
								if(args.length == 3) {
									boolean bool = false;
									if(args[2].toString().equalsIgnoreCase("on") || args[2].toString().equalsIgnoreCase("true")) {
										bool = true;
									} else if(args[2].toString().equalsIgnoreCase("off") || args[2].toString().equalsIgnoreCase("false")) {
										bool = false;
									} else {
										player.sendMessage(ChatColor.RED + "Wrong usage: /Roleplay settings (rolling/pvp) (on/off)");
									}
									
									if(args[1].equalsIgnoreCase("roll") || args[1].equalsIgnoreCase("rolling")) {
										PartyManager.changeRolling(player, bool);
										player.sendMessage(ChatColor.AQUA + "Rolling is now " + ChatColor.YELLOW + bool);
									} else if(args[1].equalsIgnoreCase("pvp") || args[1].equalsIgnoreCase("fighting")) {
										PartyManager.changePVP(player, bool);
										player.sendMessage(ChatColor.AQUA + "PVP is now " + ChatColor.YELLOW + bool);
									} else {
										player.sendMessage(ChatColor.RED + "Wrong usage: /Roleplay settings (rolling/pvp) (on/off)");
									}
								} else {
									player.sendMessage(ChatColor.RED + "Wrong usage: /Roleplay settings (rolling/pvp) (on/off)");
								}
							} else {
								player.sendMessage(ChatColor.RED + "You are not the party leader!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are not in a party!");
						}
					} else if(args[0].equalsIgnoreCase("roll") || args[0].equalsIgnoreCase("rolling")) {
						if(PartyManager.isInParty(player)) {
							CreateParty party = PartyManager.getParty(player);
							if(party.getRolling() == true) {
								Random random = new Random();
								Integer number = random.nextInt(11);
								
								for(UUID puuid : party.getPlayers()) {
									Player user = Bukkit.getPlayer(puuid);
									user.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + " has rolled a " + ChatColor.YELLOW + "" + ChatColor.BOLD + number);
								}
							} else {
								player.sendMessage(ChatColor.RED + "Rolling is not enabled in this party!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are not in a party!");
						}
					} else if(args[0].equalsIgnoreCase("kick")) {
						if(PartyManager.isInParty(player) && PartyManager.getParty(player).getLeader() == uuid) {
							if(Bukkit.getPlayer(args[1]) instanceof Player) {
								Player user = Bukkit.getPlayer(args[1]);
								CreateParty party = PartyManager.getParty(player);
								if(PartyManager.isInParty(user)) {
									if(PartyManager.getParty(user) == party) {
										PartyManager.removePlayer(player, Bukkit.getPlayer(args[1]));
									} else {
										player.sendMessage(ChatColor.RED + "Player is not in your party!");
									}
								} else {
									player.sendMessage(ChatColor.RED + "Player is not in your party!");
								}
							} else {
								player.sendMessage(ChatColor.RED + "Player is not online/does not exist!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You are either not the party leader. Or are not in a party!");
						}
					}

				}
			}
		}
		return false;
	}
	
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> arguments1 = Arrays.asList("create", "disband", "settings",  "invite", "kick", "promote");
		List<String> arguments2 = Arrays.asList("rolling", "pvp");
		List<String> arguments3 = Arrays.asList("on", "off");
		List<String> Flist = Lists.newArrayList();
		
		if(args.length == 1) {
			for(String s : arguments1) {
				if(s.toLowerCase().startsWith(args[0].toLowerCase()))Flist.add(s);
			}
		}
		
		return Flist;
	}

	

}
