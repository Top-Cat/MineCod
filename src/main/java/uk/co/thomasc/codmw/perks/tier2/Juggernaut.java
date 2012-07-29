package uk.co.thomasc.codmw.perks.tier2;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.Reason;

public class Juggernaut extends Tier2 {

	public Juggernaut(Main instance, Player owner) {
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