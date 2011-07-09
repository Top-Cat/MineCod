package com.Top_Cat.CODMW.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingPlaceEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.CArrow;
import com.Top_Cat.CODMW.objects.CWolfPack;
import com.Top_Cat.CODMW.objects.ownable;

public class CODEntityListener extends EntityListener {
    
    private final main plugin;
    
    public CODEntityListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onPaintingBreak(PaintingBreakEvent event) {
        if (event.getCause() == RemoveCause.WORLD || !(((PaintingBreakByEntityEvent) event).getRemover() instanceof Player)) {
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
                    plugin.p((Player) event.getEntity()).incHealth(1, (Player) event.getEntity(), 0, null);
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
                    ownable ks = null;
                    if (((CraftArrow) ((EntityDamageByProjectileEvent) event).getProjectile()).getHandle() instanceof CArrow) {
                        reason = ((CArrow) ((CraftArrow) ((EntityDamageByProjectileEvent) event).getProjectile()).getHandle()).reason;
                        ks = ((CArrow) ((CraftArrow) ((EntityDamageByProjectileEvent) event).getProjectile()).getHandle()).killstreak;
                    }
                    Player attacker = (Player) (((EntityDamageByProjectileEvent) event).getDamager());
                    Player defender = (Player) (((EntityDamageByProjectileEvent) event).getEntity());
                    if (plugin.game.canHit(attacker, defender)) {
                        if (reason == 2) {
                            double dif = (((EntityDamageByProjectileEvent) event).getProjectile().getLocation().getY() - event.getEntity().getLocation().getY()) - 1.5;
                            if (dif > 0.1 && dif < 0.5) {
                                reason = 7;
                            }
                            
                        }
                        plugin.p(defender).incHealth(1, attacker, reason, ks);
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
                        if (plugin.game.canHit(attacker, defender)) {
                            double dist = Math.sqrt(Math.pow(a.getX() - d.getX(), 2) + Math.pow(a.getZ() - d.getZ(), 2));
                            if (dist < 1.8) {
                                plugin.p(defender).incHealth(2, attacker, 1, null);
                                event.setCancelled(false);
                            }
                        }
                        if (defender.getHealth() < 3) {
                            event.setDamage(0);
                        }
                    }
                } else if (((EntityDamageByEntityEvent) event).getDamager() instanceof Wolf && ((EntityDamageByEntityEvent) event).getEntity() instanceof Player) {
                    Player defender = (Player) (((EntityDamageByEntityEvent) event).getEntity());
                    for (CWolfPack i : plugin.wolves.values()) {
                    	if (i.wolf.contains(((EntityDamageByEntityEvent) event).getDamager())) {
                    		if (plugin.game.canHit(defender, (Wolf) ((EntityDamageByEntityEvent) event).getDamager())) {
                    			plugin.p(defender).incHealth(2, i.getOwner(), 4, i);
                    		}
                    		i.remove((Wolf) ((EntityDamageByEntityEvent) event).getDamager());
                    	}
                    }
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
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        event.setCancelled(true);
    }
    
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.NATURAL) {
            event.setCancelled(true);
        }
    }
    
}