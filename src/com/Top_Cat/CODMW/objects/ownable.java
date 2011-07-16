package com.Top_Cat.CODMW.objects;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.sql.Achievement;

public class ownable {
    
    private Player owner;
    private player owner_p;
    private int kills = 0;
    
    public void incKills() {
        kills++;
        if (kills >= 10) {
            owner_p.s.awardAchievement(Achievement.EXTERMINATION);
        }
    }
    
    public void setOwner(Player o, player p) {
        owner = o;
        owner_p = p;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public player getOwnerplayer() {
        return owner_p;
    }
    
}