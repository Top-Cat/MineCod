package uk.co.thomasc.codmw.perks.tier3;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;

public class StoppingPower extends Tier3 {

	public StoppingPower(Main instance, Player owner) {
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
	public double getVar(CPlayer p, String name, double def) {
		if (p == getOwnerplayer() && name == "grenadedmg") {
			def = 1;
		}
		return def;
	}
	
}