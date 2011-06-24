package com.Top_Cat.CODMW.gamemodes;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.player;

public class TDM extends gamemode {

    int diam, gold;
	
	public TDM(main instance) {
		super(instance);
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
		if (time > 600) {
			onWin((diam > gold) ? team.DIAMOND : team.GOLD, null, null);
		}
	}
	
	@Override
	public void onKill(player attacker, player defender, Location l) {
		super.onKill(attacker, defender, l);
		attacker.addPoints(5);
		defender.addPoints(-2);
		
		if (attacker.getTeam() == team.GOLD) {
            gold++;
        } else {
            diam++;
        }
		
		if (diam >= 50) {
			onWin(team.DIAMOND, attacker, defender);
		} else if (gold >= 50) {
			onWin(team.GOLD, attacker, defender);
		}
	}
	
	@Override
	public Location spawnTele(player _p, Player p, boolean start) {
		Location g = null;
        ArrayList<Player> alivePlayers = new ArrayList<Player>();
        for (player i : plugin.players.values()) {
            if (i.dead == false && i.getTeam() == _p.getTeam() && i.stime < new Date().getTime() && spawnCheck(i.p.getLocation())) {
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
        _p.stime = new Date().getTime() + 5000;
        p.teleport(g);
        if (start) {
        	p.sendMessage(plugin.d + _p.getTeam().getColour() + _p.getTeam().toString() + " team go!");
        }
        return g;
	}
	
	@Override
	public void printScore(Player p, team t) {
		if (t == team.BOTH) {
			p.sendMessage(plugin.d + "6Gold: " + gold + plugin.d + "f  " + plugin.d + "bDiamond: " + diam + plugin.d + "f      / 50");
		} else if (t == team.GOLD || t == team.DIAMOND) {
			String c = "6";
            if (t == team.DIAMOND) {
                c = "b";
            }
            
            p.sendMessage(plugin.d + c + "Player Name | Ki | A | D |");
            for (player i : plugin.players.values()) {
                if (i.getTeam() == t) {
                    p.sendMessage(plugin.d + c + i.nick + " | " + i.kill + " | " + i.assists + " | " + i.death + " |");
                }
            }
            p.sendMessage(plugin.d + c + (t == team.DIAMOND ? diam : gold) + " / 50");
		}
	}

}