package uk.co.thomasc.codmw.perks.tier1;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class SleightOfHand extends Tier1 {

	public SleightOfHand(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (getOwnerplayer() == p && name == "reloadtime") {
			def = 1000;
		}
		return def;
	}	
	
}