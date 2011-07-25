package com.Top_Cat.CODMW.perks;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.perks.tier1.bandolier;
import com.Top_Cat.CODMW.perks.tier1.demolitionist;
import com.Top_Cat.CODMW.perks.tier1.ironknees;
import com.Top_Cat.CODMW.perks.tier1.martyrdom;
import com.Top_Cat.CODMW.perks.tier1.slightofhand;
import com.Top_Cat.CODMW.perks.tier2.commando;
import com.Top_Cat.CODMW.perks.tier2.jackpot;
import com.Top_Cat.CODMW.perks.tier2.juggernaut;
import com.Top_Cat.CODMW.perks.tier2.medic;
import com.Top_Cat.CODMW.perks.tier2.scavenger;
import com.Top_Cat.CODMW.perks.tier3.ghost;
import com.Top_Cat.CODMW.perks.tier3.hardline;
import com.Top_Cat.CODMW.perks.tier3.killfeeder;
import com.Top_Cat.CODMW.perks.tier3.stoppingpower;

public enum Perks {
    MARTYRDOM(0, Tiers.TIER1, martyrdom.class),
    IRONKNEES(1, Tiers.TIER1, ironknees.class),
    SLIGHTOFHAND(2, Tiers.TIER1, slightofhand.class),
    BANDOLIER(3, Tiers.TIER1, bandolier.class),
    DEMOLITIONIST(4, Tiers.TIER1, demolitionist.class),
    COMMANDO(5, Tiers.TIER2, commando.class),
    JACKPOT(6, Tiers.TIER2, jackpot.class),
    JUGGERNAUT(7, Tiers.TIER2, juggernaut.class),
    MEDIC(8, Tiers.TIER2, medic.class),
    SCAVENGER(9, Tiers.TIER2, scavenger.class),
    GHOST(10, Tiers.TIER3, ghost.class),
    HARDLINE(11, Tiers.TIER3, hardline.class),
    KILLFEEDER(12, Tiers.TIER3, killfeeder.class),
    STOPPINGPOWER(13, Tiers.TIER3, stoppingpower.class),
    ;
    
    final Class<? extends perk> k;
    final Tiers t;
    final int id;
    
    private Perks(int id, Tiers t, Class<? extends perk> k) {
        this.id = id;
        this.k = k;
        this.t = t;
    }
    
    public perk create(main instance, Player owner) {
        try {
            Constructor<? extends perk> c = k.getDeclaredConstructor(new Class[] {main.class, Player.class});
            return c.newInstance(instance, owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}