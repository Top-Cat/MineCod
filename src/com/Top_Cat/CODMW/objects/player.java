package com.Top_Cat.CODMW.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.gamemodes.TDM;
import com.Top_Cat.CODMW.sql.Achievement;
import com.Top_Cat.CODMW.sql.Stat;
import com.Top_Cat.CODMW.sql.stats;

public class player {
    
    public int kill, streak, arrow, knife, death, assists, points;
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
    Player assist;
    
    public boolean dead = false;
    
    public player(main instance, Player _p, team _t) {
        plugin = instance;
        p = _p;
        t = _t;
        
        ResultSet r = plugin.sql.query("SELECT * FROM cod_players WHERE username = '" + _p.getDisplayName() + "'");
        try {
            r.next();
        nick = r.getString("nick");
        dbid = r.getInt("Id");
        } catch (SQLException e) {
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
        
        if (plugin.activeGame == true) {
            dead = true;
        }
        
        if (plugin.tot >= plugin.minplayers && plugin.activeGame == false) {
            plugin.game = new TDM(plugin);
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
        s.destroy();
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
        assists = 0;
        points = 0;
    }
    
    public void addPoints(int c) {
    points += c;
    s.incStat(Stat.POINTS, c);
    s.maxStat(Stat.MAX_POINTS, points);
    }
    
    public void addStreak() {
        streak++;
        s.maxStat(Stat.MAX_STREAK, streak);
        switch (streak) {
            case 3: giveItem(2, new ItemStack(Material.WALL_SIGN, 2)); s.incStat(Stat.CLAYMORES_ACHIEVED); break;
            case 5: giveItem(3, new ItemStack(Material.APPLE, 1)); s.incStat(Stat.APPLES_ACHIEVED); break;
            case 7: giveItem(4, new ItemStack(Material.BONE, 1)); s.incStat(Stat.DOGS_ACHIEVED); break;
            case 9: giveItem(5, new ItemStack(Material.DISPENSER, 1)); s.incStat(Stat.SENTRIES_ACHIEVED); break;
            case 11: giveItem(6, new ItemStack(Material.DIAMOND, 1)); s.incStat(Stat.CHOPPERS_ACHIEVED); break;
        }
    }
    
    public void giveItem(int pslot, ItemStack s) {
        if (s.getAmount() > 0) {
            PlayerInventory i = p.getInventory();
            if (i.contains(s.getType()) || (i.getItem(pslot) != null && i.getItem(pslot).getType() != s.getType() && i.getItem(pslot).getType() != Material.AIR )) {
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
            	player a = plugin.p(attacker);
                if (a != this) {
                    a.s.incStat(Stat.KILLS);
                    a.addPoints(5);
                    a.kill++;
                    a.s.maxStat(Stat.MAX_KILLS, plugin.p(attacker).kill);
                    if (a.inv) {
                    	a.s.incStat(Stat.INVULNERABLE_KILLS);
                    }
                    if (p.getDisplayName().equalsIgnoreCase("Gigthank")) {
                    	a.s.awardAchievement(Achievement.KILL_GIG);
                    } else if (p.getDisplayName().equalsIgnoreCase("Notch")) {
                    	a.s.awardAchievement(Achievement.KILL_NOTCH);
                    }
                    if (reason <= 3 || reason == 7) {
                        a.addStreak();
                    }
                    
                    switch (reason) {
                        case 1: a.knife++; break;
                        case 2: a.arrow++; break;
                    }
                } else {
                    kill--;
                }
                String assist_txt = "";
                if (assist != null && assist != attacker) {
                    assist_txt = plugin.d + "c (Assist: " + plugin.d + plugin.p(assist).getTeam().getColour() + plugin.p(assist).nick + plugin.d + "c)";
                    plugin.p(assist).assists++;
                    plugin.p(assist).s.incStat(Stat.ASSISTS);
                    plugin.p(assist).addPoints(2);
                }
                plugin.p(attacker).s.incStat(Stat.DEATHS);
                death++;
                addPoints(-2);
                s.maxStat(Stat.MAX_DEATHS, death);
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
                
                String desc = "";
                switch (reason) {
                    case 0: plugin.game.sendMessage(team.BOTH, plugin.d + "c" + plugin.p(p).nick + " fell to his death. LOL!" + assist_txt); plugin.p(p).s.incStat(Stat.FALL_DEATHS); break;
                    case 1: desc = " kinfed"; plugin.p(p).s.incStat(Stat.KNIFE_DEATHS); plugin.p(attacker).s.incStat(Stat.KNIFE_KILLS); break;
                    case 2: desc = " shot"; plugin.p(p).s.incStat(Stat.BOW_DEATHS); plugin.p(attacker).s.incStat(Stat.BOW_KILLS); break;
                    case 3: desc = " claymored"; plugin.p(p).s.incStat(Stat.CLAYMORE_DEATHS); plugin.p(attacker).s.incStat(Stat.CLAYMORE_KILLS); break;
                    case 4: desc = "'s dogs mauled"; plugin.p(p).s.incStat(Stat.DOG_DEATHS); plugin.p(attacker).s.incStat(Stat.DOG_KILLS); break;
                    case 5: desc = "'s sentry shot"; plugin.p(p).s.incStat(Stat.SENTRY_DEATHS); plugin.p(attacker).s.incStat(Stat.SENTRY_KILLS); break;
                    case 6: desc = "'s chopper battered"; plugin.p(p).s.incStat(Stat.CHOPPER_DEATHS); plugin.p(attacker).s.incStat(Stat.CHOPPER_KILLS); break;
                    case 7: desc = " headshot"; plugin.p(p).s.incStat(Stat.BOW_DEATHS); plugin.p(attacker).s.incStat(Stat.HEADSHOTS); plugin.p(attacker).s.incStat(Stat.BOW_KILLS); break;
                }
                if (reason > 0) {
                	plugin.game.sendMessage(team.BOTH, plugin.d + plugin.p(attacker).getTeam().getColour() + plugin.p(attacker).nick + plugin.d + "c" + desc + " " + plugin.d + t.getColour() + nick + assist_txt);
                }
                clearinv();
                todrop += ammo;
                dropl = p.getLocation();
                
                p.teleport(plugin.prespawn);
                dead = true;
                
                plugin.game.onKill(plugin.p(attacker), this, p.getLocation());
            }
            if (_h > 0) {
            assist = attacker;
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