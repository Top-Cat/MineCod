package uk.co.thomasc.codmw.killstreaks.useable;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.sql.Stat;

public class Pork extends Useable {

	public Pork(Main instance, Player owner, Object[] args) {
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