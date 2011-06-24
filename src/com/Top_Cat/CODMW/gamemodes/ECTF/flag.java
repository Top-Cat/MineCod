package com.Top_Cat.CODMW.gamemodes.ECTF;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.sql.Stat;

public class flag {
	
	main plugin;
	public Location l;
	Material b, c, d;
	public boolean taken = false;
	public Location drop;
	public boolean toret = false;
	public Item drop_i;
	public Player p;
	public long ret = 0;
	public team t; // Team who owns
	public team t2; // Team trying to capture
	
	public flag(main instance, Location _l, Material _b, Material _c, Material _d, team _t, team _t2) {
		t = _t;
		t2 = _t2;
		plugin = instance;
		l = _l;
		b = _b;
		c = _c;
		d = _d;
		setBlocks(l.getBlock(), _b, _c, _d);
	}
	
	public void takeFlag(Player _p) {
		if (!taken || toret) {
			plugin.game.sendMessage(team.BOTH, plugin.d + t2.getColour() + t2.toString() + " team have taken their flag!");
			plugin.p(_p).s.incStat(Stat.FLAG_PICKUPS);
			p = _p;
			p.getInventory().setHelmet(new ItemStack(c, 1));
			taken = true;
			toret = false;
			setBlocks(l.getBlock(), b, Material.AIR, Material.AIR);
		}
	}
	
	public void returnFlag(Material a, boolean capped) {
		if (capped) {
			plugin.game.sendMessage(t2, plugin.d + t2.getColour() + t2.toString() + " team have captured their flag, Well done!");
			plugin.game.sendMessage(t, plugin.d + t2.getColour() + t2.toString() + " team have captured their flag, Try harder!");
		} else {
			plugin.game.sendMessage(team.BOTH, plugin.d + t2.getColour() + t2.toString() + " team have failed to capture their flag!");
		}
		taken = false;
		toret = false;
		if (p != null) {
			if (capped) {
				plugin.p(p).s.incStat(Stat.FLAG_CAPTURES);
			} else {
				plugin.p(p).s.incStat(Stat.FLAG_RETURNS);
			}
			p.getInventory().setHelmet(new ItemStack(a, 1));
			p = null;
		}
		setBlocks(l.getBlock(), b, c, d);
	}
	
	public void setBlocks(Block a, Material b, Material c, Material d) {
		a.setType(b);
		a.getRelative(1, 0, 0).setType(b);
		a.getRelative(1, 0, 1).setType(b);
		a.getRelative(0, 0, 1).setType(b);
		a.getRelative(-1, 0, 1).setType(b);
		a.getRelative(-1, 0, 0).setType(b);
		a.getRelative(-1, 0, -1).setType(b);
		a.getRelative(0, 0, -1).setType(b);
		a.getRelative(1, 0, -1).setType(b);
		a.getRelative(0, 1, 0).setType(d);
		a.getRelative(0, 2, 0).setType(d);
		a.getRelative(0, 3, 0).setType(c);
	}

	public void destroy() {
		setBlocks(l.getBlock(), Material.getMaterial((int) l.getYaw()), Material.AIR, Material.AIR);
	}
	
}