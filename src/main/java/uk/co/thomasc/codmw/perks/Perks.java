package uk.co.thomasc.codmw.perks;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.perks.tier1.Bandolier;
import uk.co.thomasc.codmw.perks.tier1.Demolitionist;
import uk.co.thomasc.codmw.perks.tier1.IronKnees;
import uk.co.thomasc.codmw.perks.tier1.Martyrdom;
import uk.co.thomasc.codmw.perks.tier1.SleightOfHand;
import uk.co.thomasc.codmw.perks.tier2.Commando;
import uk.co.thomasc.codmw.perks.tier2.Jackpot;
import uk.co.thomasc.codmw.perks.tier2.Juggernaut;
import uk.co.thomasc.codmw.perks.tier2.Medic;
import uk.co.thomasc.codmw.perks.tier2.Scavenger;
import uk.co.thomasc.codmw.perks.tier3.Ghost;
import uk.co.thomasc.codmw.perks.tier3.Hardline;
import uk.co.thomasc.codmw.perks.tier3.Killfeeder;
import uk.co.thomasc.codmw.perks.tier3.StoppingPower;
import uk.co.thomasc.codmw.sql.Achievement;

public enum Perks {
	MARTYRDOM(0, Tiers.TIER1, Martyrdom.class, new Achievement[] {Achievement.COLLATERAL_FRAG}),
	IRONKNEES(1, Tiers.TIER1, IronKnees.class, new Achievement[] {Achievement.FALL_DAMAGE_25}),
	SLIGHTOFHAND(2, Tiers.TIER1, SleightOfHand.class, new Achievement[] {Achievement.RAPID_FIRE}),
	BANDOLIER(3, Tiers.TIER1, Bandolier.class, new Achievement[] {Achievement.FIRE_1000}),
	DEMOLITIONIST(4, Tiers.TIER1, Demolitionist.class, new Achievement[] {Achievement.GRENADE_KILLS_5}),
	COMMANDO(5, Tiers.TIER2, Commando.class, new Achievement[] {Achievement.KNIFE_KILLS_25}),
	JACKPOT(6, Tiers.TIER2, Jackpot.class, new Achievement[] {Achievement.CARE_GET_10}),
	MEDIC(8, Tiers.TIER2, Medic.class, new Achievement[] {Achievement.CLOSE_CALL}),
	SCAVENGER(9, Tiers.TIER2, Scavenger.class, new Achievement[] {Achievement.FIRE_2500}),
	JUGGERNAUT(7, Tiers.TIER3, Juggernaut.class, new Achievement[] {Achievement.KILL_250}),
	GHOST(10, Tiers.TIER3, Ghost.class, new Achievement[] {Achievement.CLAYMORE_GET_25, Achievement.SENTRY_GET_10, Achievement.CHOPPER_GET_5}),
	HARDLINE(11, Tiers.TIER3, Hardline.class, new Achievement[] {Achievement.WIN_25}),
	KILLFEEDER(12, Tiers.TIER3, Killfeeder.class, new Achievement[] {Achievement.WARGASM}),
	STOPPINGPOWER(13, Tiers.TIER3, StoppingPower.class, new Achievement[] {Achievement.KILL_250}),
	;
	
	final Class<? extends Perk> k;
	final Tiers t;
	final int id;
	final List<Achievement> requires;
	
	private Perks(int id, Tiers t, Class<? extends Perk> k, Achievement[] requires) {
		this.id = id;
		this.k = k;
		this.t = t;
		this.requires = Arrays.asList(requires);
	}
	
	public Perk create(Main instance, CPlayer owner) {
		if (owner.s.achs.containsAll(requires)) {
			try {
				Constructor<? extends Perk> c = k.getDeclaredConstructor(new Class[] {Main.class, Player.class});
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