package me.mcplayhd.spellwar.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
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
		p.setHealth(20.0D);
		p.setFoodLevel(20);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.updateInventory();
		p.setLevel(0);
		p.setExp(0);
		p.setGameMode(GameMode.SURVIVAL);
		p.setAllowFlight(false);
		p.setFlying(false);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		plugin.am.mana.put(p, 64.0);
		plugin.im.sendInventory(p);
		e.setJoinMessage(plugin.prefix + "§a" + p.getDisplayName() + " §3kämpft jetzt mit");
		new BukkitRunnable() {

			@Override
			public void run() {
				p.teleport(plugin.spawnloc);
			}

		}.runTaskLater(plugin, 5);
		new BukkitRunnable() {

			@Override
			public void run() {
				p.setResourcePack("http://mcplayhd.net/dwl/rp/spellwar/SpellWar-ResourcePack.zip");
				p.sendMessage(plugin.prefix + "§eUm die Schussbahnen sehen zu können, solltest du die Partikel mindestens auf 'Minimal' haben.");
			}

		}.runTaskLater(plugin, 20);
		if(!plugin.save.contains(p)) {
			plugin.save.add(p);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		plugin.am.mana.remove(p);
		if(plugin.save.contains(p)) {
			plugin.save.remove(p);
		}
		e.setQuitMessage(plugin.prefix + "§a" + p.getDisplayName() + " §3hat die Flucht ergriffen");
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getInventory().getItemInMainHand() != null) {
				if(plugin.am.getAbility.containsKey(p.getInventory().getItemInMainHand().getType())) {
					if(!plugin.save.contains(p)) {
						plugin.am.shootSpell(p, plugin.am.getAbility.get(p.getInventory().getItemInMainHand().getType()));
					} else {
						p.sendMessage(plugin.prefix + "§eDu hast die Schutzzone noch nicht verlassen");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(plugin.save.contains(p)) {
				e.setCancelled(true);
			}
			if(e.getDamager() instanceof Player) {
				Player sender = (Player) e.getDamager();
				if(plugin.save.contains(sender)) {
					e.setCancelled(true);
					p.sendMessage(plugin.prefix + "§eDu hast die Schutzzone noch nicht verlassen");
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
		e.setDroppedExp(0);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		e.setRespawnLocation(plugin.spawnloc);
		if(!plugin.save.contains(p)) {
			plugin.save.add(p);
		}
		plugin.im.sendInventory(p);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(plugin.save.contains(p)) {
			double x = e.getTo().getX();
			double z = e.getTo().getZ();
			if(x > 2 || x < -1 || z > 2 || z < -1) {
				plugin.save.remove(p);
				p.sendMessage(plugin.prefix + "§eDu hast die Schutzzone verlassen");
			}
		}
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
