package com.Top_Cat.CODMW.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;

public abstract class MineCodListener extends ownable {
    
    final public main plugin;
    
    public MineCodListener(main instance, Player owner) {
        plugin = instance;
        setOwner(owner, plugin.p(owner));
        plugin.listeners.add(this);
    }
    
    public void destroy() {
        plugin.listeners.remove(this);
    }
    
    public abstract int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks);
    
    public abstract void onKill(player attacker, player defender, Reason reason, Object ks);
    
    public abstract void onRespawn(player p, spawnitems inventory);
    
    public abstract void afterDeath(player p);
    
    public abstract double getVar(player p, String name, double def);
    
    public abstract void onInteract(PlayerInteractEvent event);
    
    public abstract void onMove(PlayerMoveEvent event, boolean blockmove);
    
    public abstract void tickfast();
    
    public abstract void tick();
    
}