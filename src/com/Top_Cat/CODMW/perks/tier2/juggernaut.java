package com.Top_Cat.CODMW.perks.tier2;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.Reason;

public class juggernaut extends tier2 {

    public juggernaut(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        if (defender == getOwner()) {
            damage = (int) Math.floor(damage * 0.8);
        }
        return damage;
    }
    
}