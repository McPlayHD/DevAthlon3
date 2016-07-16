package me.mcplayhd.spellwar.utils;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbilityManager {
	
	public HashMap<Material, Ability> getAbility = new HashMap<Material, Ability>();

	public enum Abilitys {
		LEVITATION,
		DAMAGE,
		POSION,
	}

	public AbilityManager() {
		for(Abilitys ab : Abilitys.values()) {
			switch(ab) {
			case LEVITATION:
				getAbility.put(Material.FEATHER, new Ability(25, 25, 10, new MaterialData(Material.SNOW), new PotionEffect(PotionEffectType.LEVITATION, 20*5, 1)));
				break;
			case DAMAGE:

				break;
			case POSION:

				break;
			default:
				break;
			}
		}
	}

}
