package uk.co.thomasc.codmw.vote;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CMap;
import uk.co.thomasc.codmw.objects.CPlayer;

public class MapVote extends Vote {

	private CMap map;
	
	public MapVote(Main instance, CMap _m, CPlayer player) {
		super(instance, player);
		map = _m;
	}
	
	@Override
	public void onCreate() {
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage(s.nick + " called a vote to change map to " + map.name);
		}
		super.onCreate();
	}
	
	@Override
	public void onComplete(boolean result) {
		super.onComplete(result);
		if (result) {
			plugin.loadmap(map);
		}
	}
	
	@Override
	public void status(Player p, String fg) {
		String na = p.getDisplayName();
		CPlayer t = plugin.p(p);
		if (t != null) {
			na = t.nick;
		}
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage(na + " voted " + fg + " the map change. (" + ((yes * 100) / (plugin.tot)) + "% in favour, " + ((end - System.currentTimeMillis()) / 1000) + " seconds left)");
		}
	}
	
}