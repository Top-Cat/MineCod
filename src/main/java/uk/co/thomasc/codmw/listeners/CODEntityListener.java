package uk.co.thomasc.codmw.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CArrow;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.Grenade;
import uk.co.thomasc.codmw.killstreaks.useable.WolfPack;

public class CODEntityListener implements Listener {
	
	private final Main plugin;
	
	public CODEntityListener(Main instance) {
		plugin = instance;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPaintingBreak(PaintingBreakEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPaintingPlace(PaintingPlaceEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Snowball) {
			for (Grenade i : plugin.g) {
				if (i.s_entity == event.getEntity()) {
					i.hit();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		event.setCancelled(true);
		try {
			if (event.getCause() == DamageCause.FALL && event.getDamage() >= 4) {
				if (event.getEntity() instanceof Player) {
					plugin.p((Player) event.getEntity()).incHealth(event.getDamage(), (Player) event.getEntity(), Reason.FALL, null);
				}
				event.setCancelled(false);
			}
			event.setDamage(1);
			if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Arrow) {
				if (event.getEntity() instanceof Wolf) {
					for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
						if (i instanceof WolfPack && ((WolfPack) i).wolf.contains(event.getEntity())) {
							((WolfPack) i).remove((Wolf) event.getEntity());
						}
					}
				} else {
					Reason reason = Reason.BOW;
					Object ks = null;
					if (((CraftArrow) ((EntityDamageByEntityEvent) event).getDamager()).getHandle() instanceof CArrow) {
						reason = ((CArrow) ((CraftArrow) ((EntityDamageByEntityEvent) event).getDamager()).getHandle()).reason;
						ks = ((CArrow) ((CraftArrow) ((EntityDamageByEntityEvent) event).getDamager()).getHandle()).killstreak;
					} else {
						ks = ((EntityDamageByEntityEvent) event).getDamager();
					}
					Player attacker = (Player) ((CraftArrow) ((EntityDamageByEntityEvent) event).getDamager()).getShooter();
					Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
					if (plugin.game.canHit(attacker, defender, false)) {
						if (reason == Reason.BOW) {
							double dif = (((EntityDamageByEntityEvent) event).getDamager().getLocation().getY() - event.getEntity().getLocation().getY()) - 1.5;
							if (dif > 0.1 && dif < 0.5) {
								reason = Reason.HEADSHOT;
							}
							
						}
						plugin.p(defender).incHealth(10, attacker, reason, ks);
						((EntityDamageByEntityEvent) event).getDamager().remove();
						event.setCancelled(false);
					}
				}
			} else if (event instanceof EntityDamageByEntityEvent) {
				if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player && event.getEntity() instanceof Player) {
					Player attacker = (Player) (((EntityDamageByEntityEvent) event).getDamager());
					Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
					Location a = attacker.getLocation();
					Location d = defender.getLocation();
					if (attacker.getItemInHand().getType() == Material.RAW_FISH && !plugin.p(attacker).premium) {
						plugin.currentWorld.strikeLightningEffect(attacker.getLocation());
						plugin.p(attacker).incHealth(20, attacker, Reason.FISH_SMITE, null);
						return;
					}
					if (attacker.getItemInHand().getType() == Material.IRON_SWORD || attacker.getItemInHand().getType() == Material.RAW_FISH) {
						if (plugin.game.canHit(attacker, defender, false)) {
							double dist = Math.sqrt(Math.pow(a.getX() - d.getX(), 2) + Math.pow(a.getZ() - d.getZ(), 2));
							if (dist < plugin.p(attacker).getVar("meleerange", 1.8)) {
								plugin.p(defender).incHealth(20, attacker, attacker.getItemInHand().getType() == Material.IRON_SWORD ? Reason.KNIFE : Reason.FISH, null);
								event.setCancelled(false);
							}
						}
						if (defender.getHealth() < 3) {
							event.setDamage(0);
						}
					}
				} else if (((EntityDamageByEntityEvent) event).getDamager() instanceof Wolf && ((EntityDamageByEntityEvent) event).getEntity() instanceof Player) {
					Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
					for (MineCodListener i : plugin.listeners) {
						if (i instanceof WolfPack) {
							if (((WolfPack) i).wolf.contains(((EntityDamageByEntityEvent) event).getDamager())) {
								if (plugin.game.canHit(i.getOwner(), defender, false)) {
									plugin.p(defender).incHealth(20, i.getOwner(), Reason.DOGS, i);
								}
								((WolfPack) i).remove((Wolf) ((EntityDamageByEntityEvent) event).getDamager());
							}
						}
					}
				} else if (((EntityDamageByEntityEvent) event).getEntity() instanceof Wolf) {
					for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
						if (i instanceof WolfPack && ((WolfPack) i).wolf.contains(event.getEntity())) {
							((WolfPack) i).remove((Wolf) event.getEntity());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (event.getEntity() instanceof Player) {
				((Player) event.getEntity()).teleport(plugin.teamselect);
			}
		}
	}
	
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.NATURAL) {
			event.setCancelled(true);
		}
	}
	
}