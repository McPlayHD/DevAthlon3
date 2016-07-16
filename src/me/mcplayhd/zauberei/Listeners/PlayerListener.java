package me.mcplayhd.zauberei.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mcplayhd.zauberei.Zauberei;

public class PlayerListener implements Listener {

	Zauberei plugin;
	
	public PlayerListener(Zauberei plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR) {
			if(e.getItem().getType() == Material.STICK) {
				final Location startloc = p.getEyeLocation();
				for(int i = 1; i < 15; i += 0.5) {
					Location loc = startloc.clone().add(startloc.clone().multiply(i));
					loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.12, 0.12, 0.12);
					if(loc.getBlock().getType() != Material.AIR) {
						for(Player alle : loc.getWorld().getPlayers()) {
							if(alle.getLocation().distance(loc) < 3 || p.getEyeLocation().distance(loc) < 3) {
								alle.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20*5, 0));
							}
						}
						break;
					}
				}
			}
		}
	}
	
}
