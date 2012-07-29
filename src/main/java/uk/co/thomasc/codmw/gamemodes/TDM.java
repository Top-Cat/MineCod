package uk.co.thomasc.codmw.gamemodes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;

public class TDM extends TeamGM {

	int diam, gold;
	
	public TDM(Main instance) {
		super(instance);
		scorelimit = plugin.getVarValue("scorelimit", 50);
	}
	
	@Override
	public void setup() {
		spawns.add(0, plugin.currentMap.getSpawns(0));
		spawns.add(1, plugin.currentMap.getSpawns(1));
		spawns.add(2, plugin.currentMap.getSpawns(2));
	}
	
	@Override
	public void startGame() {
		super.startGame();
		diam = 0;
		gold = 0;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (time > gamelength) {
			Team d = Team.GOLD;
			if (diam > gold) {
				d = Team.DIAMOND;
			} else if (diam == gold) {
				d = Team.BOTH;
			}
			onWin(d, null, null);
		}
	}
	
	@Override
	public void onKill(CPlayer attacker, CPlayer defender, Location l, Reason r) {
		super.onKill(attacker, defender, l, r);
		attacker.addPoints(5);
		defender.addPoints(-2);
		
		int add = attacker.getTeam() == defender.getTeam() ? -1 : 1;
		
		if (attacker.getTeam() == Team.GOLD) {
			gold += add;
		} else {
			diam += add;
		}
		
		if (diam >= scorelimit) {
			onWin(Team.DIAMOND, attacker, defender);
		} else if (gold >= scorelimit) {
			onWin(Team.GOLD, attacker, defender);
		}
	}
	
	@Override
	public Location spawnTele(CPlayer _p, Player p, boolean start) {
		Location g = null;
		ArrayList<Player> alivePlayers = new ArrayList<Player>();
		for (CPlayer i : plugin.players.values()) {
			if (i.dead == false && i.getTeam() == _p.getTeam() && i.stime < System.currentTimeMillis() && spawnCheck(i.p.getLocation())) {
				alivePlayers.add(i.p);
			}
		}
		if (alivePlayers.size() <= 1 || start) {
			if (start) {
				switch (_p.getTeam()) {
					case DIAMOND: g = spawns.get(0).get(generator.nextInt(spawns.get(0).size())); break;
					case GOLD: g = spawns.get(1).get(generator.nextInt(spawns.get(1).size())); break;
				}
			} else {
				ArrayList<Location> spawn = new ArrayList<Location>();
				for (Location i : spawns.get(2)) {
					if (spawnCheck(i)) { spawn.add(i); }
				}
				g = spawn.get(generator.nextInt(spawn.size()));
			}
		} else {
			g = alivePlayers.get(generator.nextInt(alivePlayers.size())).getLocation();
		}
		_p.stime = System.currentTimeMillis() + 5000;
		p.teleport(g);
		if (start) {
			p.sendMessage(_p.getTeam().getColour() + _p.getTeam().toString() + " team go!");
		}
		return g;
	}
	
	@Override
	public void printScore(Player p, Team t) {
		if (t == Team.BOTH) {
			p.sendMessage(ChatColor.GOLD + "Gold: " + gold + ChatColor.WHITE + "  " + ChatColor.AQUA + "Diamond: " + diam + ChatColor.WHITE + "      / " + scorelimit);
		} else if (t == Team.GOLD || t == Team.DIAMOND) {
			p.sendMessage(t.getColour() + "Player Name | Ki | A | D |");
			for (CPlayer i : plugin.players.values()) {
				if (i.getTeam() == t) {
					p.sendMessage(t.getColour() + i.nick + " | " + i.kill + " | " + i.assists + " | " + i.death + " |");
				}
			}
			p.sendMessage(t.getColour() + (t == Team.DIAMOND ? diam : gold) + " / " + scorelimit);
		}
	}
	
	@Override
	public CPlayer getTopPlayer(Team t) {
		CPlayer out = null;
		int mkills = -1;
		for (CPlayer i : plugin.players.values()) {
			if (i.getTeam() == t) {
				if (i.kill > mkills) {
					out = i;
					mkills = i.kill;
				}
			}
		}
		return out;
	}

}