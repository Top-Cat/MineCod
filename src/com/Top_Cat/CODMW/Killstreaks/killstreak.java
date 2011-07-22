package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.ownable;
import com.Top_Cat.CODMW.objects.player;

public class killstreak extends ownable {
    
    final main plugin;
    int starttick = 0;
    
    public killstreak(main instance, Player owner, Object[] args) {
        plugin = instance;
        setOwner(owner, plugin.p(owner));
        starttick = plugin.game.time;
        getOwnerplayer().addPoints(3);
    }
    
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        return damage;
    }
    
    public void onKill(player attacker, player defender, Reason reason, Object ks) {
        
    }
    
    public int getLifetime() {
        return plugin.game.time - starttick;
    }
    
    public void onInteract(PlayerInteractEvent event) {
        
    }
    
    public void onMove(PlayerMoveEvent event) {
        
    }
    
    public void tickfast() {
        
    }
    
    public void tick() {
        
    }

    public void teamSwitch() {
        
    }
    
    public void destroy() {
        plugin.ks.remove(this);
    }

}