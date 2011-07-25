package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.spawnitems;

public class demolitionist extends tier1 {

    public demolitionist(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public void onRespawn(player p, spawnitems inventory) {
        if (getOwnerplayer() == p) {
            inventory.setAmmount(Material.SNOW_BALL, 3);
        }
    }
    
}