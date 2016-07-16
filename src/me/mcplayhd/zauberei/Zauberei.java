package me.mcplayhd.zauberei;

import org.bukkit.plugin.java.JavaPlugin;

import me.mcplayhd.zauberei.Listeners.PlayerListener;

public class Zauberei extends JavaPlugin {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}
	
}
