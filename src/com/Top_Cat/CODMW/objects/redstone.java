package com.Top_Cat.CODMW.objects;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.Top_Cat.CODMW.game;

public class redstone {
	
	ArrayList<Block> torches = new ArrayList<Block>();
	ArrayList<String> nums = new ArrayList<String>();
	int cur = 0;
	Timer t = new Timer();
	
	public redstone(Block bl) {
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
		t.schedule(new countdown(), 1000, 1000);
	}
	
	game g;
	
	public void countdown(game _g) {
		g = _g;
		cur = 6;
	}
	
	public class countdown extends TimerTask {

		@Override
		public void run() {
			if (cur > 0) {
				display(nums.get(--cur));
			} else {
				if (g != null) {
					g.startGame();
					g = null;
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