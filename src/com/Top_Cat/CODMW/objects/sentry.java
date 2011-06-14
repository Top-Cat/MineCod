package com.Top_Cat.CODMW.objects;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;

public class sentry {
	
	team t;
	public Block b, bt;
	main plugin;
	Player owner;
	Timer k = new Timer();
	int rot;
	
	public sentry(main instance, Block _c, int r, Player _o) {
		rot = r;
		plugin = instance;
		t = plugin.players.get(_o).getTeam();
		b = _c;
		bt = _c.getRelative(0, 1, 0);
		owner = _o;
		k.schedule(new tick(), 200, 200);
		k.schedule(new destroyTask(), 60000);
		plugin.sentries.add(this);
	}
	
	public class destroyTask extends TimerTask {

		@Override
		public void run() {
			destroy();
			plugin.sentries.remove(this);
		}
		
	}
	
	public void destroy() {
		b.setType(Material.AIR);
		bt.setType(Material.AIR);
		k.cancel();
	}
	
	public class tick extends TimerTask {

		@Override
		public void run() {
			for (player i : plugin.players.values()) {
				Location l1 = i.p.getLocation();
				Location l2 = b.getLocation();
				if (i.getTeam() != t && l1.distance(l2) < 10) {
					int yaw = (int) (Math.toDegrees(Math.atan2(l1.getX() - (l2.getX() + 0.5), (l2.getZ() + 0.5) - l1.getZ())) + 180);
					if (yaw >= 315) { yaw -= 359; }
					int r = (int) Math.ceil((yaw + 45) / 90);
					if (r == rot) {
						CArrow arrow = new CArrow(plugin.currentWorld, owner, bt, plugin, yaw, 0, r, 5);
						arrow.world.addEntity(arrow);
					}
				}
			}
		}
		
	}
}