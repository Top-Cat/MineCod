package uk.co.thomasc.codmw.vote;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class GameTypeVote extends Vote {
	
	GameModes gm;
	
	public GameTypeVote(Main instance, String mode, CPlayer player) {
		super(instance, player);
		gm = GameModes.getGMFromId(mode);
	}
	
	@Override
	public void onCreate() {
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage(s.nick + " called a vote to change mode to " + gm.toString());
		}
		super.onCreate();
	}
	
	@Override
	public void onComplete(boolean result) {
		super.onComplete(result);
		if (result) {
			plugin.gm = gm;
			plugin.preparemap();
		}
	}
	
	public void status(Player p, String fg) {
		String na = p.getDisplayName();
		CPlayer t = plugin.p(p);
		if (t != null) {
			na = t.nick;
		}
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage(na + " voted " + fg + " the mode change. (" + ((yes * 100) / (plugin.tot)) + "% in favour, " + ((end - System.currentTimeMillis()) / 1000) + " seconds left)");
		}
	}
	
}