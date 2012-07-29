package uk.co.thomasc.codmw.perks.tier1;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.objects.SpawnItems;

public class Bandolier extends Tier1 {
	
	public Bandolier(Main instance, Player owner) {
		super(instance, owner);
	}
	
	@Override
	public void onRespawn(CPlayer p, SpawnItems inventory) {
		super.onRespawn(p, inventory);
		if (getOwnerplayer() == p) {
			inventory.setAmmount(Material.FEATHER, 99);
			inventory.setAmmount(Material.ARROW, 18);
		}
	}
	
	@Override
	public double getVar(CPlayer p, String name, double def) {
		if (getOwnerplayer() == p && name == "maxclip") {
			def = 18;
		}
		return def;
	}
	
}