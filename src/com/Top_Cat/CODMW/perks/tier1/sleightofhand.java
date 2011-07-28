package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class sleightofhand extends tier1 {

    public sleightofhand(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (getOwnerplayer() == p && name == "reloadtime") {
            def = 1000;
        }
        return def;
    }    
    
}