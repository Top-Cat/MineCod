package com.Top_Cat.CODMW.objects;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.Top_Cat.CODMW.main;

public class CWolf extends ownable {
    
    public Wolf wolf;
    public long expire;
    main plugin;
    
    public CWolf(main instance, Wolf i, Player _o, long _e) {
        wolf = i;
        setOwner(_o);
        expire = _e;
        plugin = instance;
    }
    
}