package com.Top_Cat.CODMW.listeners;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

import com.Top_Cat.CODMW.main;

public class CODWeatherListener extends WeatherListener {

	main plugin;
	
	public CODWeatherListener(main instance) {
		plugin = instance;
	}

	@Override
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState() != plugin.currentMap.storm) {
			event.setCancelled(true);
		}
	}
	
}