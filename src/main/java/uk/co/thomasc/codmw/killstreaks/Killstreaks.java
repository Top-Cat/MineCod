package uk.co.thomasc.codmw.killstreaks;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.getspout.spoutapi.material.Material;
import org.getspout.spoutapi.material.MaterialData;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.killstreaks.placeable.Claymore;
import uk.co.thomasc.codmw.killstreaks.placeable.Sentry;
import uk.co.thomasc.codmw.killstreaks.useable.WolfPack;
import uk.co.thomasc.codmw.killstreaks.useable.Apple;
import uk.co.thomasc.codmw.killstreaks.useable.Arrows;
import uk.co.thomasc.codmw.killstreaks.useable.CarePackage;
import uk.co.thomasc.codmw.killstreaks.useable.Chopper;
import uk.co.thomasc.codmw.killstreaks.useable.Pork;
import uk.co.thomasc.codmw.sql.Stat;

public enum Killstreaks {
	CLAYMORES(1, "Claymores", 3, Claymore.class, MaterialData.wallSign, Stat.CLAYMORES_ACHIEVED, 24, 2),
	POWER_PORK(2, "Power Pork", 5, Pork.class, MaterialData.cookedPorkchop, Stat.PORK_ACHIEVED, 16),
	INVULNERABLE_APPLE(3, "Invulnerable Apple", 6, Apple.class, MaterialData.redApple, Stat.APPLES_ACHIEVED, 19),
	SENTRY(4, "Sentry", 7, Sentry.class, MaterialData.dispenser, Stat.SENTRIES_ACHIEVED, 12),
	DOGS(5, "Dogs", 9, WolfPack.class, MaterialData.bone, Stat.DOGS_ACHIEVED, 6),
	HELICOPTER(6, "Chopper", 11, Chopper.class, MaterialData.diamond, Stat.CHOPPERS_ACHIEVED, 4),
	UNLIMITED_ARROWS(7, "Unlimited Arrows", 6, Arrows.class, MaterialData.flint, Stat.UARROWS_ACHIEVED, 19),
	CARE_PACKAGE(8, "Care Package", 5, CarePackage.class, MaterialData.chest, Stat.CAREPACKAGES_ACHIEVED, 0),
	;
	
	final private int id;
	final private int kills;
	final private Class<? extends Killstreak> k;
	final private Material m;
	final private Stat ach;
	final private int amm;
	final private double prob;
	final private String name;

	private Killstreaks(int id, String name, int kills, Class<? extends Killstreak> k, Material m, Stat ach, double prob, int amm) {
		this.id = id;
		this.kills = kills;
		this.k = k;
		this.m = m;
		this.ach = ach;
		this.amm = amm;
		this.prob = prob / 100;
		this.name = name;
	}
	
	private Killstreaks(int id, String name, int kills, Class<? extends Killstreak> k, Material m, Stat ach, double prob) {
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
	
	public Class<? extends Killstreak> getkClass() {
		return k;
	}
	
	public Killstreak callIn(Main instance, Player owner, Object[] args) {
		try {
			Constructor<? extends Killstreak> c = k.getDeclaredConstructor(new Class[] {Main.class, Player.class, Object[].class});
			return c.newInstance(instance, owner, args);
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
	
	public static HashMap<org.bukkit.Material, Killstreaks> table2 = new HashMap<org.bukkit.Material, Killstreaks>(); 
	static {
		for (Killstreaks i : Killstreaks.values()) {
			table2.put(org.bukkit.Material.getMaterial(i.getMat().getRawId()), i);
		}
	}

	public static Killstreaks fromMaterial(Integer m) {
		return table2.get(org.bukkit.Material.getMaterial(m));
	}
	
}