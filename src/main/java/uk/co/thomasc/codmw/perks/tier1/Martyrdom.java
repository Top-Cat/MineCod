package uk.co.thomasc.codmw.perks.tier1;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.Grenade;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Martyrdom extends Tier1 {
	
	public Martyrdom(Main instance, Player owner) {
		super(instance, owner);
	}

	@Override
	public void afterDeath(CPlayer p) {
		if (getOwnerplayer() == p) {;
			new Grenade(plugin, p.p, p.dropl);
		}
	}
	
}