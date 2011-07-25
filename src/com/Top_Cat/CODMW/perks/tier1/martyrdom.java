package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.grenade;
import com.Top_Cat.CODMW.objects.player;

public class martyrdom extends tier1 {
    
    public martyrdom(main instance, Player owner) {
        super(instance, owner);
    }

    @Override
    public void afterDeath(player p) {
        super.afterDeath(p);
        new grenade(plugin, p.p, p.dropl);
    }
    
}