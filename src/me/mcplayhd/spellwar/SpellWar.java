package me.mcplayhd.spellwar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.mcplayhd.spellwar.listeners.PlayerListener;

public class SpellWar extends JavaPlugin {
	
	public PlayerListener pl;

	public void onEnable() {
		pl = new PlayerListener(this);
		getServer().getPluginManager().registerEvents(pl, this);
		for(Player alle : Bukkit.getOnlinePlayers()) {
			pl.mana.put(alle, 64.0);
		}
	}
	
}
