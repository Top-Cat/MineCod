package com.Top_Cat.CODMW.perks;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.MineCodListener;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.spawnitems;

public class perk extends MineCodListener {

    public perk(main instance, Player owner) {
        super(instance, owner);
    }
    
    public void checkIsPerk() {
        ArrayList<Tiers> r = new ArrayList<Tiers>();
        for (Tiers i : getOwnerplayer().perks.keySet()) {
            if (i.instanceOf(getOwnerplayer().perks.get(i))) {
                r.add(i);
            }
        }
        for (Tiers i : r) {
            getOwnerplayer().perks.remove(i);
        }
        if (!getOwnerplayer().perks.containsValue(this)) {
            plugin.listeners.remove(this);
        }
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