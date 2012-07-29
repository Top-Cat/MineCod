package uk.co.thomasc.codmw.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import uk.co.thomasc.codmw.Main;

public class CODInventoryListener implements Listener {
	
	public CODInventoryListener(Main instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		// || event.getSlotType() == SlotType.BOOTS || event.getSlotType() == SlotType.HELMET || event.getSlotType() == SlotType.LEGGINGS
		if (event.getSlotType() == SlotType.ARMOR) {
			event.setCancelled(true);
		}
	}
	
}