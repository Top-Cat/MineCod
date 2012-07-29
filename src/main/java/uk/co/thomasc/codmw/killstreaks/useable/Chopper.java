package uk.co.thomasc.codmw.killstreaks.useable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.objects.CArrow;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Achievement;
import uk.co.thomasc.codmw.sql.Stat;

public class Chopper extends Useable {

	Location p;
	public Location l;
	double mx = 0, mz = 0, tx = 0, tz = 0;
	int tick = 0;
	boolean started = false;
	int health = 5;

	public Chopper(Main instance, Player _o, Object[] args) {
		super(instance, _o, args);
		plugin.game.sendMessage(Team.BOTH, plugin.p(getOwner()).getTeam().getColour() + plugin.p(getOwner()).nick + ChatColor.WHITE + " called in a chopper!");
		getOwnerplayer().s.incStat(Stat.CHOPPERS_USED);
		l = avgEnemies();
		moveto();
	}

	@Override
	public void destroy() {
		super.destroy();
		l.getBlock().setType(Material.AIR);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getLifetime() >= 60 || health <= 0) {
			destroy();
		}
	}
	
	@Override
	public void tickfast() {
		super.tickfast();
		for (Entity i : plugin.currentWorld.getEntities()) {
			if (i instanceof Arrow && !(((CraftArrow) i).getHandle() instanceof CArrow)) {
				Location l = i.getLocation();
				if (plugin.game.ploc.containsKey(i)) {
					if (l.distance(plugin.game.ploc.get(i)) < 0.1) {
						if (l.distance(l) < 1.5) {
							health--;
							if (health <= 0) {
								plugin.p((Player) ((Arrow) i).getShooter()).s.awardAchievement(Achievement.AERIAL_AIM);
								destroy();
							}
						}
					}
				}
			}
		}
		
		tick++;
		if (tick <= 10) {
			try {
				if (started) {
					l.getBlock().setType(Material.AIR);
				}
				started = true;
				tx += mx;
				tz += mz;
				l = p.clone().add(tx, 0, tz);
				l.setY(l.getWorld().getHighestBlockYAt(l) + 7);
				switch (plugin.p(getOwner()).getTeam()) {
					case DIAMOND: l.getBlock().setType(Material.DIAMOND_BLOCK); break;
					case GOLD: l.getBlock().setType(Material.GOLD_BLOCK); break;
					case BOTH: l.getBlock().setType(Material.OBSIDIAN); break;
				}
			} catch (Exception e) {
				System.out.println("Error moving chopper");
			}
		} else if (tick > 18) {
			tick = 0;
			moveto();
		} else {
			for (CPlayer i : plugin.players.values()) {
				Location l1 = i.p.getLocation();
				if (plugin.game.canHit(getOwner(), i.p, true)) {
					int yaw = (int) (Math.toDegrees(Math.atan2(l1.getX() - (l.getX() + 0.5), (l.getZ() + 0.5) - l1.getZ())) + 180);
					if (yaw >= 315) { yaw -= 359; }
					int r = (int) Math.ceil((yaw + 45) / 90);
					
					Location l2 = l1.clone();
					l2.setY(l.getY());
					int pitch = (int) ((Math.toDegrees(Math.atan2(l2.distance(l), (l.getY() + 0.5) - l1.getY())) - 85) * 1.1);
					
					CArrow arrow = new CArrow(plugin.currentWorld, getOwner(), l.getBlock(), plugin, yaw, -pitch, r, Reason.CHOPPER, this);
					arrow.world.addEntity(arrow);
				}
			}
			
		}
	}

	public Location avgEnemies() {
		int ax = 0;
		int az = 0;
		int a = 0;
		for (Player i : plugin.players.keySet()) {
			if (plugin.game.canHit(getOwner(), i, true) && plugin.p(i).dead == false) {
				ax = (int) (((ax * a) + i.getLocation().getX()) / (a + 1));
				az = (int) (((az * a) + i.getLocation().getZ()) / (a + 1));
				a++;
			}
		}
		Location out = new Location(plugin.currentWorld, (ax - 3) + plugin.game.generator.nextInt(7), 0, (az - 3) + plugin.game.generator.nextInt(7));
		out.setY(out.getWorld().getHighestBlockYAt(out) + 7);
		return out;
	}
	
	public void moveto() {
		Location l2 = avgEnemies();
		mx = (l2.getX() - l.getX()) / 10;
		mz = (l2.getZ() - l.getZ()) / 10;
		tx = 0;
		tz = 0;
		p = l;
	}

}