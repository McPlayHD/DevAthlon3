package me.mcplayhd.spellwar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.mcplayhd.spellwar.listeners.PlayerListener;
import me.mcplayhd.spellwar.utils.AbilityManager;

public class SpellWar extends JavaPlugin {
	
	public final String prefix = "§2SpellWar §7| ";
	
	public PlayerListener pl;
	public AbilityManager am;
	
	public void onEnable() {
		pl = new PlayerListener(this);
		am = new AbilityManager(this);
		getServer().getPluginManager().registerEvents(pl, this);
		for(Player alle : Bukkit.getOnlinePlayers()) {
			am.mana.put(alle, 64.0);
		}
	}
	
}
