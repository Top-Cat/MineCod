package com.Top_Cat.CODMW.perks.tier2;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class medic extends tier2 {

    public medic(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (p == getOwnerplayer() && name == "healwait") {
            def = 5000;
        }
        return def;
    }
    
}