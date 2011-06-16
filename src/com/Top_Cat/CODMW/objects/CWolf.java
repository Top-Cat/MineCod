package com.Top_Cat.CODMW.objects;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;

public class CWolf {
    
    public Player owner;
    public Wolf wolf;
    public long expire;
    public team t;
    main plugin;
    
    public CWolf(main instance, Wolf i, Player _o, long _e) {
        wolf = i;
        owner = _o;
        expire = _e;
        plugin = instance;
        t = plugin.p(owner).getTeam();
    }
    
}