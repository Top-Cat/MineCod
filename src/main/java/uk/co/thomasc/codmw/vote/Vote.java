package uk.co.thomasc.codmw.vote;

import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.objects.CPlayer;

public class Vote {

	protected int yes = 0;
	private int no = 0;
	protected long end;
	protected Main plugin;
	protected CPlayer s;
	private HashMap<Player, Boolean> votes = new HashMap<Player, Boolean>();

	public Vote(Main instance, CPlayer player) {
		plugin = instance;
		s = player;
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new start(), 1L);
	}
	
	public class start extends TimerTask {

		@Override
		public void run() {
			onCreate();
		}
		
	}
	
	public void onCreate() {
		for (Player i : plugin.getServer().getOnlinePlayers()) {
			i.sendMessage("Type /y or /n to vote!");
		}
		end = System.currentTimeMillis() + 30000;
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Complete(), 600L);
	}
	
	public class Complete extends TimerTask {

		@Override
		public void run() {
			onComplete(yes >= Math.ceil(plugin.tot / 2));
		}
		
	}

	public void onComplete(boolean result) {
		plugin.v = null;
	}
	
	public void VoteUp(Player player) {
		if (votes.containsKey(player)) {
			if (!votes.get(player)) {
				no--;
				yes++;
			}
		} else {
			yes++;
		}
		votes.put(player, true);
		status(player, "for");
	}
	
	public void VoteDown(Player p) {
		if (votes.containsKey(p)) {
			if (votes.get(p)) {
				yes--;
				no++;
			}
		} else {
			no++;
		}
		votes.put(p, false);
		status(p, "against");
	}
	
	public void status(Player p, String fg) { };
	
}