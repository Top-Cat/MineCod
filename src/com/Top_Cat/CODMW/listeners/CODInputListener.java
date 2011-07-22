package com.Top_Cat.CODMW.listeners;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkitcontrib.event.input.InputListener;
import org.bukkitcontrib.event.input.KeyPressedEvent;
import org.bukkitcontrib.event.input.KeyReleasedEvent;
import org.bukkitcontrib.keyboard.Keyboard;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.Achievement;

public class CODInputListener extends InputListener {
    
    main plugin;
    List<Keyboard> konami = Arrays.asList(Keyboard.KEY_I, Keyboard.KEY_I, Keyboard.KEY_K, Keyboard.KEY_K, Keyboard.KEY_J, Keyboard.KEY_L, Keyboard.KEY_J, Keyboard.KEY_L, Keyboard.KEY_B, Keyboard.KEY_A);
    HashMap<Player, Integer> konami_p = new HashMap<Player, Integer>();
    
    public CODInputListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onKeyReleasedEvent(KeyReleasedEvent event) {
        
    }
    
    @Override
    public void onKeyPressedEvent(KeyPressedEvent event) {
        if (event.getKey() == Keyboard.KEY_R) {
            plugin.getServer().dispatchCommand(event.getPlayer(), "r");
            plugin.p(event.getPlayer()).rtime = new Date().getTime() + 3000;
        }
        int p = konami_p.containsKey(event.getPlayer()) ? konami_p.get(event.getPlayer()) : 0;
        if (event.getKey() == konami.get(p)) {
            p++;
            if (p >= 10) {
                plugin.p(event.getPlayer()).s.awardAchievement(Achievement.KONAMI);
                p = 0;
            }
        } else {
            p = 0;
        }
        konami_p.put(event.getPlayer(), p);
    }
    
}