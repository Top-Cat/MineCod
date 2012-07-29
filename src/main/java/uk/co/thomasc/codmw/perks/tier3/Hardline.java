package uk.co.thomasc.codmw.perks.tier3;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Hardline extends Tier3 {

	public Hardline(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (p == getOwnerplayer() && name == "streakoffset") {
			def = 1;
		}
		return def;
	}
	
}