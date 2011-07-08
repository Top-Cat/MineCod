package com.Top_Cat.CODMW.vote;

import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class Vote {

    int y = 0, n = 0;
    long end;
    main plugin;
    player s;
    HashMap<Player, Boolean> votes = new HashMap<Player, Boolean>();

    public Vote(main instance, player player) {
        plugin = instance;
        s = player;
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new start(), 1L);
    }
    
    public class start extends TimerTask {

        @Override
        public void run() {
            onCreate();
        }
        
    }
    
    public void onCreate() {
        for (Player i : plugin.getServer().getOnlinePlayers()) {
            i.sendMessage("Type /y or /n to vote!");
        }
        end = new Date().getTime() + 30000;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Complete(), 600L);
    }
    
    public class Complete extends TimerTask {

        @Override
        public void run() {
            onComplete(y > n);
        }
        
    }

    public void onComplete(boolean result) {
        plugin.v = null;
    }
    
    public void VoteUp(Player p) {
        if (votes.containsKey(p)) {
            if (!votes.get(p)) {
                n--;
                y++;
            }
        } else {
            y++;
        }
        votes.put(p, true);
        status(p, "for");
    }
    
    public void VoteDown(Player p) {
        if (votes.containsKey(p)) {
            if (votes.get(p)) {
                y--;
                n++;
            }
        } else {
            n++;
        }
        votes.put(p, false);
        status(p, "against");
    }
    
    public void status(Player p, String fg) { };
    
}