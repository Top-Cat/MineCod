package uk.co.thomasc.codmw.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Stat;

public class FFA extends Gamemode {

	public HashMap<CPlayer, Integer> scores = new HashMap<CPlayer, Integer>();
	
	public FFA(Main instance) {
		super(instance);
		scorelimit = plugin.getVarValue("scorelimit", 20);
	}
	
	@Override
	public void setup() {
		spawns.add(0, plugin.currentMap.getSpawns(2));
	}
	
	@Override
	public void tick() {
		super.tick();
		if (time > gamelength) {
			CPlayer p = null;
			int max = 0;
			for (CPlayer i : scores.keySet()) {
				if (scores.get(i) > max) {
					max = scores.get(i);
					p = i;
				}
			}
			onWin(p, null, null);
		}
	}
	
	public void onWin(CPlayer winner, CPlayer lastkill, CPlayer lastdeath) {
		if (winner == null) {
			sendMessage(Team.BOTH, "DRAW");
		} else {
			winner.p.sendMessage(winmesssages.get(generator.nextInt(winmesssages.size())));
			sendMessage(Team.BOTH, lossmesssages.get(generator.nextInt(lossmesssages.size())), winner.p);
			winner.s.incStat(Stat.WINS);
		}
		
		for (CPlayer i : plugin.players.values()) {
			if (winner != i) {
				i.s.incStat(Stat.LOSSES);
				i.addPoints(-5);
			}
		}
		
		super.onWin(Team.BOTH, lastkill, lastdeath);
	}
	
	@Override
	public void onKill(CPlayer attacker, CPlayer defender, Location l, Reason r) {
		super.onKill(attacker, defender, l, r);
		attacker.addPoints(5);
		defender.addPoints(-2);
		
		int s = scores.containsKey(attacker) ? scores.get(attacker) + 1 : 1;
		
		scores.put(attacker, s);
		
		if (scores.get(attacker) >= scorelimit) {
			onWin(attacker, attacker, defender);
		}
	}
	
	@Override
	public Location spawnTele(CPlayer _p, Player p, boolean start) {
		Location g = null;
		ArrayList<Location> spawn = new ArrayList<Location>();
		for (Location i : spawns.get(0)) {
			if (spawnCheck(i)) { spawn.add(i); }
		}
		g = spawn.get(generator.nextInt(spawn.size()));
		_p.stime = System.currentTimeMillis() + 5000;
		p.teleport(g);
		return g;
	}
	
	@Override
	public void printScore(Player p, Team t) {
		p.sendMessage(ChatColor.BLUE + "Player Name | Ki | A | D |");
		for (CPlayer i : plugin.players.values()) {
			if (i.getTeam() == t) {
				p.sendMessage(ChatColor.LIGHT_PURPLE + i.nick + " | " + i.kill + " | " + i.assists + " | " + i.death + " |");
			}
		}
	}
	
	@Override
	public void jointele(Player p) {
		p.teleport(plugin.prespawn);
		new CPlayer(plugin, p, Team.BOTH);
	}
	
	@Override
	public boolean canHit(LivingEntity a, LivingEntity d, boolean killstreak, boolean ignoreff) {
		if (killstreak && d instanceof Player && plugin.p((Player) d).getVar("ghost", 0) == 1) {
			return false;
		}
		return (a != d);
	}
	
	@Override
	public String getClaymoreText(Player p) {
		return "[ " + plugin.p(p).nick + " ]";
	}

	@Override
	public CPlayer getTopPlayer(Team t) {
		CPlayer out = null;
		int mkills = -1;
		for (CPlayer i : plugin.players.values()) {
			if (i.kill > mkills) {
				out = i;
				mkills = i.kill;
			}
		}
		return out;
	}	
	
}