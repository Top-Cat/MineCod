package com.Top_Cat.CODMW.objects;

import org.bukkit.entity.Player;

public class ownable {
    
    private Player owner;
    
    public void setOwner(Player o) {
        owner = o;
    }
    
    public Player getOwner() {
        return owner;
    }
    
}