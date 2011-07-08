package com.Top_Cat.CODMW.gamemodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.sql.Stat;

public class FFA extends gamemode {

    
    public HashMap<player, Integer> scores = new HashMap<player, Integer>();
    
    public FFA(main instance) {
        super(instance);
    }
    
    @Override
    public void setup() {
        spawns.add(0, plugin.currentMap.getSpawns(2));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (time > 600) {
            player p = null;
            int max = 0;
            for (player i : scores.keySet()) {
                if (scores.get(i) > max) {
                    max = scores.get(i);
                    p = i;
                }
            }
            onWin(p, null, null);
        }
    }
    
    public void onWin(player winner, player lastkill, player lastdeath) {
        if (winner == null) {
            sendMessage(team.BOTH, "DRAW");
        } else {
            winner.p.sendMessage(winmesssages.get(generator.nextInt(winmesssages.size())));
            sendMessage(team.BOTH, lossmesssages.get(generator.nextInt(lossmesssages.size())), winner.p);
        }
        
        winner.s.incStat(Stat.WINS);
        for (player i : plugin.players.values()) {
            if (winner != i) {
                i.s.incStat(Stat.LOSSES);
                i.addPoints(-5);
            }
        }
        
        super.onWin(team.BOTH, lastkill, lastdeath);
    }
    
    @Override
    public void onKill(player attacker, player defender, Location l) {
        super.onKill(attacker, defender, l);
        attacker.addPoints(5);
        defender.addPoints(-2);
        
        int s = scores.containsKey(attacker) ? scores.get(attacker) + 1 : 1;
        
        scores.put(attacker, s);
        
        if (scores.get(attacker) >= 20) {
            onWin(attacker, attacker, defender);
        }
    }
    
    @Override
    public Location spawnTele(player _p, Player p, boolean start) {
        Location g = null;
        ArrayList<Location> spawn = new ArrayList<Location>();
        for (Location i : spawns.get(0)) {
            if (spawnCheck(i)) { spawn.add(i); }
        }
        g = spawn.get(generator.nextInt(spawn.size()));
        _p.stime = new Date().getTime() + 5000;
        p.teleport(g);
        return g;
    }
    
    @Override
    public void printScore(Player p, team t) {
        p.sendMessage(plugin.d + "9Player Name | Ki | A | D |");
        for (player i : plugin.players.values()) {
            if (i.getTeam() == t) {
                p.sendMessage(plugin.d + "d" + i.nick + " | " + i.kill + " | " + i.assists + " | " + i.death + " |");
            }
        }
    }
    
    @Override
    public void jointele(Player p) {
        p.teleport(plugin.prespawn);
        new player(plugin, p, team.BOTH);
    }
    
    @Override
    public boolean canHit(LivingEntity a, LivingEntity d) {
        return (a != d);
    }
    
    @Override
    public String getClaymoreText(Player p) {
    	return "[ " + plugin.p(p).nick + " ]";
    }

}