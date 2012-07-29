package uk.co.thomasc.codmw.objects;

import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.sql.Achievement;

public class Ownable {
	
	private Player owner;
	private CPlayer owner_p;
	private int kills = 0;
	
	public void incKills() {
		kills++;
		if (kills >= 10) {
			owner_p.s.awardAchievement(Achievement.EXTERMINATION);
		}
	}
	
	public void setOwner(Player o, CPlayer p) {
		owner = o;
		owner_p = p;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public CPlayer getOwnerplayer() {
		return owner_p;
	}
	
}