package uk.co.thomasc.codmw.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.sql.Achievement;
import uk.co.thomasc.codmw.util.StatUtil;

public class Grenade extends Ownable {
	
	public Entity s_entity;
	public Main plugin;
	int starttick = 0;
	
	public Grenade(Main instance, Player owner, Location l) {
		plugin = instance;
		setOwner(owner, plugin.p(owner));
		
		s_entity = getOwner().throwSnowball();
		((CraftSnowball) s_entity).getHandle().setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		s_entity.setVelocity(s_entity.getVelocity().multiply(0.5));
		
		starttick = plugin.game.time;
		
		plugin.g.add(this);
	}
	
	public Grenade(Main instance, Player owner) {
		this(instance, owner, owner.getEyeLocation());
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
			int kills = 0;
			for (Entity i : s_entity.getNearbyEntities(7, 7, 7)) {
				if (i instanceof Player && (plugin.game.canHit(getOwner(), (Player) i, false) || i == getOwner())) {
					CPlayer p = plugin.p((Player) i);
					if (p != null) {
						int cdf = 0;
						if (p.getVar("grenadedmg", 0) == 1) {
							cdf = (int) (StatUtil.erfc((i.getLocation().distance(s_entity.getLocation()) / 2.5) - 2) * 11);
						} else {
							cdf = (int) (StatUtil.erfc((i.getLocation().distance(s_entity.getLocation()) / 2) - 1.5) * 11);
						}
						
						//int exp = (int) (Math.pow(1.4, -i.getLocation().distance(s_entity.getLocation())) * 22);
						if (p.incHealth(cdf, getOwner(), Reason.GRENADE, null) && p != getOwnerplayer()) { kills++; }
					}
				}
			}
			if (kills >= 2) {
				getOwnerplayer().s.awardAchievement(Achievement.COLLATERAL_FRAG);
			}
		}
	}
	
}