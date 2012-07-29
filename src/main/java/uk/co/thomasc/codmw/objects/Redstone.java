package uk.co.thomasc.codmw.objects;

import java.util.ArrayList;
import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.block.Block;

import uk.co.thomasc.codmw.Main;

public class Redstone {
	
	private ArrayList<Block> torches = new ArrayList<Block>();
	private ArrayList<String> nums = new ArrayList<String>();
	private int cur = 0;
	private Main plugin;
	private boolean s = true;
	
	public Redstone(Block bl, Main instance) {
		plugin = instance;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				torches.add(bl.getRelative(0, j, i));
			}
		}
		nums.add("......   ..   ......");
		nums.add(".    .  . ......    ");
		nums.add("... .. . .. . .. ...");
		nums.add(". . .. . .. . ......");
		nums.add(" .... .   ...   .   ");
		nums.add(". .... . .. . .... .");
		display(nums.get(0));
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new countdown(), 20L, 20L);
	}
	
	public void countdowns() {
		cur = 6;
		s = false;
	}
	
	public class countdown extends TimerTask {

		@Override
		public void run() {
			if (cur > 0) {
				display(nums.get(--cur));
			} else {
				if (!s && plugin.game != null) {
					plugin.game.startGame();
					s = true;
				}
			}
		}
		
	}
	
	public void display(String i) {
		try {
			int a = 0;
			for (Block b : torches) {
				if (i.substring(a, a + 1).equals(".")) {
					b.setType(Material.REDSTONE_TORCH_ON);
				} else {
					b.setType(Material.AIR);
				}
				a++;
			}
		} catch (NullPointerException e) {
			
		}
	}
	
}