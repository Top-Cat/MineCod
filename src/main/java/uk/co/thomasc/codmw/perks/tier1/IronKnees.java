package uk.co.thomasc.codmw.perks.tier1;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.Reason;

public class IronKnees extends Tier1 {

	public IronKnees(Main instance, Player owner) {
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