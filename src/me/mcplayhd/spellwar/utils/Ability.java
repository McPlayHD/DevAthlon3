package me.mcplayhd.spellwar.utils;

import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

public class Ability {
	
	private int reach;
	private int max_mana;
	private int min_mana;
	private MaterialData block_crack;
	private PotionEffect effect;
	
	public Ability(int reach, int max_mana, int min_mana, MaterialData block_crack, PotionEffect effect) {
		this.reach = reach;
		this.max_mana = max_mana;
		this.min_mana = min_mana;
		this.block_crack = block_crack;
		this.effect = effect;
	}
	
	public int getReach() {
		return reach;
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
