package com.Top_Cat.CODMW.vote;

import java.util.Date;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class MapVote extends Vote {

	String mapname;
	Integer mapid;
	
	public MapVote(main instance, String map, Integer mid, player player) {
		super(instance, player);
		mapname = map;
		mapid = mid;
	}
	
	@Override
	public void onCreate() {
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage(s.nick + " called a vote to change map to " + mapname);
		}
		super.onCreate();
	}
	
	@Override
	public void onComplete(boolean result) {
		super.onComplete(result);
		if (result) {
			plugin.loadmap(mapid);
		}
	}
	
	@Override
	public void status(Player p, String fg) {
		String na = p.getDisplayName();
		player t = plugin.p(p);
		if (t != null) {
			na = t.nick;
		}
        for (Player i : plugin.getServer().getOnlinePlayers()) {
            i.sendMessage(na + " voted " + fg + " the map change. (" + ((y / (y + n)) * 100) + "% in favour, " + ((end - new Date().getTime()) / 1000) + " seconds left)");
        }
	}
	
}