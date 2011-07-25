package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class slightofhand extends tier1 {

    public slightofhand(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (getOwner() == p && name == "reloadtime") {
            def = 1000;
        }
        return def;
    }    
    
}