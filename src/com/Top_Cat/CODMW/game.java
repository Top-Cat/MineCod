package com.Top_Cat.CODMW;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.objects.chopper;
import com.Top_Cat.CODMW.objects.claymore;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.sentry;

public class game {

	main plugin;
	ArrayList<Location> spawns1 = new ArrayList<Location>();
	ArrayList<Location> spawns2 = new ArrayList<Location>();
	public ArrayList<Location> spawns3 = new ArrayList<Location>();
	public Random generator = new Random();
	Timer t = new Timer();
	HashMap<Arrow, Location> ploc = new HashMap<Arrow, Location>();
	Location d1, d2;
	boolean dl = false;
	ArrayList<String> lossmesssages = new ArrayList<String>();
	ArrayList<String> winmesssages = new ArrayList<String>();
	public boolean initspawn = false;
	
	public void death(player p, Location l) {
		if (dl) {
			d1 = l;
		} else {
			d2 = l;
		}
		dl = !dl;
		
		int td = 0, tg = 0;
    	for (player i : plugin.players.values()) {
    		if (i.getTeam() == team.DIAMOND) {
    			td += i.kill;
    		} else {
    			tg += i.kill;
    		}
    	}
    	
    	if (td >= 50) {
    		sendMessage(team.DIAMOND, winmesssages.get(generator.nextInt(winmesssages.size())));
    		sendMessage(team.GOLD, lossmesssages.get(generator.nextInt(lossmesssages.size())));
    	} else if (tg >= 50) {
    		sendMessage(team.GOLD, winmesssages.get(generator.nextInt(winmesssages.size())));
    		sendMessage(team.DIAMOND, lossmesssages.get(generator.nextInt(lossmesssages.size())));
    	} else {
    		return;
    	}
    	destroy();
    	plugin.loadmap();
    	plugin.scheduleGame();
    	
    	for (player i : plugin.players.values()) {
    		i.resetScore();
    		p.dead = false;
    		i.clearinv();
    		switch(i.getTeam()) {
				case GOLD: i.p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1)); break;
				case DIAMOND: i.p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1)); break;
    		}
    	}
    	
    	p.p.getInventory().setHelmet(new ItemStack(Material.WOOD, 1));
    	
    	sendMessage(team.BOTH, plugin.d + "9Game ended, game will resume on '" + plugin.currentMap.name + "' in 60 seconds");
	}
	
	public void destroy() {
		t.cancel();
		for (claymore i : plugin.clays) {
			i.b.setType(Material.AIR);
		}
		for (Wolf i : plugin.wolves.keySet()) {
			i.remove();
		}
		for (sentry i : plugin.sentries) {
			i.destroy();
		}
		for (chopper i : plugin.choppers) {
			i.destroy();
		}
		plugin.clays.clear();
		plugin.wolves.clear();
		plugin.sentries.clear();
		plugin.activeGame = false;
	}
	
	public class tick extends TimerTask {

		@Override
		public void run() {
			for (player p : plugin.players.values()) {
				if (p.vtime < new Date().getTime()) {
					p.p.getInventory().clear(38);
					p.p.getInventory().clear(37);
					p.p.getInventory().clear(36);
					p.inv = false;
				}
				if (p.dead && initspawn) {
					if (p.todrop > 0) {
						plugin.currentWorld.dropItem(p.dropl, new ItemStack(Material.FEATHER, p.todrop));
						p.todrop = 0;
					}
					spawn(p.p, false);
					p.giveItem(2, new ItemStack(Material.WALL_SIGN, p.last.clays));
					p.giveItem(3, new ItemStack(Material.APPLE, p.last.apples));
					p.giveItem(4, new ItemStack(Material.BONE, p.last.dogs));
					p.giveItem(5, new ItemStack(Material.DISPENSER, p.last.sentry));
					p.giveItem(6, new ItemStack(Material.DIAMOND, p.last.chop));
				}
				if (p.htime < new Date().getTime()) {
					p.incHealth(-1, null, 0);
				}
				p.p.setHealth(p.h * 10);
			}
			List<Wolf> r = new ArrayList<Wolf>();
			for (Wolf i : plugin.wolves.keySet()) {
				if (plugin.wolves.get(i) < new Date().getTime()) {
					i.remove();
					r.add(i);
				}
			}
			for (Wolf j : r) {
				plugin.wolves.remove(j);
			}
		}
		
	}
	
	public class tickfast extends TimerTask {

		@Override
		public void run() {
			List<claymore> r = new ArrayList<claymore>();
			for (Entity i : plugin.currentWorld.getEntities()) {
				if (i instanceof Arrow) {
					Location l = i.getLocation();
					for (claymore j : plugin.clays) {
						if (j.b.getLocation().add(0.5, 0, 0.5).distance(l) < 1) {
							j.kill();
							r.add(j);
						}
					}
					if (ploc.containsKey(i)) {
						if (l.distance(ploc.get(i)) < 0.1) {
							i.remove();
						}
					}
					ploc.put((Arrow) i, l);
				}
			}
			for (claymore i : plugin.clays) {
				if (i.exploded && i.explode < new Date().getTime()) {
					i.kill();
					r.add(i);
				}
			}
			plugin.clays.removeAll(r);
		}
		
	}
	
	public game(main instance) {
		plugin = instance;
		plugin.activeGame = true;
		
		spawns1 = plugin.currentMap.getSpawns(0);
		spawns2 = plugin.currentMap.getSpawns(1);
		spawns3 = plugin.currentMap.getSpawns(2);
		
		lossmesssages.add("They have won the battle but not the war!");
		lossmesssages.add("We have made a lot of mistakes you are going to regret");
		lossmesssages.add("God. You're such Gigs");
		winmesssages.add("Good job seals");
		winmesssages.add("You will receive cake for your victory here!");
		
		sendMessage(team.BOTH, plugin.d + "9Game starting in 5 seconds!");
		plugin.r.countdown(this);
		
		t.schedule(new tick(), 2000, 2000);
		t.schedule(new tickfast(), 200, 200);
	}
	
	public void startGame() {
		sendMessage(team.BOTH, plugin.d + "9Game starting now!");
		for (Player p : plugin.players.keySet()) {
			spawn(p, true);
		}
		initspawn = true;
	}
	
	public void spawn(Player p, boolean gamestart) {
		player _p = plugin.players.get(p);
		_p.clearinv();
		_p.setinv();
		spawntele(_p, p, gamestart);
		_p.dead = false;
	}
	
	public Location spawntele(player _p, Player p, boolean gamestart) {
		Location g = null;
		ArrayList<Player> alivePlayers = new ArrayList<Player>();
		for (player i : plugin.players.values()) {
			if (i.dead == false && i.getTeam() == _p.getTeam() && i.stime < new Date().getTime() && (d1 == null || i.p.getLocation().distance(d1) > 5) && (d2 == null || i.p.getLocation().distance(d2) > 5)) {
				alivePlayers.add(i.p);
			}
		}
		if (alivePlayers.size() <= 1 || gamestart) {
			if (gamestart) {
				switch (_p.getTeam()) {
					case DIAMOND: g = spawns1.get(generator.nextInt(spawns1.size())); break;
					case GOLD: g = spawns2.get(generator.nextInt(spawns2.size())); break;
				}
			} else {
				g = spawns3.get(generator.nextInt(spawns3.size()));
			}
		} else {
			g = alivePlayers.get(generator.nextInt(alivePlayers.size())).getLocation();
		}
		_p.stime = new Date().getTime() + 5000;
		p.teleport(g);
		return g;
	}
	
	public void sendMessage(team t, String s) {
		for (Player p : plugin.players.keySet()) {
			if (t == team.BOTH || t == plugin.players.get(p).getTeam()) {
				p.sendMessage(s);
			}
		}
	}
	
}