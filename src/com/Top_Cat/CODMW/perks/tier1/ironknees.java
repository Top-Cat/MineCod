package com.Top_Cat.CODMW.perks.tier1;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.Reason;

public class ironknees extends tier1 {

    public ironknees(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        if (getOwner() == defender && reason == Reason.FALL) {
            damage = 0;
        }
        return damage;
    }
    
}