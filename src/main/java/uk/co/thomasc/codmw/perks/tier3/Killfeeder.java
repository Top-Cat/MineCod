package uk.co.thomasc.codmw.perks.tier3;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Killfeeder extends Tier3 {

	public Killfeeder(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (p == getOwnerplayer()) {
			if (name == "allstreak") {
				def = 1;
			} else if (name == "streakoffset") {
				def = -2;
			}
		}
		return def;
	}
	
}