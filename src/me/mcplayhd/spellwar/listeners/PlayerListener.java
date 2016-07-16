package me.mcplayhd.spellwar.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		plugin.am.mana.remove(p);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getInventory().getItemInMainHand() != null) {
				switch(p.getInventory().getItemInMainHand().getType()) {
				case FEATHER:
				case NETHER_STALK:
				case MAGMA_CREAM:
				case IRON_AXE:
					plugin.am.shootSpell(p, plugin.am.getAbility.get(p.getInventory().getItemInMainHand().getType()));
					break;
				default:
					break;
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
	}

}
