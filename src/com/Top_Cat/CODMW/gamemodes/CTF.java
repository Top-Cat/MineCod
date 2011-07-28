package com.Top_Cat.CODMW.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.sql.Achievement;
import com.Top_Cat.CODMW.sql.Stat;

import com.Top_Cat.CODMW.gamemodes.ECTF.flag;

public class CTF extends team_gm {

    int diam, gold;
    flag f1, f2;
    boolean swap = false;
    HashMap<player, Integer> scores = new HashMap<player, Integer>();
    
    public CTF(main instance) {
        super(instance);
        scorelimit = plugin.getVarValue("scorelimit", -1);
    }
    
    @Override
    public void setup() {
        spawns.add(0, plugin.currentMap.getSpawns(0));
        spawns.add(1, plugin.currentMap.getSpawns(1));
        spawns.add(2, plugin.currentMap.getSpawns(2));
        f1 = new flag(plugin, plugin.currentMap.getSpawns(3).get(0), Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.FENCE, team.DIAMOND, team.GOLD);
        f2 = new flag(plugin, plugin.currentMap.getSpawns(4).get(0), Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.FENCE, team.GOLD, team.DIAMOND);
    }
    
    @Override
    public void startGame() {
        super.startGame();
        diam = 0;
        gold = 0;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        f1.destroy();
        f2.destroy();
    }
    
    @Override
    public void onKill(player attacker, player defender, Location l, Reason r) {
        super.onKill(attacker, defender, l, r);
        attacker.addPoints(3);
        defender.addPoints(-1);
        
        if (defender == plugin.p(f1.p)) {
            f1.everdropped = true;
            f1.drop = l;
        } else if (defender == plugin.p(f2.p)) {
            f2.everdropped = true;
            f2.drop = l;
        } else {
            return;
        }
        attacker.s.incStat(Stat.FLAG_CARRIER_KILLED);
        if (r == Reason.GRENADE) {
            attacker.s.awardAchievement(Achievement.FLAG_GRENADE);
        }
    }
    
    @Override
    public void afterDeath(player p) {
        super.afterDeath(p);
        if (f1.drop != null && plugin.p(f1.p) == p) {
            dropFlag(f1, Material.GOLD_BLOCK);
        } else if (f2.drop != null && plugin.p(f2.p) == p) {
            dropFlag(f2, Material.DIAMOND_BLOCK);
        }
    }
    
    public void dropFlag(flag fa, Material a) {
        fa.p = null;
        fa.drop_i = plugin.currentWorld.dropItem(fa.drop, new ItemStack(a, 1));
        fa.ret = System.currentTimeMillis() + 20000;
        fa.drop = null;
        fa.toret = true;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (f1.toret == true && f1.ret < System.currentTimeMillis()) {
            f1.drop_i.remove();
            f1.toret = false;
            f1.returnFlag(null, false);
        } else if (f2.toret == true && f2.ret < System.currentTimeMillis()) {
            f2.drop_i.remove();
            f2.toret = false;
            f2.returnFlag(null, false);
        }
        if (time > (gamelength / 2) && swap == false) {
            sendMessage(team.BOTH, "Switching sides!");
            Location temp = f1.l;
            Location temp2 = f2.l;
            f1.destroy();
            f2.destroy();
            f1 = new flag(plugin, temp2, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.FENCE, team.DIAMOND, team.GOLD);
            f2 = new flag(plugin, temp, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.FENCE, team.GOLD, team.DIAMOND);
            swap = true;
            for (Player i : plugin.players.keySet()) {
                plugin.p(i).setStreaks();
                spawnPlayer(i, true);
            }
        } else if (time > gamelength) {
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
    public void playerpickup(PlayerPickupItemEvent event, Material pickedup) {
        if (pickedup == Material.DIAMOND_BLOCK) {
            event.setCancelled(true);
            event.getItem().remove();
            if (plugin.p(event.getPlayer()).getTeam() == team.DIAMOND) {
                f2.takeFlag(event.getPlayer());
            } else {
                f2.returnFlag(null, false);
            }
        } else if (pickedup == Material.GOLD_BLOCK) {
            event.setCancelled(true);
            event.getItem().remove();
            if (plugin.p(event.getPlayer()).getTeam() == team.GOLD) {
                f1.takeFlag(event.getPlayer());
            } else {
                f1.returnFlag(null, false);
            }
        }
    }
    
    @Override
    public void playermove(PlayerMoveEvent event) {
        if (event.getTo().distance(f1.l) < 2) {
            if (plugin.p(event.getPlayer()).getTeam() != f1.t) {
                f1.takeFlag(event.getPlayer());
                plugin.p(event.getPlayer()).addPoints(2);
            } else if (event.getPlayer() == f2.p) {
                onCap(f2, f1, event.getPlayer());
            }
        } else if (event.getTo().distance(f2.l) < 2) {
            if (plugin.p(event.getPlayer()).getTeam() != f2.t) {
                f2.takeFlag(event.getPlayer());
                plugin.p(event.getPlayer()).addPoints(2);
            } else if (event.getPlayer() == f1.p) {
                onCap(f1, f2, event.getPlayer());
            }
        }
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand().getType() == Material.BOW) {
                if (f1.p == event.getPlayer()) {
                    f1.everdropped = true;
                } else if (f2.p == event.getPlayer()) {
                    f2.everdropped = true;
                }
            }
        }
    }
    
    public void onCap(flag a, flag b, Player p) {
        if (!a.everdropped) {
            plugin.p(p).s.awardAchievement(Achievement.NO_HITTER);
        }
        a.returnFlag(plugin.p(p).getTeam() == team.DIAMOND ? Material.DIAMOND_HELMET : Material.GOLD_HELMET, true);
        if (b.t == team.GOLD) {
            gold++;
        } else {
            diam++;
        }
        if (scorelimit < 0) {
            
        } else if (gold > scorelimit) {
            onWin(team.GOLD, null, null);
        } else if (diam > scorelimit) {
            onWin(team.DIAMOND, null, null);
        }
        int score = scores.containsKey(plugin.p(p)) ? scores.get(plugin.p(p)) : 0;
        scores.put(plugin.p(p), score + 1);
        plugin.p(p).addPoints(7);
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
                team t = _p.getTeam();
                if (swap == true) {
                    if (t == team.DIAMOND) {
                        t = team.GOLD;
                    } else {
                        t = team.DIAMOND;
                    }
                }
                switch (t) {
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
            p.sendMessage(plugin.d + "6Gold: " + gold + plugin.d + "f  " + plugin.d + "bDiamond: " + diam);
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
            p.sendMessage(plugin.d + c + (t == team.DIAMOND ? diam : gold));
        }
    }
    
    @Override
    public player getTopPlayer(team t) {
        player out = null;
        int mcaps = -1;
        for (player i : plugin.players.values()) {
            if (i.getTeam() == t) {
                if (scores.containsKey(i) && scores.get(i) > mcaps) {
                    out = i;
                    mcaps = scores.get(i);
                }
            }
        }
        return out;
    }

}