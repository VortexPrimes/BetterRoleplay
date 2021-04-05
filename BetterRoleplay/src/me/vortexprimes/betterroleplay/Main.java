package me.vortexprimes.betterroleplay;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	
	public static ArrayList<UUID> local = new ArrayList<UUID>();
	public static ArrayList<UUID> spy = new ArrayList<UUID>();
	
	@Override
	public void onEnable() {
		getCommand("roleplay").setExecutor(new Commands());
		getCommand("roleplay").setTabCompleter(new Commands());
		getCommand("local").setExecutor(new LocalCommand());
		getCommand("localspy").setExecutor(new SpyCommand());
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	
	@Override
	public void onDisable() {
		
	}
	
	public static void addToLocal(Player player) {
		UUID uuid = player.getUniqueId();
		local.add(uuid);
	}
	
	public static void removeFromLocal(Player player) {
		UUID uuid = player.getUniqueId();
		local.remove(uuid);
	}
	
	public static ArrayList getLocalPlayers() {
		return local;
		
	}
	
	public static void addToSpy(Player player) {
		UUID uuid = player.getUniqueId();
		spy.add(uuid);
	}
	
	public static void removeFromSpy(Player player) {
		UUID uuid = player.getUniqueId();
		spy.remove(uuid);
	}
	
	public static ArrayList getSpyPlayers() {
		return spy;
		
	}
	
	
	
	 @EventHandler
	 public void onChat(PlayerChatEvent e) {
		 Player player = e.getPlayer();
		 UUID uuid = player.getUniqueId();
		 String message = e.getMessage();
		 //checks if in party
		 //If not in party.
		 //Check if they started with a ? to start local
		 if(e.getMessage().startsWith("*")) {
			 message = e.getMessage().substring(1);
			 e.setCancelled(true);
			 for(Entity p : player.getNearbyEntities(75, 75, 75)) {
				 if(p instanceof Player) {
					 if(!(p.getName() == player.getName())) { 
						 p.sendMessage(ChatColor.GOLD + "[L] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
					 }
				 }
				 
			 }
			 player.sendMessage(ChatColor.GOLD + "[L] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
			 for(Player opped : Bukkit.getOnlinePlayers()) {
				 if(opped.isOp()) {
					 if(opped.getName() != player.getName()) {
						 if(getSpyPlayers().contains(opped.getUniqueId())) {
							 opped.sendMessage(ChatColor.RED + "[Spy (LOCAL)] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
						 }
					 }
				}
			 }
			return;
		 } else if(e.getMessage().startsWith("!")) {
			 message = e.getMessage().substring(1);
				 e.setCancelled(true);
				 for(Player p :Bukkit.getOnlinePlayers()) {
					 e.setCancelled(true);
					 p.sendMessage(ChatColor.GOLD + "[G] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
				 }
				 return;
		 } else if(!e.getMessage().startsWith("!") && !e.getMessage().startsWith("*") && PartyManager.isInParty(player) == false) {
			 if(!local.contains(uuid)) {
				 for(Player p :Bukkit.getOnlinePlayers()) {
					 e.setCancelled(true);
					 p.sendMessage(ChatColor.GOLD + "[G] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
				 }
				 return;
			 } else {
				 for(Entity p : player.getNearbyEntities(75, 75, 75)) {
					 if(p instanceof Player) {
						 if(p.getName() != player.getName()) { 
							 p.sendMessage(ChatColor.GOLD + "[L] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
						 }
					 }
				 }
				 for(Player opped : Bukkit.getOnlinePlayers()) {
					 if(opped.isOp()) {
						 if(opped.getName() != player.getName()) {
							 if(getSpyPlayers().contains(opped.getUniqueId())) {
								 if(getSpyPlayers().contains(opped.getUniqueId())) {
									 opped.sendMessage(ChatColor.RED + "[Spy (LOCAL)] " + ChatColor.GRAY + "<" + player.getDisplayName() + ChatColor.GRAY + "> " + ChatColor.WHITE + message);
								 }
							 }
						 }
					 }
				 }
			 }
		 } else if(PartyManager.isInParty(player) == true) {
			 String opmessage = "";
			 for(CreateParty parties : PartyManager.listOfParties) {
				 if(parties.users.contains(uuid)) {
					 e.setCancelled(true);
					 for(UUID puuid : parties.users) {
						 if(e.getMessage().startsWith("(") && e.getMessage().endsWith(")")) {
							 Bukkit.getPlayer(puuid).sendMessage(ChatColor.RED + "(OOC) " + ChatColor.WHITE + player.getDisplayName() + ": " + e.getMessage());
							 opmessage = ChatColor.RED + "[Spy (OOC)] " + ChatColor.WHITE + player.getDisplayName() + ": " + e.getMessage();
						 } else {
							 Bukkit.getPlayer(puuid).sendMessage(ChatColor.AQUA + "(IRP) " + ChatColor.WHITE + player.getDisplayName() + ": " + e.getMessage());
							 opmessage = ChatColor.RED + "[Spy (IRP)] " + ChatColor.WHITE + player.getDisplayName() + ": " + e.getMessage();
						 }
					 }
				 }
			 }
			 for(Player opped : Bukkit.getOnlinePlayers()) {
				 if(spy.contains(opped.getUniqueId())) {
					 if(opped.getName() != player.getName()) {
						 opped.sendMessage(opmessage);
					 }
				 }
			 }
		 }
		 return;
		 
	 }
	 
	 @EventHandler
	 public void onHit(EntityDamageByEntityEvent e) {
		 if(e.getEntity() instanceof Player) {
			 if(e.getDamager() instanceof Player) {
				 Player theHitPlayer = (Player) e.getEntity();
				 Player theAttackingPlayer = (Player) e.getDamager();
				 //Check if player is in a party
				 if(PartyManager.isInParty(theAttackingPlayer)) {
					 CreateParty party = PartyManager.getParty(theAttackingPlayer);
					 if(party.getPVP() == false) {  e.setCancelled(true); }
				 }
				 if(PartyManager.isInParty(theHitPlayer)) {
					 CreateParty party = PartyManager.getParty(theHitPlayer);
					 if(party.getPVP() == false) {  e.setCancelled(true); }
				 }

				 
			 }
		 }
		 
	 }
	
	 
	 
	 
	 //Leaving Listeners
	 @EventHandler
	 public void onLeave(PlayerQuitEvent e) {
		 Player player = e.getPlayer();
		 String name = player.getName();
		 if(PartyManager.isInParty(player)) {
			 CreateParty party = PartyManager.getParty(Bukkit.getOfflinePlayer(name));
			 if(party.getLeader() == Bukkit.getOfflinePlayer(name).getUniqueId()) {
				 if(party.getPlayers().size() > 0) {
					 for(UUID uuid : party.getPlayers()) {
						 Player user = Bukkit.getPlayer(uuid);
						 user.sendMessage(ChatColor.AQUA + "Party has been disbanded!");
					 }
				 }
				 party.disband();
			 } else {
				 party.getPlayers().remove(player.getUniqueId());
				 for(UUID uuid : party.getPlayers()) {
					 Player user = Bukkit.getPlayer(uuid);
					 user.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.AQUA + " has left the party!");
				 }
			 }
		 }
	 }
	 
}
