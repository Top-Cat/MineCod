package com.Top_Cat.CODMW.vote;

import java.util.Date;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.map;
import com.Top_Cat.CODMW.objects.player;

public class MapVote extends Vote {

    map m;
    
    public MapVote(main instance, map _m, player player) {
        super(instance, player);
        m = _m;
    }
    
    @Override
    public void onCreate() {
        for (Player i : plugin.getServer().getOnlinePlayers()) {
            i.sendMessage(s.nick + " called a vote to change map to " + m.name);
        }
        super.onCreate();
    }
    
    @Override
    public void onComplete(boolean result) {
        super.onComplete(result);
        if (result) {
            plugin.loadmap(m);
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
            i.sendMessage(na + " voted " + fg + " the map change. (" + ((y * 100) / (plugin.tot)) + "% in favour, " + ((end - new Date().getTime()) / 1000) + " seconds left)");
        }
    }
    
}