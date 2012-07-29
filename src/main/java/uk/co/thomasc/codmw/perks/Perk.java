package uk.co.thomasc.codmw.perks;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.objects.SpawnItems;

public class Perk extends MineCodListener {

	public Perk(Main instance, Player owner) {
		super(instance, owner);
	}
	
	public void checkIsPerk() {
		ArrayList<Tiers> r = new ArrayList<Tiers>();
		for (Tiers i : getOwnerplayer().perks.keySet()) {
			if (!i.instanceOf(getOwnerplayer().perks.get(i))) {
				r.add(i);
			}
		}
		for (Tiers i : r) {
			getOwnerplayer().perks.remove(i);
			destroy();
		}
		if (!getOwnerplayer().perks.containsValue(this)) {
			destroy();
		}
	}

	@Override
	public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
		return damage;
	}

	@Override
	public void onKill(CPlayer attacker, CPlayer defender, Reason reason, Object ks) {
		
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		
	}

	@Override
	public void onMove(PlayerMoveEvent event, boolean blockmove) {
		
	}

	@Override
	public void tickfast() {
		
	}

	@Override
	public void tick() {
		checkIsPerk();
	}

	@Override
	public void afterDeath(CPlayer p) {
		
	}

	@Override
	public void onRespawn(CPlayer p, SpawnItems inventory) {
		
	}

	@Override
	public double getVar(CPlayer p, String name, double def) {
		return def;
	}
	
}