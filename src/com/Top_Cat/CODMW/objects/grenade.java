package com.Top_Cat.CODMW.objects;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;

public class grenade extends ownable {
	
	public Entity s_entity;
	public main plugin;
    int starttick = 0;
    
	public grenade(main instance, Player owner) {
		plugin = instance;
		setOwner(owner, plugin.p(owner));
		
    	s_entity = getOwner().throwSnowball();
    	s_entity.setVelocity(s_entity.getVelocity().multiply(0.5));
    	
    	starttick = plugin.game.time;
    	
    	plugin.g.add(this);
	}
	
    public int getLifetime() {
        return plugin.game.time - starttick;
    }
	
	public void hit() {
		s_entity.remove();
		s_entity = plugin.currentWorld.dropItem(s_entity.getLocation(), new ItemStack(Material.SNOW_BALL, 1));
	}
	
	public void explode() {
		if (getLifetime() >= 5) {
			s_entity.remove();
			plugin.g.remove(this);
			plugin.currentWorld.createExplosion(s_entity.getLocation(), 0);
			for (Entity i : s_entity.getNearbyEntities(5, 5, 5)) {
	    		if (i instanceof Player && (plugin.game.canHit(getOwner(), (Player) i) || i == getOwner())) {
	    			player p = plugin.p((Player) i);
	    			if (p != null) {
	    				p.incHealth((int) (Math.pow(1.5, -i.getLocation().distance(s_entity.getLocation())) * 16), getOwner(), Reason.GRENADE, null);
	    			}
	    		}
	    	}
		}
	}
	
}