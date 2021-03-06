package me.mcplayhd.spellwar.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.mcplayhd.spellwar.SpellWar;
import me.mcplayhd.spellwar.enums.EnumAbilitys.Abilitys;
import me.mcplayhd.spellwar.utils.Ability;

public class AbilityManager {

	private SpellWar plugin;

	final int MANAPERSECOUND = 10;

	public HashMap<Player, Double> mana = new HashMap<Player, Double>();
	public HashMap<Material, Ability> getAbility = new HashMap<Material, Ability>();
	public HashMap<Player, Player> levitation = new HashMap<Player, Player>();
	public HashMap<Player, Player> wither = new HashMap<Player, Player>();

	public AbilityManager(SpellWar plugin) {
		this.plugin = plugin;
		for(Player alle : Bukkit.getOnlinePlayers()) {
			mana.put(alle, 64.0);
		}
		getAbility.put(Material.FEATHER, new Ability(Abilitys.LEVITATION, 25, 25, 10, new MaterialData(Material.SNOW), new PotionEffect(PotionEffectType.LEVITATION, 20*5, 1)));
		getAbility.put(Material.NETHER_STALK, new Ability(Abilitys.DAMAGE, 10, 30, 20, new MaterialData(Material.REDSTONE_BLOCK), null));
		getAbility.put(Material.MAGMA_CREAM, new Ability(Abilitys.POSION, 10, 20, 10, new MaterialData(Material.CACTUS), new PotionEffect(PotionEffectType.POISON, 20*6, 0)));
		getAbility.put(Material.COAL, new Ability(Abilitys.WITHER, 15, 25, 10, new MaterialData(Material.NETHER_BRICK), new PotionEffect(PotionEffectType.WITHER, 20*10, 0)));
		getAbility.put(Material.SUGAR, new Ability(Abilitys.SLOW, 25, 25, 10, new MaterialData(Material.PACKED_ICE), new PotionEffect(PotionEffectType.SLOW, 20*5, 2)));
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player p : mana.keySet()) {
					if(mana.get(p) < 64) {
						mana.put(p, (mana.get(p) + (MANAPERSECOUND/10) > 64 ? 64 : mana.get(p) + (MANAPERSECOUND/10)));
						ItemStack mana = plugin.im.getManaItem();
						mana.setAmount((int) Math.floor(plugin.am.mana.get(p)));
						p.getInventory().setItem(8, mana);
					}
				}
			}
		}.runTaskTimer(plugin, 0, 2);
	}

	public void shootSpell(Player p, Ability a) {
		double player_mana = mana.get(p);
		if(player_mana < a.getMinMana()) {
			p.sendMessage(plugin.prefix + "�cDu hast nicht genug �aMana");
			return;
		}
		final double range = (player_mana >= a.getMaxMana() ? a.getRange() : (a.getRange()*player_mana)/a.getMaxMana());
		mana.put(p, (player_mana >= a.getMaxMana() ? player_mana-a.getMaxMana() : 0));
		new BukkitRunnable() {
			final Location startloc = p.getEyeLocation();
			final Player shooter = p;
			double i = 0.5;
			@Override
			public void run() {
				for(int ticks = 0; ticks < 3; ticks ++) {
					Location loc = startloc.clone().add(startloc.clone().getDirection().multiply(i));
					loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.12, 0.12, 0.12);
					loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.12, 0.12, 0.12, a.getBlockCrackMaterialData());
					if(loc.getBlock().getType() != Material.AIR && loc.getBlock().getType().isBlock() && loc.getBlock().getType().isSolid()) {
						explosion(p, a, loc, startloc.clone().getDirection().multiply(0.1));
						cancel();
						return;
					}
					for(Player alle : loc.getWorld().getPlayers()) {
						if(!plugin.save.contains(alle)) {
							if(alle != shooter) {
								if(isInHitbox(loc, alle.getLocation())) {
									explosion(p, a, loc, startloc.clone().getDirection().multiply(0.1));
									cancel();
									return;
								}
							}
						}
					}
					i += 0.5;
					if(i > range) {
						cancel();
						return;
					}
				}
			}

		}.runTaskTimer(plugin, 0, 1);
	}

	private void explosion(Player p, Ability a, Location loc, Vector vector) {
		for(Player alle : loc.getWorld().getPlayers()) {
			if(!plugin.save.contains(alle)) {
				if(alle.getLocation().distance(loc) < 2 || alle.getEyeLocation().distance(loc) < 2) {
					alle.setVelocity(p.getLocation().getDirection().add(vector));
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
						if(alle.getHealth() > 0.0) {
							alle.damage(5.0);
							if(alle.getHealth() <= 0.0) {
								killPlayer(alle, p, Abilitys.DAMAGE);
							}
						}
					}
				}
			}
		}
	}

	public void killPlayer(Player killed, Player killer, Abilitys a) {
		String deathmessage = plugin.prefix + "�a" + killed.getDisplayName() + " �e";
		if(killer == null || killer == killed) {
			deathmessage += "hat �4Suizid �ebegangen";
			plugin.getServer().broadcastMessage(deathmessage);
			return;
		}
		deathmessage += "wurde von �a" + killer.getDisplayName() + " �e";
		switch(a) {
		case LEVITATION:
			deathmessage += "zu weit in die L�fte gehoben";
			break;
		case DAMAGE:
			deathmessage += "zertr�mmert";
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
