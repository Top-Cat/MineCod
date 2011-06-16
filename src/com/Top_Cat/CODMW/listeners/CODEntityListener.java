package com.Top_Cat.CODMW.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingPlaceEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.CArrow;

public class CODEntityListener extends EntityListener {
    
    private final main plugin;
    
    public CODEntityListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onPaintingBreak(PaintingBreakEvent event) {
        if (event.getCause() == RemoveCause.WORLD || !(((PaintingBreakByEntityEvent) event).getRemover() instanceof Player) || !(((Player) ((PaintingBreakByEntityEvent) event).getRemover()).isOp())) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onPaintingPlace(PaintingPlaceEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
        try {
            if (event.getCause() == DamageCause.FALL && event.getDamage() >= 4) {
                if (event.getEntity() instanceof Player) {
                    plugin.p((Player) event.getEntity()).incHealth(1, (Player) event.getEntity(), 0);
                }
                event.setCancelled(false);
            }
            event.setDamage(1);
            if (event instanceof EntityDamageByProjectileEvent) {
                if (event.getEntity() instanceof Wolf) {
                    plugin.wolves.remove(event.getEntity());
                    event.getEntity().remove();
                    return;
                } else {
                    int reason = 2;
                    if (((CraftArrow) ((EntityDamageByProjectileEvent) event).getProjectile()).getHandle() instanceof CArrow) {
                        reason = ((CArrow) ((CraftArrow) ((EntityDamageByProjectileEvent) event).getProjectile()).getHandle()).reason;
                    }
                    Player attacker = (Player) (((EntityDamageByProjectileEvent) event).getDamager());
                    Player defender = (Player) (((EntityDamageByProjectileEvent) event).getEntity());
                    if (plugin.p(attacker).getTeam() != plugin.p(defender).getTeam()) {
                        plugin.p(defender).incHealth(1, attacker, reason);
                        event.setCancelled(false);
                    }
                }
            } else if (event instanceof EntityDamageByEntityEvent) {
                if (((EntityDamageByEntityEvent) event).getDamager() instanceof Player && event.getEntity() instanceof Player) {
                    Player attacker = (Player) (((EntityDamageByEntityEvent) event).getDamager());
                    Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
                    Location a = attacker.getLocation();
                    Location d = defender.getLocation();
                    if (attacker.getItemInHand().getType() == Material.IRON_SWORD) {
                        if (plugin.p(attacker).getTeam() != plugin.p(defender).getTeam()) {
                            double dist = Math.sqrt(Math.pow(a.getX() - d.getX(), 2) + Math.pow(a.getY() - d.getY(), 2) + Math.pow(a.getZ() - d.getZ(), 2));
                            if (dist < 1.5) {
                                plugin.p(defender).incHealth(2, attacker, 1);
                                event.setCancelled(false);
                            }
                        }
                        if (defender.getHealth() < 3) {
                            event.setDamage(0);
                        }
                    }
                } else if (((EntityDamageByEntityEvent) event).getDamager() instanceof Wolf && ((EntityDamageByEntityEvent) event).getEntity() instanceof Player) {
                    Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
                    if (plugin.wolves.containsKey(((EntityDamageByEntityEvent) event).getDamager())) {
	                    if (plugin.p(defender).getTeam() != plugin.wolves.get(((EntityDamageByEntityEvent) event).getDamager()).t) {
		                    plugin.p(defender).incHealth(2, (Player) ((Wolf) ((EntityDamageByEntityEvent) event).getDamager()).getOwner(), 4);
	                    }
	                    plugin.wolves.remove(((EntityDamageByEntityEvent) event).getDamager());
                    }
                    ((Wolf) ((EntityDamageByEntityEvent) event).getDamager()).remove();
                } else if (((EntityDamageByEntityEvent) event).getEntity() instanceof Wolf) {
                    plugin.wolves.remove(event.getEntity());
                    ((EntityDamageByEntityEvent) event).getEntity().remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (event.getEntity() instanceof Player) {
                ((Player) event.getEntity()).teleport(plugin.teamselect);
            }
        }
    }
    
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Wolf)) {
            event.setCancelled(true);
        }
    }
    
}