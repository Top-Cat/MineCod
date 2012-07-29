package uk.co.thomasc.codmw.objects;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SpawnItems {
	
	HashMap<Material, SpawnMaterial> sitems = new HashMap<Material, SpawnMaterial>();
	
	public void setItem(Material item, int ammount, int slot) {
		if (sitems.containsKey(item)) {
			SpawnMaterial si = sitems.get(item);
			si.setAmmount(ammount);
			si.setSlot(ammount);
		} else {
			sitems.put(item, new SpawnMaterial(ammount, slot));
		}
	}
	
	public void setAmmount(Material item, int ammount) {
		if (sitems.containsKey(item)) {
			sitems.get(item).setAmmount(ammount);
		}
	}
	
	public void removeItem(Material item) {
		sitems.remove(item);
	}
	
	public void give(CPlayer p) {
		for (Material i : sitems.keySet()) {
			SpawnMaterial sm = sitems.get(i);
			p.giveItem(sm.getSlot(), new ItemStack(i, sm.getAmmount()));
		}
	}
	
}