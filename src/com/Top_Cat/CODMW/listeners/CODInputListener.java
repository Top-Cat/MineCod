package com.Top_Cat.CODMW.listeners;

import org.bukkitcontrib.event.input.InputListener;
import org.bukkitcontrib.event.input.KeyPressedEvent;
import org.bukkitcontrib.keyboard.Keyboard;

import com.Top_Cat.CODMW.main;

public class CODInputListener extends InputListener {
    
    main plugin;
    
    public CODInputListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onKeyPressedEvent(KeyPressedEvent event) {
    	if (event.getKey() == Keyboard.KEY_R) {
    		plugin.getServer().dispatchCommand(event.getPlayer(), "r");
    	}
    }
    
}