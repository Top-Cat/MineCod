package com.Top_Cat.CODMW.perks.tier3;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;

public class stoppingpower extends tier3 {

    public stoppingpower(main instance, Player owner) {
        super(instance, owner);
    }
    
    @Override
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        if (attacker == getOwner()) {
            damage = (int) Math.ceil(damage * 1.2);
        }
        return damage;
    }
    
    @Override
    public double getVar(player p, String name, double def) {
        if (p == getOwnerplayer() && name == "grenadedmg") {
            def = 1;
        }
        return def;
    }
    
}