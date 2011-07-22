package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.sql.Stat;

public class apple extends useable {

	public apple(main instance, Player owner, Object[] args) {
		super(instance, owner, args);
        getOwnerplayer().s.incStat(Stat.APPLES_USED);
        setArmour();
        getOwner().updateInventory();
        getOwnerplayer().inv = true;
	}
	
	public void setArmour() {
        if (getOwnerplayer().getTeam() == team.GOLD) {
        	getOwner().getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
        	getOwner().getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
        	getOwner().getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
        } else {
        	getOwner().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
        	getOwner().getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
        	getOwner().getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));
        }
	}
	
	@Override
	public void teamSwitch() {
		super.teamSwitch();
		setArmour();
	}
	
	@Override
	public void destroy() {
		super.destroy();
        getOwner().getInventory().clear(38);
        getOwner().getInventory().clear(37);
        getOwner().getInventory().clear(36);
        getOwnerplayer().inv = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getLifetime() > 10) {
			destroy();
		}
	}
	
}