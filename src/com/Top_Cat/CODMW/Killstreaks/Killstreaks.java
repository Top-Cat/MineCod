package com.Top_Cat.CODMW.Killstreaks;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.Stat;

public enum Killstreaks {
    CLAYMORES(1, "Claymores", 3, claymore.class, Material.WALL_SIGN, Stat.CLAYMORES_ACHIEVED, 24, 2),
    POWER_PORK(2, "Power Pork", 5, pork.class, Material.GRILLED_PORK, Stat.PORK_ACHIEVED, 16),
    INVULNERABLE_APPLE(3, "Invulnerable Apple", 6, apple.class, Material.APPLE, Stat.APPLES_ACHIEVED, 19),
    SENTRY(4, "Sentry", 7, sentry.class, Material.DISPENSER, Stat.SENTRIES_ACHIEVED, 12),
    DOGS(5, "Dogs", 9, WolfPack.class, Material.BONE, Stat.DOGS_ACHIEVED, 6),
    HELICOPTER(6, "Chopper", 11, chopper.class, Material.DIAMOND, Stat.CHOPPERS_ACHIEVED, 4),
    UNLIMITED_ARROWS(7, "Unlimited Arrows", 6, arrows.class, Material.FLINT, Stat.UARROWS_ACHIEVED, 19),
    CARE_PACKAGE(8, "Care Package", 5, carepackage.class, Material.CHEST, Stat.CAREPACKAGES_ACHIEVED, 0),
    ;
    
    final private int id;
    final private int kills;
    final private Class<? extends killstreak> k;
    final private Material m;
    final private Stat ach;
    final private int amm;
    final private double prob;
    final private String name;

    private Killstreaks(int id, String name, int kills, Class<? extends killstreak> k, Material m, Stat ach, double prob, int amm) {
        this.id = id;
        this.kills = kills;
        this.k = k;
        this.m = m;
        this.ach = ach;
        this.amm = amm;
        this.prob = prob / 100;
        this.name = name;
    }
    
    private Killstreaks(int id, String name, int kills, Class<? extends killstreak> k, Material m, Stat ach, double prob) {
        this(id, name, kills, k, m, ach, prob, 1);
    }
    
    public int getId() {
        return id;
    }
    
    public int getKills() {
        return kills;
    }
    
    public Stat getStat() {
        return ach;
    }
    
    public int getAmm() {
        return amm;
    }
    
    @Override
    public String toString() {
    	return name;
    }
    
    public Class<? extends killstreak> getkClass() {
        return k;
    }
    
    public killstreak callIn(main instance, Player owner, Object[] args) {
        try {
            Constructor<? extends killstreak> c = k.getDeclaredConstructor(new Class[] {main.class, Player.class, Object[].class});
            killstreak n = c.newInstance(instance, owner, args);
            instance.ks.add(n);
            return n;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Material getMat() {
        return m;
    }
    
    public double getProb() {
        return prob;
    }
    
    public static Killstreaks getRandom() {
    	double p = Math.random();
    	for (Killstreaks i : table.values()) {
    		if (p < i.getProb()) {
    			return i;
    		} else {
    			p -= i.getProb();
    		}
    	}
    	return CLAYMORES;
    }
    
    public static HashMap<Integer, Killstreaks> table = new HashMap<Integer, Killstreaks>(); 
    static {
        for (Killstreaks i : Killstreaks.values()) {
            table.put(i.getId(), i);
        }
    }

    public static Killstreaks valueOf(int id) {
        return table.get(id);
    }
    
    public static HashMap<Material, Killstreaks> table2 = new HashMap<Material, Killstreaks>(); 
    static {
        for (Killstreaks i : Killstreaks.values()) {
            table2.put(i.getMat(), i);
        }
    }

    public static Killstreaks fromMaterial(Material m) {
        return table2.get(m);
    }
    
}