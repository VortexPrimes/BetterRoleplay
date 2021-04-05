package me.vortexprimes.betterroleplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreateParty {
	
	UUID uuid;
	UUID leader;
	ArrayList<UUID> users;
	ArrayList<UUID> inviteList;
	boolean rolling;
	boolean pvp;
	
	
	
	public CreateParty(Player player) {
		UUID uuid = player.getUniqueId();
		this.uuid = uuid;
		this.leader = uuid;
		this.pvp = true;
		this.rolling = true;
		ArrayList<UUID> users = new ArrayList<UUID>();
		ArrayList<UUID> inviteList = new ArrayList<UUID>();
		this.users = users;
		this.inviteList = inviteList;
		
		users.add(uuid);
		
	}

	public boolean isLeader(UUID uuid) {
		return (this.leader == uuid);
	}
	
	public boolean isInParty(UUID uuid) {
		if(this.users.contains(uuid)) {
			return true;
		}
		return false;
	}
	
	public UUID getLeader() {
		return this.leader;
	}
	public List<UUID> getPlayers(){
		return this.users;
	}
	
	public Boolean getPVP() {
		return this.pvp;
	}
	
	public Boolean getRolling() {
		return this.rolling;
	}
	
	public void disband() {
		this.users.clear();
		this.leader = null;
		this.uuid = null;
	}
	
	public void teleportPlayers(Player player) {
		for(UUID puuid : this.users) {
			Player target = Bukkit.getPlayer(puuid);
			target.teleport(player.getLocation());
		}
	}
	
	public void setLeader(UUID uuid) {
		this.leader = uuid;
	}
	
	public void remove(Player player) {
		this.users.remove(player.getUniqueId());
	}
	
	public void tweakPVP(Boolean bool) {
		if(bool == true) {
			this.pvp = true;
		} else {
			this.pvp = false;
		}
	}
	
	public void tweakRolling(Boolean bool) {
		if(bool == true) {
			this.rolling = true;
		} else {
			this.rolling = false;
		}
	}

}
