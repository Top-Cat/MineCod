package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.MineCodListener;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.spawnitems;

public class killstreak extends MineCodListener {
    
    int starttick = 0;
    
    public killstreak(main instance, Player owner, Object[] args) {
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
    public void onKill(player attacker, player defender, Reason reason, Object ks) {
        
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
    public void afterDeath(player p) {
        
    }

    @Override
    public void onRespawn(player p, spawnitems inventory) {
        
    }

    @Override
    public double getVar(player p, String name, double def) {
        return def;
    }

}