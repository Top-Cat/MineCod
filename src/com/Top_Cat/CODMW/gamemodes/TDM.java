package com.Top_Cat.CODMW.gamemodes;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.player;

public class TDM extends team_gm {

    int diam, gold;
    
    public TDM(main instance) {
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
            team d = team.GOLD;
            if (diam > gold) {
                d = team.DIAMOND;
            } else if (diam == gold) {
                d = team.BOTH;
            }
            onWin(d, null, null);
        }
    }
    
    @Override
    public void onKill(player attacker, player defender, Location l) {
        super.onKill(attacker, defender, l);
        attacker.addPoints(5);
        defender.addPoints(-2);
        
        int add = attacker == defender ? -1 : 1;
        
        if (attacker.getTeam() == team.GOLD) {
            gold += add;
        } else {
            diam += add;
        }
        
        if (diam >= scorelimit) {
            onWin(team.DIAMOND, attacker, defender);
        } else if (gold >= scorelimit) {
            onWin(team.GOLD, attacker, defender);
        }
    }
    
    @Override
    public Location spawnTele(player _p, Player p, boolean start) {
        Location g = null;
        ArrayList<Player> alivePlayers = new ArrayList<Player>();
        for (player i : plugin.players.values()) {
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
            p.sendMessage(plugin.d + _p.getTeam().getColour() + _p.getTeam().toString() + " team go!");
        }
        return g;
    }
    
    @Override
    public void printScore(Player p, team t) {
        if (t == team.BOTH) {
            p.sendMessage(plugin.d + "6Gold: " + gold + plugin.d + "f  " + plugin.d + "bDiamond: " + diam + plugin.d + "f      / " + scorelimit);
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
            p.sendMessage(plugin.d + c + (t == team.DIAMOND ? diam : gold) + " / " + scorelimit);
        }
    }
    
    @Override
    public player getTopPlayer(team t) {
        player out = null;
        int mkills = -1;
        for (player i : plugin.players.values()) {
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