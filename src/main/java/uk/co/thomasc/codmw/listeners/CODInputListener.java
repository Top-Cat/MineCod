package uk.co.thomasc.codmw.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Achievement;

public class CODInputListener implements Listener {
	
	private Main plugin;
	private List<Keyboard> konami = Arrays.asList(Keyboard.KEY_I, Keyboard.KEY_I, Keyboard.KEY_K, Keyboard.KEY_K, Keyboard.KEY_J, Keyboard.KEY_L, Keyboard.KEY_J, Keyboard.KEY_L, Keyboard.KEY_B, Keyboard.KEY_A);
	private HashMap<Player, Integer> konami_p = new HashMap<Player, Integer>();
	
	public CODInputListener(Main instance) {
		plugin = instance;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onKeyPressedEvent(KeyPressedEvent event) {
		CPlayer u = plugin.p(event.getPlayer());
		if (u != null) {
			if (event.getKey() == Keyboard.KEY_R) {
				plugin.getServer().dispatchCommand(event.getPlayer(), "r");
			}
			int p = konami_p.containsKey(event.getPlayer()) ? konami_p.get(event.getPlayer()) : 0;
			if (event.getKey() == konami.get(p)) {
				p++;
				if (p >= 10) {
					u.s.awardAchievement(Achievement.KONAMI);
					p = 0;
				}
			} else {
				p = 0;
			}
			konami_p.put(event.getPlayer(), p);
		}
	}
	
}