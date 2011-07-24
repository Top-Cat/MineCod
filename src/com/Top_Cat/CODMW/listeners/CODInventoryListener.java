package com.Top_Cat.CODMW.listeners;

import org.bukkitcontrib.event.inventory.InventoryClickEvent;
import org.bukkitcontrib.event.inventory.InventoryListener;
import org.bukkitcontrib.event.inventory.InventorySlotType;

import com.Top_Cat.CODMW.main;

public class CODInventoryListener extends InventoryListener {
    
    main plugin;
    
    public CODInventoryListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventorySlotType.ARMOR || event.getSlotType() == InventorySlotType.BOOTS || event.getSlotType() == InventorySlotType.HELMET || event.getSlotType() == InventorySlotType.LEGGINGS) {
            event.setCancelled(true);
        }
    }
    
}