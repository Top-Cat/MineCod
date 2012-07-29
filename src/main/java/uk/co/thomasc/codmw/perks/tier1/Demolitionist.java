package uk.co.thomasc.codmw.perks.tier1;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.objects.SpawnItems;

public class Demolitionist extends Tier1 {

	public Demolitionist(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public void onRespawn(CPlayer p, SpawnItems inventory) {
		if (getOwnerplayer() == p) {
			inventory.setAmmount(Material.SNOW_BALL, 3);
		}
	}
	
}