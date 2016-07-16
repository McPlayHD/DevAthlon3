package me.mcplayhd.spellwar.utils;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.mcplayhd.spellwar.SpellWar;
import me.mcplayhd.spellwar.SpellWar.Abilitys;

public class AbilityManager {

	private SpellWar plugin;

	final int MANAPERSECOUND = 10;

	public HashMap<Player, Double> mana = new HashMap<Player, Double>();
	public HashMap<Material, Ability> getAbility = new HashMap<Material, Ability>();
	public HashMap<Player, Player> levitation = new HashMap<Player, Player>();
	public HashMap<Player, Player> wither = new HashMap<Player, Player>();

	public AbilityManager(SpellWar plugin) {
		this.plugin = plugin;
		for(Abilitys ab : Abilitys.values()) {
			switch(ab) {
			case LEVITATION:
				getAbility.put(Material.FEATHER, new Ability(ab, 25, 25, 10, new MaterialData(Material.SNOW), new PotionEffect(PotionEffectType.LEVITATION, 20*5, 1)));
				break;
			case DAMAGE:
				getAbility.put(Material.NETHER_STALK, new Ability(ab, 20, 20, 10, new MaterialData(Material.REDSTONE_BLOCK), null));
				break;
			case POSION:
				getAbility.put(Material.MAGMA_CREAM, new Ability(ab, 10, 30, 15, new MaterialData(Material.CACTUS), new PotionEffect(PotionEffectType.POISON, 20*6, 0)));
				break;
			case WITHER:
				getAbility.put(Material.IRON_AXE, new Ability(ab, 15, 25, 10, new MaterialData(Material.NETHER_BRICK), new PotionEffect(PotionEffectType.WITHER, 20*10, 0)));
				break;
			default:
				break;
			}
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : mana.keySet()) {
					if(mana.get(p) < 64) {
						mana.put(p, (mana.get(p) + (MANAPERSECOUND/10) > 64 ? 64 : mana.get(p) + (MANAPERSECOUND/10)));
						ItemStack emeralds =  new ItemStack(Material.EMERALD);
						emeralds.setAmount((int) Math.floor(mana.get(p)));
						p.getInventory().setItem(8, emeralds);
					}
				}
			}
		}.runTaskTimer(plugin, 0, 2);
	}

	public void shootSpell(Player p, Ability a) {
		double player_mana = mana.get(p);
		if(player_mana < a.getMinMana()) {
			p.sendMessage(plugin.prefix + "§cYou don't have enough mana");
			return;
		}
		final double range = (player_mana >= a.getMaxMana() ? a.getRange() : (a.getRange()*player_mana)/a.getMaxMana());
		mana.put(p, (player_mana >= a.getMaxMana() ? player_mana-a.getMaxMana() : 0));
		new BukkitRunnable() {
			final Location startloc = p.getEyeLocation();
			final Player shooter = p;
			//final Item drop = startloc.getWorld().dropItem(startloc, new ItemStack(Material.CHORUS_FRUIT_POPPED, 1));
			double i = 0.5;
			@Override
			public void run() {
				for(int ticks = 0; ticks < 3; ticks ++) {
					Location loc = startloc.clone().add(startloc.clone().getDirection().multiply(i));
					loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.12, 0.12, 0.12);
					loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.12, 0.12, 0.12, a.getBlockCrackMaterialData());
					//drop.teleport(loc); //Fuktioniert nicht?
					if(loc.getBlock().getType() != Material.AIR) {
						explosion(p, a, loc);
						//drop.remove();
						cancel();
						return;
					}
					for(Player alle : loc.getWorld().getPlayers()) {
						if(alle != shooter) {
							if(isInHitbox(loc, alle.getLocation())) {
								explosion(p, a, loc);
								//drop.remove();
								cancel();
								return;
							}
						}
					}
					i += 0.5;
					if(i > range) {
						//drop.remove();
						cancel();
						return;
					}
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}
	
	private void explosion(Player p, Ability a, Location loc) {
		for(Player alle : loc.getWorld().getPlayers()) {
			if(alle.getLocation().distance(loc) < 3 || alle.getEyeLocation().distance(loc) < 3) {
				if(a.getAbility() != Abilitys.DAMAGE) {
					alle.addPotionEffect(a.getPotionEffect());
					switch(a.getAbility()) {
					case LEVITATION:
						levitation.put(alle, p);
						break;
					case WITHER:
						wither.put(alle, p);
						break;
					default:
						break;
					}
				} else {
					alle.damage(5.0);
					if(alle.getHealth() <= 0.0) {
						killPlayer(alle, p, Abilitys.DAMAGE);
					}
				}
			}
		}
	}
	
	public void killPlayer(Player killed, Player killer, Abilitys a) {
		String deathmessage = plugin.prefix + "§a" + killed.getDisplayName() + " §e";
		if(killer == null) {
			deathmessage += "hat §4Suizid §ebegangen";
			plugin.getServer().broadcastMessage(deathmessage);
			return;
		}
		deathmessage += "wurde von §a" + killer.getDisplayName() + " §e";
		switch(a) {
		case LEVITATION:
			deathmessage += "zu weit in die Lüfte gehoben";
			break;
		case DAMAGE:
			deathmessage += "zerstört";
			break;
		case WITHER:
			deathmessage += "mit dunkler Magie erledigt";
			break;
		default:
			break;
		}
		plugin.getServer().broadcastMessage(deathmessage);
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
