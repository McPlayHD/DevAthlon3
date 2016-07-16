package me.mcplayhd.spellwar.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.mcplayhd.spellwar.SpellWar;
import me.mcplayhd.spellwar.enums.EnumAbilitys.Abilitys;

public class PlayerListener implements Listener {

	private SpellWar plugin;

	public PlayerListener(SpellWar plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.am.mana.put(p, 64.0);
		p.getInventory().clear();
		plugin.im.sendInventory(p);
		e.setJoinMessage(plugin.prefix + "§a" + p.getDisplayName() + " §3kämpft jetzt mit");
		new BukkitRunnable() {

			@Override
			public void run() {
				p.teleport(plugin.spawnloc);
			}
			
		}.runTaskLater(plugin, 5);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		plugin.am.mana.remove(p);
		e.setQuitMessage(plugin.prefix + "§a" + p.getDisplayName() + " §3hat die Flucht ergriffen");
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getInventory().getItemInMainHand() != null) {
				if(plugin.am.getAbility.containsKey(p.getInventory().getItemInMainHand().getType())) {
					plugin.am.shootSpell(p, plugin.am.getAbility.get(p.getInventory().getItemInMainHand().getType()));
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		String message = e.getDeathMessage();
		message = message.replace(p.getName() + " ", "");
		switch(message) {
		case "fell from a high place":
			plugin.am.killPlayer(p, (plugin.am.levitation.containsKey(p) ? plugin.am.levitation.get(p) : null), Abilitys.LEVITATION);
			break;
		case "withered away":
			plugin.am.killPlayer(p, (plugin.am.wither.containsKey(p) ? plugin.am.wither.get(p) : null), Abilitys.WITHER);
			break;
		default:
			if(!message.equals("died")) {
				plugin.am.killPlayer(p, null, null);
			}
			break;
		}
		e.setDeathMessage(null);
		e.getDrops().clear();
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		e.setRespawnLocation(plugin.spawnloc);
		plugin.im.sendInventory(p);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.EMERALD) {
				e.setCancelled(true);
				p.updateInventory();
			}
		}
	}

}
