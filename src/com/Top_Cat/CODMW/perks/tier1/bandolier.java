package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.spawnitems;

public class bandolier extends tier1 {
    
    public bandolier(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public void onRespawn(player p, spawnitems inventory) {
        super.onRespawn(p, inventory);
        if (getOwnerplayer() == p) {
            inventory.setAmmount(Material.FEATHER, 99);
            inventory.setAmmount(Material.ARROW, 18);
        }
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (getOwnerplayer() == p && name == "maxclip") {
            def = 18;
        }
        return def;
    }
    
}