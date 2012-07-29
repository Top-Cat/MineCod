package uk.co.thomasc.codmw.perks.tier2;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Commando extends Tier2 {

	public Commando(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (p == getOwnerplayer() && name == "meleerange") {
			def = 2.3;
		}
		return def;
	}
	
}