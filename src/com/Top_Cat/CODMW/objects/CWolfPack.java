package com.Top_Cat.CODMW.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.Top_Cat.CODMW.main;

public class CWolfPack extends ownable {
    
    public List<Wolf> wolf = new ArrayList<Wolf>();
    public long expire;
    main plugin;
    
    public CWolfPack(main instance, List<Wolf> i, Player _o, long _e) {
        wolf = i;
        plugin = instance;
        setOwner(_o, plugin.p(_o));
        expire = _e;
    }
    
    public void remove(Wolf r) {
        wolf.remove(r);
        r.remove();
    }
    
    public void removeAll() {
        for (Wolf i : wolf) {
            i.remove();
        }
        wolf.clear();
    }
    
}