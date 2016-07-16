package me.mcplayhd.spellwar.Listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.mcplayhd.spellwar.SpellWar;

public class PlayerListener implements Listener {

	final int MANAPERSECOUND = 20;
	private SpellWar plugin;
	
	public HashMap<Player, Double> mana = new HashMap<Player, Double>();

	public PlayerListener(SpellWar plugin) {
		this.plugin = plugin;
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : mana.keySet()) {
					if(mana.get(p) < 64) {
						mana.put(p, (mana.get(p) + (MANAPERSECOUND/20) > 64 ? 64 : mana.get(p) + (MANAPERSECOUND/20)));
						ItemStack emeralds =  new ItemStack(Material.EMERALD_BLOCK);
						emeralds.setAmount((int) Math.floor(mana.get(p)));
						p.getInventory().setItem(8, emeralds);
					}
				}
			}
			
		}.runTaskTimer(plugin, 0, 1);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		mana.put(p, 64.0);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		mana.remove(p);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(e.getItem().getType() == Material.STICK) {
				double pmana = mana.get(p);
				final double range = (pmana >= 25 ? 25 : pmana);
				mana.put(p, (pmana >= 25 ? pmana-25 : 0));
				new BukkitRunnable() {
					final Location startloc = p.getEyeLocation();
					final Player shooter = p;
					double i = 0.5;
					@Override
					public void run() {
						for(int ticks = 0; ticks < 3; ticks ++) {
							Location loc = startloc.clone().add(startloc.clone().getDirection().multiply(i));
							loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.12, 0.12, 0.12);
							if(loc.getBlock().getType() != Material.AIR) {
								for(Player alle : loc.getWorld().getPlayers()) {
									if(alle.getLocation().distance(loc) < 3 || p.getEyeLocation().distance(loc) < 3) {
										alle.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*5, 1));
									}
								}
								cancel();
								break;
							}
							for(Player a : loc.getWorld().getPlayers()) {
								if(a != shooter) {
									if(isInHitbox(loc, a.getLocation())) {
										for(Player alle : loc.getWorld().getPlayers()) {
											if(alle.getLocation().distance(loc) < 3 || p.getEyeLocation().distance(loc) < 3) {
												alle.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*5, 2));
											}
										}
										break;
									}
								}
							}
							i += 0.5;
							if(i > range) {
								cancel();
								break;
							}
						}
					}

				}.runTaskTimer(plugin, 0, 1);
			}
		}
	}

	public boolean isInHitbox(Location loc, Location playerloc) {
		double miny = playerloc.getY()-0.2;
		double maxy = playerloc.getY()+2.0;
		if(loc.getY() > miny && loc.getY() < maxy) {
			double minx = playerloc.getX()-0.4;
			double maxx = playerloc.getX()+0.4;
			if(loc.getX() > minx && loc.getX() < maxx) {
				double minz = playerloc.getZ()-0.4;
				double maxz = playerloc.getZ()+0.4;
				if(loc.getZ() > minz && loc.getZ() < maxz) {
					return true;
				}
			}
		}
		return false;
	}

}
