package com.Top_Cat.CODMW.vote;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.objects.player;

public class GameTypeVote extends Vote {
    
    GameModes gm;
    
    public GameTypeVote(main instance, String mode, player player) {
        super(instance, player);
        gm = GameModes.getGMFromId(mode);
    }
    
    @Override
    public void onCreate() {
        for (Player i : plugin.getServer().getOnlinePlayers()) {
            i.sendMessage(s.nick + " called a vote to change mode to " + gm.toString());
        }
        super.onCreate();
    }
    
    @Override
    public void onComplete(boolean result) {
        super.onComplete(result);
        if (result) {
            plugin.gm = gm;
            plugin.preparemap();
        }
    }
    
    public void status(Player p, String fg) {
        String na = p.getDisplayName();
        player t = plugin.p(p);
        if (t != null) {
            na = t.nick;
        }
        for (Player i : plugin.getServer().getOnlinePlayers()) {
            i.sendMessage(na + " voted " + fg + " the mode change. (" + ((y * 100) / (plugin.tot)) + "% in favour, " + ((end - System.currentTimeMillis()) / 1000) + " seconds left)");
        }
    }
    
}