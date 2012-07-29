package uk.co.thomasc.codmw.gamemodes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.killstreaks.useable.WolfPack;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Stat;

public class TeamGM extends Gamemode {
	
	public TeamGM(Main instance) {
		super(instance);
	}
	
	@Override
	public void startGame() {
		super.startGame();
	}
	
	@Override
	public void playerjoin(PlayerJoinEvent event) {
		super.playerjoin(event);
		event.getPlayer().sendMessage(ChatColor.BLUE + "Please choose your team!");
	}
	
	@Override
	public void onWin(Team winners, CPlayer lastkill, CPlayer lastdeath) {
		Team lost = winners == Team.DIAMOND ? Team.GOLD : Team.DIAMOND;
		sendMessage(winners, winmesssages.get(generator.nextInt(winmesssages.size())));
		sendMessage(lost, lossmesssages.get(generator.nextInt(lossmesssages.size())));
		
		for (CPlayer i : plugin.players.values()) {
			switch(i.getTeam()) {
				case GOLD: i.p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1)); break;
				case DIAMOND: i.p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1)); break;
			}
			if (winners != Team.BOTH) {
				if (i.getTeam() == winners) {
					i.s.incStat(Stat.WINS);
					i.addPoints(10);
				} else {
					i.s.incStat(Stat.LOSSES);
					i.addPoints(-5);
				}
			}
		}
		
		super.onWin(winners, lastkill, lastdeath);
	}
	
	@Override
	public void jointele(Player p) {
		p.teleport(plugin.teamselect);
		plugin.players.remove(p);
	}
	
	@Override
	public boolean canHit(LivingEntity a, LivingEntity d, boolean killstreak, boolean ignoreff) {
		if (killstreak && d instanceof Player && plugin.p((Player) d) != null && plugin.p((Player) d).getVar("ghost", 0) == 1) {
			return false;
		}
		if (ff && !ignoreff && a != d) { return true; }
		Team t1 = Team.BOTH;
		Team t2 = Team.BOTH;
		if (a instanceof Player) {
			CPlayer p = plugin.p((Player) a);
			if (p != null) {
				t1 = p.getTeam();
			}
		} else if (a instanceof Wolf) {
			for (MineCodListener i : plugin.listeners) {
				if (i instanceof WolfPack && ((WolfPack) i).wolf.contains(a)) {
					t1 = i.getOwnerplayer().getTeam();
				}
			}
		}
		if (d instanceof Player) {
			CPlayer p = plugin.p((Player) d);
			if (p != null) {
				t2 = p.getTeam();
			}
		} else if (d instanceof Wolf) {
			for (MineCodListener i : plugin.listeners) {
				if (i instanceof WolfPack && ((WolfPack) i).wolf.contains(d)) {
					t2 = i.getOwnerplayer().getTeam();
				}
			}
		}
		return (t1 != t2);
	}
	
	@Override
	public String getClaymoreText(Player p) {
		if (plugin.p(p).getTeam() == Team.DIAMOND) {
			return ChatColor.AQUA + "** DIAMOND **";
		} else {
			return ChatColor.GOLD + "-- GOLD --";
		}
	}
	
}