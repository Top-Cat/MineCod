package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.sql.Stat;

public class pork extends useable {

    public pork(main instance, Player owner, Object[] args) {
        super(instance, owner, args);
        getOwnerplayer().s.incStat(Stat.PORK_USED);
    }
    
    @Override
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        if (attacker == getOwner()) {
            damage *= 2;
        }
        return damage;
    }
    
    @Override
    public void teamSwitch() {
        super.teamSwitch();
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getLifetime() > 10) {
            destroy();
        }
    }
    
}