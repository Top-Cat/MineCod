package com.Top_Cat.CODMW.perks.tier3;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class ghost extends tier3 {

    public ghost(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (p == getOwnerplayer() && name == "ghost") {
            def = 1;
        }
        return def;
    }
    
}