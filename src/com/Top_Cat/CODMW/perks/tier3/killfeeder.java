package com.Top_Cat.CODMW.perks.tier3;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class killfeeder extends tier3 {

    public killfeeder(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (p == getOwnerplayer()) {
            if (name == "allstreak") {
                def = 1;
            } else if (name == "streakoffset") {
                def = -2;
            }
        }
        return def;
    }
    
}