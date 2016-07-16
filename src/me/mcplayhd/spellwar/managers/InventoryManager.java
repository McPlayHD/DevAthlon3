package me.mcplayhd.spellwar.managers;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mcplayhd.spellwar.enums.EnumAbilitys.Abilitys;

public class InventoryManager {
	
	private HashMap<Abilitys, ItemStack> items = new HashMap<Abilitys, ItemStack>();

	public InventoryManager() {
		ItemStack feather = new ItemStack(Material.FEATHER, 1);
		ItemMeta featherm = feather.getItemMeta();
		featherm.setDisplayName("�fLevitation �emagic wand");
		feather.setItemMeta(featherm);
		items.put(Abilitys.LEVITATION, feather);
		
		ItemStack wart = new ItemStack(Material.NETHER_STALK, 1);
		ItemMeta wartm = wart.getItemMeta();
		wartm.setDisplayName("�4Damage �emagic wand");
		wart.setItemMeta(wartm);
		items.put(Abilitys.DAMAGE, wart);
		
		ItemStack cream = new ItemStack(Material.MAGMA_CREAM, 1);
		ItemMeta creamm = cream.getItemMeta();
		creamm.setDisplayName("�2Posion �emagic wand");
		cream.setItemMeta(creamm);
		items.put(Abilitys.POSION, cream);
		
		ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
		ItemMeta axem = axe.getItemMeta();
		axem.setDisplayName("�8Wither �emagic wand");
		axe.setItemMeta(axem);
		items.put(Abilitys.WITHER, axe);
	}
	
	public void sendInventory(Player p) {
		p.getInventory().setItem(0, items.get(Abilitys.LEVITATION));
		p.getInventory().setItem(1, items.get(Abilitys.DAMAGE));
		p.getInventory().setItem(2, items.get(Abilitys.POSION));
		p.getInventory().setItem(3, items.get(Abilitys.WITHER));
		
		p.getInventory().setItem(8, new ItemStack(Material.EMERALD, 64));
	}
	
}