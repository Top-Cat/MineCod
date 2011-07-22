package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.Stat;

public class pork extends useable {

	public pork(main instance, Player owner, Object[] args) {
		super(instance, owner, args);
        getOwnerplayer().s.incStat(Stat.PORK_USED);
        getOwner().updateInventory();
	}
	
	@Override
	public void teamSwitch() {
		super.teamSwitch();
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getLifetime() > 10) {
			destroy();
		}
	}
	
}