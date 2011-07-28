package com.Top_Cat.CODMW.perks;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.perks.tier1.bandolier;
import com.Top_Cat.CODMW.perks.tier1.demolitionist;
import com.Top_Cat.CODMW.perks.tier1.ironknees;
import com.Top_Cat.CODMW.perks.tier1.martyrdom;
import com.Top_Cat.CODMW.perks.tier1.sleightofhand;
import com.Top_Cat.CODMW.perks.tier2.commando;
import com.Top_Cat.CODMW.perks.tier2.jackpot;
import com.Top_Cat.CODMW.perks.tier2.juggernaut;
import com.Top_Cat.CODMW.perks.tier2.medic;
import com.Top_Cat.CODMW.perks.tier2.scavenger;
import com.Top_Cat.CODMW.perks.tier3.ghost;
import com.Top_Cat.CODMW.perks.tier3.hardline;
import com.Top_Cat.CODMW.perks.tier3.killfeeder;
import com.Top_Cat.CODMW.perks.tier3.stoppingpower;
import com.Top_Cat.CODMW.sql.Achievement;

public enum Perks {
    MARTYRDOM(0, Tiers.TIER1, martyrdom.class, new Achievement[] {Achievement.COLLATERAL_FRAG}),
    IRONKNEES(1, Tiers.TIER1, ironknees.class, new Achievement[] {Achievement.FALL_DAMAGE_25}),
    SLIGHTOFHAND(2, Tiers.TIER1, sleightofhand.class, new Achievement[] {Achievement.RAPID_FIRE}),
    BANDOLIER(3, Tiers.TIER1, bandolier.class, new Achievement[] {Achievement.FIRE_1000}),
    DEMOLITIONIST(4, Tiers.TIER1, demolitionist.class, new Achievement[] {Achievement.GRENADE_KILLS_5}),
    COMMANDO(5, Tiers.TIER2, commando.class, new Achievement[] {Achievement.KNIFE_KILLS_25}),
    JACKPOT(6, Tiers.TIER2, jackpot.class, new Achievement[] {Achievement.CARE_GET_10}),
    MEDIC(8, Tiers.TIER2, medic.class, new Achievement[] {Achievement.CLOSE_CALL}),
    SCAVENGER(9, Tiers.TIER2, scavenger.class, new Achievement[] {Achievement.FIRE_2500}),
    JUGGERNAUT(7, Tiers.TIER3, juggernaut.class, new Achievement[] {Achievement.KILL_250}),
    GHOST(10, Tiers.TIER3, ghost.class, new Achievement[] {Achievement.CLAYMORE_GET_25, Achievement.SENTRY_GET_10, Achievement.CHOPPER_GET_5}),
    HARDLINE(11, Tiers.TIER3, hardline.class, new Achievement[] {Achievement.WIN_25}),
    KILLFEEDER(12, Tiers.TIER3, killfeeder.class, new Achievement[] {Achievement.WARGASM}),
    STOPPINGPOWER(13, Tiers.TIER3, stoppingpower.class, new Achievement[] {Achievement.KILL_250}),
    ;
    
    final Class<? extends perk> k;
    final Tiers t;
    final int id;
    final List<Achievement> requires;
    
    private Perks(int id, Tiers t, Class<? extends perk> k, Achievement[] requires) {
        this.id = id;
        this.k = k;
        this.t = t;
        this.requires = Arrays.asList(requires);
    }
    
    public perk create(main instance, player owner) {
        if (owner.s.achs.containsAll(requires)) {
            try {
                Constructor<? extends perk> c = k.getDeclaredConstructor(new Class[] {main.class, Player.class});
                return c.newInstance(instance, owner.p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static HashMap<Integer, Perks> table = new HashMap<Integer, Perks>(); 
    static {
        for (Perks i : Perks.values()) {
            table.put(i.getId(), i);
        }
    }

    public static Perks valueOf(int id) {
        return table.get(id);
    }

    public int getId() {
        return id;
    }
    
    public Tiers getTier() {
        return t;
    }
}