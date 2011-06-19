package com.Top_Cat.CODMW.gamemodes;

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

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.chopper;
import com.Top_Cat.CODMW.objects.claymore;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.sentry;
import com.Top_Cat.CODMW.sql.Stat;

public class gamemode {
	
	main plugin;
	ArrayList<Location> spawns1 = new ArrayList<Location>();
    ArrayList<Location> spawns2 = new ArrayList<Location>();
    public ArrayList<Location> spawns3 = new ArrayList<Location>();
    HashMap<Arrow, Location> ploc = new HashMap<Arrow, Location>();
	ArrayList<String> lossmesssages = new ArrayList<String>();
    ArrayList<String> winmesssages = new ArrayList<String>();
    public Random generator = new Random();
    Timer t = new Timer();
    Location d1, d2, d3, d4;
    boolean dl = false;
	
	public gamemode(main instance) {
		plugin = instance;
		plugin.activeGame = true;
		
		setup();
		
		lossmesssages.add("They have won the battle but not the war!");
        lossmesssages.add("We have made a lot of mistakes you are going to regret");
        lossmesssages.add("God. You're such Gigs");
        winmesssages.add("Good job seals");
        winmesssages.add("You will receive cake for your victory here!");
        
        sendMessage(team.BOTH, plugin.d + "9Game starting in 5 seconds!");
        plugin.r.countdown(this);
	}
	
	public void sendMessage(team t, String s, Player exclude) {
    	for (Player p : plugin.players.keySet()) {
            if ((t == team.BOTH || t == plugin.p(p).getTeam()) && p != exclude) {
                p.sendMessage(s);
            }
        }
    }
    
    public void sendMessage(team t, String s) {
        sendMessage(t, s, null);
    }
    
    public class tick extends TimerTask {
		@Override
		public void run() { tick(); }
    }
    
    public class tickfast extends TimerTask {
		@Override
		public void run() { tickfast(); }
    }
    
    public boolean spawnCheck(Location i) {
    	return ((d1 == null || i.distance(d1) > 7) && (d2 == null || i.distance(d2) > 7) && (d3 == null || i.distance(d3) > 7) && (d4 == null || i.distance(d4) > 7));
    }
	
    // BELOW ARE INTERFACES
    
	public void setup() {};
	
	public void spawnPlayer(Player p, boolean start) {
		player _p = plugin.p(p);
        _p.clearinv();
        _p.setinv();
        spawnTele(_p, p, start);
        _p.dead = false;
	}
	
	public Location spawnTele(player _p, Player p, boolean start) { return null; };
	
	public void startGame() {
		sendMessage(team.BOTH, plugin.d + "9Game starting now!");
        
        for (Player p : plugin.players.keySet()) {
            plugin.p(p).resetScore();
            spawnPlayer(p, true);
        }
		t.schedule(new tick(), 2000, 2000);
        t.schedule(new tickfast(), 200, 200);
	}
	
	public void onWin(team winners, player lastkill, player lastdeath) {
		team lost = winners == team.DIAMOND ? team.GOLD : team.DIAMOND;
		sendMessage(winners, winmesssages.get(generator.nextInt(winmesssages.size())));
        sendMessage(lost, lossmesssages.get(generator.nextInt(lossmesssages.size())));
        
        destroy();
        plugin.loadmap();
        plugin.scheduleGame();
        
        for (player i : plugin.players.values()) {
            i.dead = false;
            i.clearinv();
            switch(i.getTeam()) {
                case GOLD: i.p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1)); break;
                case DIAMOND: i.p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1)); break;
            }
            if (i.getTeam() == winners) {
                i.s.incStat(Stat.WINS);
                i.addPoints(10);
            } else {
                i.s.incStat(Stat.LOSSES);
                i.addPoints(-5);
            }
        }
        
        lastdeath.p.getInventory().setHelmet(new ItemStack(Material.WOOD, 1));
        lastdeath.s.incStat(Stat.LAST_DEATH);
        lastkill.s.incStat(Stat.LAST_KILL);
        
        sendMessage(team.BOTH, plugin.d + "9Game ended, game will resume on '" + plugin.currentMap.name + "' in 60 seconds");
	}
	
	public void onKill(player attacker, player defender, Location l) {
		if (dl) {
            d1 = l;
            d3 = attacker.p.getLocation();
        } else {
            d2 = l;
            d4 = attacker.p.getLocation();
        }
        dl = !dl;	
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
	
	public void tick() {
        for (player p : plugin.players.values()) {
            if (p.vtime < new Date().getTime()) {
                p.p.getInventory().clear(38);
                p.p.getInventory().clear(37);
                p.p.getInventory().clear(36);
                p.inv = false;
            }
            if (p.dead) {
                if (p.todrop > 0) {
                    plugin.currentWorld.dropItem(p.dropl, new ItemStack(Material.FEATHER, p.todrop));
                    p.todrop = 0;
                }
                spawnPlayer(p.p, false);
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
            if (plugin.wolves.get(i).expire < new Date().getTime()) {
                i.remove();
                r.add(i);
            }
        }
        for (Wolf j : r) {
            plugin.wolves.remove(j);
        }
	}
	
	public void tickfast() {
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
                        
                        for (chopper j : plugin.choppers) {
                            if (j.l.distance(l) < 1.5) {
                                j.arrowhit();
                            }
                        }
                        
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
	
	public void printScore(Player p, team t) {};
	
}