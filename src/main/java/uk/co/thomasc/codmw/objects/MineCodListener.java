package uk.co.thomasc.codmw.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import uk.co.thomasc.codmw.Main;

public abstract class MineCodListener extends Ownable {
	
	final protected Main plugin;
	
	public MineCodListener(Main instance, Player owner) {
		plugin = instance;
		setOwner(owner, plugin.p(owner));
		plugin.listeners.add(this);
	}
	
	public void destroy() {
		plugin.listeners.remove(this);
	}
	
	public abstract int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks);
	
	public abstract void onKill(CPlayer attacker, CPlayer defender, Reason reason, Object ks);
	
	public abstract void onRespawn(CPlayer p, SpawnItems inventory);
	
	public abstract void afterDeath(CPlayer p);
	
	public abstract double getVar(CPlayer p, String name, double def);
	
	public abstract void onInteract(PlayerInteractEvent event);
	
	public abstract void onMove(PlayerMoveEvent event, boolean blockmove);
	
	public abstract void tickfast();
	
	public abstract void tick();
	
}