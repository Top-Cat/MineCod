package uk.co.thomasc.codmw.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import uk.co.thomasc.codmw.Main;

public class CODWeatherListener implements Listener {

	private Main plugin;
	
	public CODWeatherListener(Main instance) {
		this.plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState() != plugin.currentMap.storm) {
			event.setCancelled(true);
		}
	}
	
}