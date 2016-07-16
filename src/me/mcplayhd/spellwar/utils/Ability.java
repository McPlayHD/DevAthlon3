package me.mcplayhd.spellwar.utils;

import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import me.mcplayhd.spellwar.SpellWar.Abilitys;

public class Ability {
	
	private Abilitys abilitys;
	private int range;
	private int max_mana;
	private int min_mana;
	private MaterialData block_crack;
	private PotionEffect effect;
	
	public Ability(Abilitys abilitys, int range, int max_mana, int min_mana, MaterialData block_crack, PotionEffect effect) {
		this.abilitys = abilitys;
		this.range = range;
		this.max_mana = max_mana;
		this.min_mana = min_mana;
		this.block_crack = block_crack;
		this.effect = effect;
	}
	
	public Abilitys getAbility() {
		return abilitys;
	}
	
	public int getRange() {
		return range;
	}
	
	public int getMinMana() {
		return min_mana;
	}
	
	public int getMaxMana() {
		return max_mana;
	}
	
	public MaterialData getBlockCrackMaterialData() {
		return block_crack;
	}
	
	public PotionEffect getPotionEffect() {
		return effect;
	}

}
