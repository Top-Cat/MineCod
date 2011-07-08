package com.Top_Cat.CODMW.sql;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkitcontrib.player.ContribPlayer;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.player;

public class stats {
    
    Timer t = new Timer();
    player p;
    HashMap<Stat, Integer> stats = new HashMap<Stat, Integer>();
    List<Achievement> achs = new ArrayList<Achievement>();
    List<Achievement> toach = new java.util.ArrayList<Achievement>(Arrays.asList(Achievement.values()));
    List<Stat> updated = new ArrayList<Stat>();
    List<Stat> newv = new ArrayList<Stat>();
    List<Achievement> newa = new ArrayList<Achievement>();
    main plugin;
    
    public stats(main instance, player _p) {
        plugin = instance;
        p = _p;
        ResultSet r = plugin.sql.query("SELECT type, count FROM cod_stats WHERE PID = '" + p.dbid + "'");
        ResultSet r2 = plugin.sql.query("SELECT aid FROM cod_achievement WHERE PID = '" + p.dbid + "'");
        try {
            while (r.next()) {
                stats.put(Stat.valueOf(r.getInt("type")), r.getInt("count"));
            }
            while (r2.next()) {
                achs.add(Achievement.valueOf(r2.getInt("aid")));
            }
            toach.removeAll(achs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        t.schedule(new updatestats(), 30000, 30000);
    }
    
    public void incStat(Stat s) {
        incStat(s, 1);
    }
    
    public void incStat(Stat s, int c) {
        synchronized (this) {
            if (stats.containsKey(s)) {
                updated.add(s);
            } else {
                newv.add(s);
            }
            int out = getStat(s) + c;
            if (out < 0) { out = 0; }
            ArrayList<Achievement> tmp = new ArrayList<Achievement>();
            for (Achievement a : toach) {
                if (a.getStat() == s && a.getCount() <= out) {
                    tmp.add(a);
                }
            }
            for (Achievement a : tmp) {
                awardAchievement(a);
            }
            stats.put(s, out);
        }
    }
    
    public void maxStat(Stat s, int c) {
        if (stats.containsKey(s) && stats.get(s) < c) {
            updated.add(s);
        } else if (!stats.containsKey(s)) {
            newv.add(s);
        } else {
            return;
        }
        for (Achievement a : toach) {
            if (a.getStat() == s && a.getCount() <= c) {
                awardAchievement(a);
            }
        }
        stats.put(s, c);
    }
    
    public int getStat(Stat s) {
        if (stats.containsKey(s)) {
            return stats.get(s);
        }
        return 0;
    }
    
    public void awardAchievement(Achievement a) {
        if (!achs.contains(a)) {
        	Player ex = p.p;
            ContribPlayer cp = (ContribPlayer) p.p;
            if (cp.isEnabledBukkitContribSinglePlayerMod()) {
            	ex = null;
            	cp.sendNotification("Achievement Get!", a.getName(), Material.DIAMOND);
            } else {
            	p.p.sendMessage(plugin.d + p.getTeam().getColour() + "You earned achievement: '" + a.getText() + "'");
            }
            plugin.game.sendMessage(team.BOTH, plugin.d + p.getTeam().getColour() + p.nick + " earned achievement: '" + a.getText() + "'", ex);
            newa.add(a);
            achs.add(a);
            toach.remove(a);
        }
    }
    
    public void destroy() {
        t.cancel();
        update();
    }
    
    public class updatestats extends TimerTask {

        @Override
        public void run() {
            update();
        }
        
    }
    
    public void update() {
        synchronized (this) {
            List<Stat> r = new ArrayList<Stat>();
            for (Stat i : newv) {
                plugin.sql.update("INSERT INTO cod_stats VALUES(NULL, '" + p.dbid + "', '" + i.getId() + "', '" + stats.get(i) + "')");
                r.add(i);
            }
            newv.removeAll(r);
            updated.removeAll(r);
            r.clear();
            for (Stat i : updated) {
                plugin.sql.update("UPDATE cod_stats SET count = '" + stats.get(i) + "' WHERE PID = '" + p.dbid + "' and type = '" + i.getId() + "'");
                r.add(i);
            }
            updated.removeAll(r);
            r.clear();
            for (Achievement a : newa) {
                plugin.sql.update("INSERT INTO cod_achievement VALUES(NULL, '" + p.dbid + "', '" + a.getId() + "')");
            }
            newa.clear();
        }
    }
}