package uk.co.thomasc.codmw.perks.tier2;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Medic extends Tier2 {

	public Medic(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (p == getOwnerplayer() && name == "healwait") {
			def = 5000;
		}
		return def;
	}
	
}