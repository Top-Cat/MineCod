package uk.co.thomasc.codmw.perks.tier2;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Scavenger extends Tier2 {

	public Scavenger(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (name == "todrop") {
			if (p == getOwnerplayer()) {
				def = 0;
			} else if (p.lastkiller == getOwnerplayer()) {
				def = (int) def * 2;
			}
		}
		return def;
	}
	
}