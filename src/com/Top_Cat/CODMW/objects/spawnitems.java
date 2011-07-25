package com.Top_Cat.CODMW.objects;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class spawnitems {
    
    HashMap<Material, spawnmaterial> sitems = new HashMap<Material, spawnmaterial>();
    
    public void setItem(Material item, int ammount, int slot) {
        if (sitems.containsKey(item)) {
            spawnmaterial si = sitems.get(item);
            si.setAmmount(ammount);
            si.setSlot(ammount);
        } else {
            sitems.put(item, new spawnmaterial(ammount, slot));
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
    
    public void give(player p) {
        for (Material i : sitems.keySet()) {
            spawnmaterial sm = sitems.get(i);
            p.giveItem(sm.getSlot(), new ItemStack(i, sm.getAmmount()));
        }
    }
    
}