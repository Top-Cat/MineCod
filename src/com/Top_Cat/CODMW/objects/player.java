package com.Top_Cat.CODMW.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.Top_Cat.CODMW.game;
import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.sql.Stat;
import com.Top_Cat.CODMW.sql.stats;

public class player {
    
    public int kill, streak, arrow, knife, death;
    public streaks last = new streaks();
    private final main plugin;
    public Player p;
    public String nick;
    public int dbid;
    team t;
    public stats s;
    public int h = 2;
    public long htime = 0;
    public long stime = 0;
    public long vtime = 0;
    public int todrop = 0;
    public boolean inv = false;
    public Location dropl;
    
    public boolean dead = false;
    
    public player(main instance, Player _p, team _t) {
        plugin = instance;
        p = _p;
        t = _t;
        
        ResultSet r = plugin.sql.query("SELECT * FROM cod_players WHERE username = '" + plugin.p(p).nick + "'");
        try {
			r.next();
	        nick = r.getString("nick");
	        dbid = r.getInt("Id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        s = new stats(plugin, this);
        s.incStat(Stat.LOGIN);
        
        switch(t) {
            case GOLD:
                p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
                plugin.gold++;
                break;
            case DIAMOND:
                p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1));
                plugin.diam++;
                break;
        }
        plugin.tot++;
        plugin.players.put(_p, this);
        
        if (plugin.activeGame == true && plugin.game.initspawn == true) {
            dead = true;
        }
        
        if (plugin.tot >= plugin.minplayers && plugin.activeGame == false) {
            plugin.game = new game(plugin);
        }
        
        p.teleport(plugin.prespawn);
    }
    
    public void destroy() {
        switch(t) {
            case GOLD:
                plugin.gold--;
                break;
            case DIAMOND:
                plugin.diam--;
                break;
        }
        plugin.tot--;
        plugin.setDoors();
    }
    
    public team getTeam() {
        return t;
    }
    
    public void setTeam(team _t) {
        t = _t;
    }
    
    public void resetScore() {
        kill = 0;
        arrow = 0;
        knife = 0;
        death = 0;
        streak = 0;
    }
    
    public void addStreak() {
        streak++;
        //System.out.println(plugin.p(p).nick + " got a " + streak + " kill streak!");
        switch (streak) {
            case 3: giveItem(2, new ItemStack(Material.WALL_SIGN, 2)); break;
            case 5: giveItem(3, new ItemStack(Material.APPLE, 1)); break;
            case 7: giveItem(4, new ItemStack(Material.BONE, 1)); break;
            case 9: giveItem(5, new ItemStack(Material.DISPENSER, 1)); break;
            case 11: giveItem(6, new ItemStack(Material.DIAMOND, 1)); break;
        }
    }
    
    public void giveItem(int pslot, ItemStack s) {
        if (s.getAmount() > 0) {
            PlayerInventory i = p.getInventory();
            if (i.contains(s.getType()) || i.getItem(pslot) != null) {
                i.addItem(s);
            } else {
                i.setItem(pslot, s);
            }
        }
        p.updateInventory();
    }
    
    public void incHealth(int _h, Player attacker, int reason) {
        if (_h < 0 || inv == false) {
            h -= _h;
            if (h > 2) { h = 2; }
            if (_h > 0) {
                htime = new Date().getTime() + 10000;
                stime = new Date().getTime() + 5000;
            }
            if (h <= 0) {
                if (plugin.p(attacker) != this) {
                	plugin.p(attacker).s.incStat(Stat.KILLS);
                    plugin.p(attacker).kill++;
                    if (reason <= 3) {
                        plugin.p(attacker).addStreak();
                    }
                    
                    switch (reason) {
                        case 1: plugin.p(attacker).knife++; break;
                        case 2: plugin.p(attacker).arrow++; break;
                    }
                } else {
                    kill--;
                }
                plugin.p(attacker).s.incStat(Stat.DEATHS);
                death++;
                streak = 0;
                
                h = 2;
                
                int ammo = 0;
                last = new streaks();
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null) {
                        if (i.getType() == Material.FEATHER) {
                            ammo += i.getAmount();
                        } else if (i.getType() == Material.WALL_SIGN) {
                            last.clays += i.getAmount();
                        } else if (i.getType() == Material.APPLE) {
                            last.apples += i.getAmount();
                        } else if (i.getType() == Material.BONE) {
                            last.dogs += i.getAmount();
                        } else if (i.getType() == Material.DISPENSER) {
                            last.sentry += i.getAmount();
                        } else if (i.getType() == Material.DIAMOND) {
                            last.chop += i.getAmount();
                        }
                    }
                }
                ammo = (int) (ammo / 15);
                
                switch (reason) {
                    case 0: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(p).nick + " fell to his death. LOL!"); break;
                    case 1: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + " knifed " + plugin.p(p).nick); break;
                    case 2: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + " shot " + plugin.p(p).nick); break;
                    case 3: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + " claymored " + plugin.p(p).nick); break;
                    case 4: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + "'s dogs mauled " + plugin.p(p).nick); break;
                    case 5: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + "'s sentry shot " + plugin.p(p).nick); break;
                    case 6: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(attacker).nick + "'s chopper battered " + plugin.p(p).nick); break;
                }
                clearinv();
                todrop += ammo;
                dropl = p.getLocation();
                
                p.teleport(plugin.prespawn);
                dead = true;
                
                plugin.game.death(this, p.getLocation());
            }
        } else {
            h = 2;
        }
        p.setHealth(h * 10);
    }
    
    public void setinv() {
        switch(t) {
            case GOLD: p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1)); break;
            case DIAMOND: p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1)); break;
        }
        p.getInventory().setItem(0, new ItemStack(Material.BOW, 1));
        p.getInventory().setItem(1, new ItemStack(Material.IRON_SWORD, 1));
        p.getInventory().setItem(8, new ItemStack(Material.ARROW, 15));
        p.getInventory().setItem(7, new ItemStack(Material.FEATHER, 75));
    }
    
    public void clearinv() {
        plugin.clearinv(p);
    }
    
}