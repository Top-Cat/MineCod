package uk.co.thomasc.codmw.killstreaks;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.objects.SpawnItems;

public class Killstreak extends MineCodListener {
	
	int starttick = 0;
	
	public Killstreak(Main instance, Player owner, Object[] args) {
		super(instance, owner);
		starttick = plugin.game.time;
		getOwnerplayer().addPoints(3);
	}
	
	public int getLifetime() {
		return plugin.game.time - starttick;
	}

	public void teamSwitch() {
		
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