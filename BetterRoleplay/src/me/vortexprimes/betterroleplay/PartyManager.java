package me.vortexprimes.betterroleplay;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;

public class PartyManager {
	
	public static ArrayList<CreateParty> listOfParties = new ArrayList<CreateParty>();
	
	public static CreateParty getParty(Player player) {
		UUID uuid = player.getUniqueId();
		for(CreateParty parties : listOfParties) {
			if(parties.isInParty(uuid)) {
				return parties;
			}
		}
		return null;
	}
	
	public static CreateParty getParty(OfflinePlayer player) {
		UUID uuid = player.getUniqueId();
		for(CreateParty parties : listOfParties) {
			if(parties.isInParty(uuid)) {
				return parties;
			}
		}
		return null;
	}
	
	public static boolean isInParty(Player player) {
		UUID uuid = player.getUniqueId();
		for(CreateParty parties : listOfParties) {
			if(parties.isInParty(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static void createParty(Player player) {
		if(getParty(player) == null) {
			listOfParties.add(new CreateParty(player));
		} else {
			player.sendMessage("You are already in a party. Please disband or leave this party.");
		}
	}
	
	public static void disbandParty(Player player) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		if(party != null && party.getLeader() == uuid) {
			party.disband();
		}
	}
	
	public static void removePlayer(Player player, Player target) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		if(party != null && party.getLeader() == uuid) {
			player.sendMessage(ChatColor.RED + "Removed " + target.getDisplayName() + ChatColor.RED + " from the party.");
			party.remove(target);
			target.sendMessage(ChatColor.RED + "You were kicked from the party!");
		}
	}
	
	public static void joinPlayer(Player player) {
		for(CreateParty parties : listOfParties) {
			if(parties.isInParty(player.getUniqueId()) && parties.inviteList.contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "You must leave your current party!");
				return;
			}
			if(parties.inviteList.contains(player.getUniqueId())) {
				Player leader = Bukkit.getPlayer(parties.leader);
				leader.sendMessage(ChatColor.AQUA + "Player " + ChatColor.AQUA + player.getDisplayName() + ChatColor.DARK_AQUA + " has joined the party!");
				parties.users.add(player.getUniqueId());
				parties.inviteList.remove(player.getUniqueId());
				player.sendMessage(ChatColor.AQUA + "You joined the party!");
				return;
			}
			
			player.sendMessage(ChatColor.RED + "You have no invitations to a Roleplay party!");
			
		}
	}
	
	
	public static void invitePlayer(Player player, Player target) {
		if(player.getName() == target.getName()) {
			player.sendMessage(ChatColor.RED + "You cannot invite Yourself!");
			return;
		}
		CreateParty party = getParty(player);
		if(party.inviteList.contains(target.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "You have already invited this player!");
			return;
		}
		
		
		for(CreateParty parties : listOfParties) {
			if (parties.inviteList.contains(target.getUniqueId())) {
				parties.inviteList.remove(target.getUniqueId());
			}
			
			if(parties.users.contains(target.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "You cannot invite this user. They are already in a Roleplay party!");
				return;
			}
			
		}
		target.sendMessage(ChatColor.AQUA + "You have been invited to " + player.getDisplayName() + ChatColor.AQUA + "'s Roleplay Party! Use: " + ChatColor.DARK_AQUA + " /Roleplay accept" + ChatColor.AQUA + " to accept the invite.");
		player.sendMessage(ChatColor.AQUA + "Invited player " + ChatColor.DARK_AQUA + target.getDisplayName());
		party.inviteList.add(target.getUniqueId());
		
	}
	
	public static void leavePlayer(Player player) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		if(party != null && party.getLeader() != uuid) {
			if(party.users.contains(player.getUniqueId())) {
				party.users.remove(player.getUniqueId());
				player.sendMessage(ChatColor.RED + "You left the party!");
			}
		}
	}
	
	public static void promotePlayer(Player player, Player target) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		UUID puuid = target.getUniqueId();
		if(party != null && party.getLeader() == uuid) {
			if(uuid != puuid) {
				party.setLeader(puuid);
				target.sendMessage(ChatColor.AQUA + "You are now the party leader!");
				player.sendMessage(ChatColor.AQUA + "You are no longer the party leader!");
			} else {
				player.sendMessage(ChatColor.RED + "You cannot promote yourself. You are already the leader!");
			}
		}
	}
	
	
	
	
	
	
	//Roleplay Settings!
	
	
	public static void changePVP(Player player, boolean bool) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		party.tweakPVP(bool);
	}
	
	public static void changeRolling(Player player, boolean bool) {
		CreateParty party = getParty(player);
		UUID uuid = player.getUniqueId();
		party.tweakRolling(bool);
	}
	
	
	
	

}
