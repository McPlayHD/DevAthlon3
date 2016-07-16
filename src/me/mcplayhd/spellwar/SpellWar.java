package me.mcplayhd.spellwar;

import org.bukkit.plugin.java.JavaPlugin;

import me.mcplayhd.spellwar.Listeners.PlayerListener;

public class SpellWar extends JavaPlugin {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}
	
}
