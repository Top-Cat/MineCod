package uk.co.thomasc.codmw.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Achievement;
import uk.co.thomasc.codmw.sql.Stat;

import uk.co.thomasc.codmw.gamemodes.ectf.Flag;

public class CTF extends TeamGM {

	int diam, gold;
	Flag f1, f2;
	boolean swap = false;
	HashMap<CPlayer, Integer> scores = new HashMap<CPlayer, Integer>();
	
	public CTF(Main instance) {
		super(instance);
		scorelimit = plugin.getVarValue("scorelimit", -1);
	}
	
	@Override
	public void setup() {
		spawns.add(0, plugin.currentMap.getSpawns(0));
		spawns.add(1, plugin.currentMap.getSpawns(1));
		spawns.add(2, plugin.currentMap.getSpawns(2));
		f1 = new Flag(plugin, plugin.currentMap.getSpawns(3).get(0), Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.FENCE, Team.DIAMOND, Team.GOLD);
		f2 = new Flag(plugin, plugin.currentMap.getSpawns(4).get(0), Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.FENCE, Team.GOLD, Team.DIAMOND);
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
	public void onKill(CPlayer attacker, CPlayer defender, Location l, Reason r) {
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
	public void afterDeath(CPlayer p) {
		super.afterDeath(p);
		if (f1.drop != null && plugin.p(f1.p) == p) {
			dropFlag(f1, Material.GOLD_BLOCK);
		} else if (f2.drop != null && plugin.p(f2.p) == p) {
			dropFlag(f2, Material.DIAMOND_BLOCK);
		}
	}
	
	public void dropFlag(Flag fa, Material a) {
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
			sendMessage(Team.BOTH, "Switching sides!");
			Location temp = f1.l;
			Location temp2 = f2.l;
			f1.destroy();
			f2.destroy();
			f1 = new Flag(plugin, temp2, Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.FENCE, Team.DIAMOND, Team.GOLD);
			f2 = new Flag(plugin, temp, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.FENCE, Team.GOLD, Team.DIAMOND);
			swap = true;
			for (Player i : plugin.players.keySet()) {
				plugin.p(i).setStreaks();
				spawnPlayer(i, true);
			}
		} else if (time > gamelength) {
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
	public void playerpickup(PlayerPickupItemEvent event, Material pickedup) {
		if (pickedup == Material.DIAMOND_BLOCK) {
			event.setCancelled(true);
			event.getItem().remove();
			if (plugin.p(event.getPlayer()).getTeam() == Team.DIAMOND) {
				f2.takeFlag(event.getPlayer());
			} else {
				f2.returnFlag(null, false);
			}
		} else if (pickedup == Material.GOLD_BLOCK) {
			event.setCancelled(true);
			event.getItem().remove();
			if (plugin.p(event.getPlayer()).getTeam() == Team.GOLD) {
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
	
	public void onCap(Flag a, Flag b, Player p) {
		if (!a.everdropped) {
			plugin.p(p).s.awardAchievement(Achievement.NO_HITTER);
		}
		a.returnFlag(plugin.p(p).getTeam() == Team.DIAMOND ? Material.DIAMOND_HELMET : Material.GOLD_HELMET, true);
		if (b.t == Team.GOLD) {
			gold++;
		} else {
			diam++;
		}
		if (scorelimit < 0) {
			
		} else if (gold > scorelimit) {
			onWin(Team.GOLD, null, null);
		} else if (diam > scorelimit) {
			onWin(Team.DIAMOND, null, null);
		}
		int score = scores.containsKey(plugin.p(p)) ? scores.get(plugin.p(p)) : 0;
		scores.put(plugin.p(p), score + 1);
		plugin.p(p).addPoints(7);
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
				Team t = _p.getTeam();
				if (swap == true) {
					if (t == Team.DIAMOND) {
						t = Team.GOLD;
					} else {
						t = Team.DIAMOND;
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
			p.sendMessage(_p.getTeam().getColour() + _p.getTeam().toString() + " team go!");
		}
		return g;
	}
	
	@Override
	public void printScore(Player p, Team t) {
		if (t == Team.BOTH) {
			p.sendMessage(ChatColor.GOLD + "Gold: " + gold + ChatColor.WHITE + "  " + ChatColor.AQUA + "Diamond: " + diam);
		} else if (t == Team.GOLD || t == Team.DIAMOND) {
			p.sendMessage(t.getColour() + "Player Name | Ki | A | D |");
			for (CPlayer i : plugin.players.values()) {
				if (i.getTeam() == t) {
					p.sendMessage(t.getColour() + i.nick + " | " + i.kill + " | " + i.assists + " | " + i.death + " |");
				}
			}
			p.sendMessage(t.getColour() + (t == Team.DIAMOND ? diam : gold));
		}
	}
	
	@Override
	public CPlayer getTopPlayer(Team t) {
		CPlayer out = null;
		int mcaps = -1;
		for (CPlayer i : plugin.players.values()) {
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