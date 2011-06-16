package com.Top_Cat.CODMW.sql;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.player;

public class stats {
    
    Timer t = new Timer();
    player p;
    HashMap<Stat, Integer> stats = new HashMap<Stat, Integer>();
    List<Achieve> achs = new ArrayList<Achieve>();
    List<Achieve> toach = new java.util.ArrayList(Arrays.asList(Achieve.values()));
    List<Stat> updated = new ArrayList<Stat>();
    List<Stat> newv = new ArrayList<Stat>();
    List<Achieve> newa = new ArrayList<Achieve>();
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
                achs.add(Achieve.valueOf(r2.getInt("aid")));
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
        if (stats.containsKey(s)) {
            updated.add(s);
        } else {
            newv.add(s);
        }
        int out = getStat(s) + c;
        if (out < 0) { out = 0; }
        ArrayList<Achieve> tmp = new ArrayList<Achieve>();
        for (Achieve a : toach) {
            if (a.getStat() == s && a.getCount() <= out) {
                tmp.add(a);
            }
        }
        for (Achieve a : tmp) {
        	awardAchieve(a);
        }
        stats.put(s, out);
    }
    
    public void maxStat(Stat s, int c) {
        if (stats.containsKey(s) && stats.get(s) < c) {
            updated.add(s);
        } else if (!stats.containsKey(s)) {
            newv.add(s);
        } else {
            return;
        }
        for (Achieve a : toach) {
            if (a.getStat() == s && a.getCount() <= c) {
                awardAchieve(a);
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
    
    public void awardAchieve(Achieve a) {
        if (!achs.contains(a)) {
            plugin.game.sendMessage(team.BOTH, plugin.d + p.getTeam().getColour() + p.nick + " earned achievement: '" + a.getName() + "'", p.p);
            p.p.sendMessage(plugin.d + p.getTeam().getColour() + "You earned achievement: '" + a.getName() + "'");
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
        for (Achieve a : newa) {
            plugin.sql.update("INSERT INTO cod_achievement VALUES(NULL, '" + p.dbid + "', '" + a.getId() + "')");
        }
        newa.clear();
    }
}