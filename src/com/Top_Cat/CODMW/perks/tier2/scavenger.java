package com.Top_Cat.CODMW.perks.tier2;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class scavenger extends tier2 {

    public scavenger(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (name == "todrop") {
            if (p == getOwnerplayer()) {
                def = 0;
            } else if (p.lastkiller == getOwnerplayer()) {
                def = (int) def * 2;
            }
        }
        return def;
    }
    
}