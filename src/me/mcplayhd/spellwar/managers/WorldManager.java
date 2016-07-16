package me.mcplayhd.spellwar.managers;

import java.io.File;

import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.mcplayhd.spellwar.SpellWar;

public class WorldManager {

	SpellWar plugin;

	public WorldManager(SpellWar plugin) {
		this.plugin = plugin;
	}

	public boolean loadWorld(String name) {
		File dir = new File(name);
		if(dir.exists()) {
			if(!isLoaded(name)) {
				plugin.getServer().createWorld(new WorldCreator(name));
			}
			return true;
		}
		return false;
	}

	public boolean isLoaded(String name) {
		for(World w : plugin.getServer().getWorlds()) {
			if(w.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
