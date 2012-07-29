package uk.co.thomasc.codmw.killstreaks.useable;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.killstreaks.Killstreaks;
import uk.co.thomasc.codmw.sql.Stat;

public class CarePackage extends Useable {
	
	Location dropl;
	Random generator = new Random();
	
	public CarePackage(Main instance, Player owner, Object[] args) {
		super(instance, owner, args);
		plugin.game.sendMessage(Team.BOTH, getOwnerplayer().getTeam().getColour() + getOwnerplayer().nick + " called in a care package!");
		getOwnerplayer().s.incStat(Stat.CAREPACKAGES_USED);
		dropl = getOwner().getEyeLocation();
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getLifetime() >= 10) {
			Killstreaks i;
			if (getOwnerplayer().getVar("randomstreak", 0) == 0) {
				i = Killstreaks.getRandom();
			} else {
				i = Killstreaks.valueOf(generator.nextInt(8) + 1);
			}
			plugin.currentWorld.dropItemNaturally(dropl, new ItemStack(i.getMat().getRawId(), i.getAmm()));
			getOwner().sendMessage("Care package dropped " + i.toString());
			destroy();
		}
	}
	
}