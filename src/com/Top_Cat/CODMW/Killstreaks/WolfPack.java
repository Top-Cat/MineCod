package com.Top_Cat.CODMW.Killstreaks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.sql.Stat;

public class WolfPack extends useable {
    
    public List<Wolf> wolf = new ArrayList<Wolf>();
    main plugin;
    Random generator = new Random();
    
    public WolfPack(main instance, Player _o, Object[] args) {
    	super(instance, _o, args);
    	
        getOwnerplayer().s.incStat(Stat.DOGS_USED);
        plugin.game.sendMessage(team.BOTH, plugin.d + getOwnerplayer().getTeam().getColour() + getOwnerplayer().nick + plugin.d + "f called in a pack of dogs!");
        for (Player i : plugin.players.keySet()) {
            if (plugin.game.canHit(getOwner(), i)) {
                double theta = (generator.nextFloat() * Math.PI * 2);
                Location l = i.getLocation().clone(); 
                for (int j = 1; j <= 15; j++) {
                    if (i.getLocation().add(j * Math.cos(theta), 0, j * Math.sin(theta)).getBlock().getType() == Material.AIR) {
                        l = i.getLocation().add(j * Math.cos(theta), 0, j * Math.sin(theta));
                    } else {
                        break;
                    }
                }
                Wolf w = (Wolf) plugin.currentWorld.spawnCreature(l, CreatureType.WOLF);
                w.setTarget(i);
                w.setAngry(true);
                wolf.add(w);
            }
        }
    }
    
    @Override
    public void tick() {
    	super.tick();
        if (wolf.size() == 0 || getLifetime() >= 30) {
            destroy();
        }
    }
    
    @Override
    public void teamSwitch() {
    	super.teamSwitch();
    	destroy();
    }
    
    @Override
    public void destroy() {
    	super.destroy();
    	removeAll();
    }
    
    public void remove(Wolf r) {
        wolf.remove(r);
        r.remove();
    }
    
    public void removeAll() {
        for (Wolf i : wolf) {
            i.remove();
        }
        wolf.clear();
    }
    
}