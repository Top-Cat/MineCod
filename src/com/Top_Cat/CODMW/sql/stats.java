package com.Top_Cat.CODMW.sql;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class stats {
    
	Timer t = new Timer();
	player p;
	HashMap<Stat, Integer> stats = new HashMap<Stat, Integer>();
	List<Stat> updated = new ArrayList<Stat>();
	List<Stat> newv = new ArrayList<Stat>();
	main plugin;
	
    public stats(main instance, player _p) {
    	plugin = instance;
    	p = _p;
		ResultSet r = plugin.sql.query("SELECT type, count FROM cod_stats WHERE PID = '" + p.dbid + "'");
		try {
			while (r.next()) {
				stats.put(Stat.valueOf(r.getInt("type")), r.getInt("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		t.schedule(new updatestats(), 20000, 20000);
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
    	stats.put(s, getStat(s) + c);
    }
    
    public int getStat(Stat s) {
    	if (stats.containsKey(s)) {
    		return stats.get(s);
    	}
    	return 0;
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
    }
}