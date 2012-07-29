package uk.co.thomasc.codmw.gamemodes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import net.minecraft.server.EntityItem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.SpoutServer;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.killstreaks.Killstreaks;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.Reason;
import uk.co.thomasc.codmw.objects.Grenade;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Achievement;
import uk.co.thomasc.codmw.sql.Stat;

public class Gamemode {
	
	protected Main plugin;
	protected ArrayList<ArrayList<Location>> spawns = new ArrayList<ArrayList<Location>>();
	public HashMap<Arrow, Location> ploc = new HashMap<Arrow, Location>();
	public HashMap<Arrow, Location> floc = new HashMap<Arrow, Location>();
	protected ArrayList<String> lossmesssages = new ArrayList<String>();
	protected ArrayList<String> winmesssages = new ArrayList<String>();
	public Random generator = new Random();
	private Location d1, d2, d3, d4;
	private int t1, t2, t3, t4;
	private boolean dl = false;
	public int time = 0;
	protected int gamelength = 600;
	protected int scorelimit = 0;
	public int melee = 0;
	public int respawntime = 0;
	private int waittime = 0;
	protected boolean ff = false;
	
	public Gamemode(Main instance) {
		plugin = instance;
		
		setup();
		t4 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new tele(), 4L, 4L);
		
		lossmesssages.add("They have won the battle but not the war!");
		lossmesssages.add("We have made a lot of mistakes you are going to regret");
		lossmesssages.add("God. You're such Gigs");
		winmesssages.add("Good job seals");
		winmesssages.add("You will receive cake for your victory here!");
		
		gamelength = plugin.getVarValue("gamelength", 600);
		melee = plugin.getVarValue("melee", 0);
		respawntime = plugin.getVarValue("respawntime", 0);
		waittime = plugin.getVarValue("waittime", 55);
		ff = plugin.getVarValue("friendlyfire", 0) == 1;
		scheduleGame();
	}
	
	public void sendMessage(Team team, String s, Player exclude) {
		for (Player p : plugin.players.keySet()) {
			if ((team == Team.BOTH || team == plugin.p(p).getTeam()) && p != exclude) {
				p.sendMessage(s);
			}
		}
	}
	
	public void sendMessage(Team t, String s) {
		sendMessage(t, s, null);
	}
	
	public class tick implements Runnable {
		@Override
		public void run() { try { tick(); } catch (Exception e) { e.printStackTrace(); } }
	}
	
	public class tickone implements Runnable {
		@Override
		public void run() { time++; }
	}
	
	public class tickfast implements Runnable {
		@Override
		public void run() { try { tickfast(); } catch (Exception e) { e.printStackTrace(); } }
	}
	
	public boolean spawnCheck(Location i) {
		return ((d1 == null || i.distance(d1) > 7) && (d2 == null || i.distance(d2) > 7) && (d3 == null || i.distance(d3) > 7) && (d4 == null || i.distance(d4) > 7));
	}
	
	public void scheduleGame() {
		t1 = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new startgame(), waittime * 20);
	}

	public class startgame extends TimerTask {

		@Override
		public void run() {
			beginGame();
		}

	}
	
	public void beginGame() {
		if (plugin.activeGame == false) {
			if (plugin.players.size() >= plugin.minplayers) {
				sendMessage(Team.BOTH, ChatColor.BLUE + "Game starting in 5 seconds!");
				plugin.r.countdowns();
				plugin.activeGame = true;
			} else {
				scheduleGame();
			}
		}
	}
	
	// BELOW ARE INTERFACES
	
	public void setup() {};
	
	public void spawnPlayer(Player p, boolean start) {
		CPlayer _p = plugin.p(p);
		_p.spawn = System.currentTimeMillis();
		_p.clearinv();
		_p.setinv();
		
		if (!start) {
			int slot = 3;
			for (Killstreaks s : _p.last.keySet()) {
				_p.giveItem(slot++, new ItemStack(Material.getMaterial(s.getMat().getRawId()), _p.last.get(s)));
			}
		}
		
		spawnTele(_p, p, start);
		_p.dead = false;
	}
	
	public Location spawnTele(CPlayer _p, Player p, boolean start) { return null; };
	
	public void startGame() {
		sendMessage(Team.BOTH, ChatColor.BLUE + "Game starting now!");
		for (Player p : plugin.players.keySet()) {
			plugin.p(p).resetScore();
			spawnPlayer(p, true);
		}
		t1 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new tick(), 40L, 40L);
		t2 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new tickone(), 20L, 20L);
		t3 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new tickfast(), 2L, 2L);
	}
	
	public void onWin(Team winners, CPlayer lastkill, CPlayer lastdeath) {
		destroy();
		
		if (plugin.uids.containsKey(plugin.currentWorld.getUID().toString())) {
			for (CPlayer i : plugin.players.values()) {
				if (((i.s.getStat(Stat.MAPS_PLAYED) >> plugin.uids.get(plugin.currentWorld.getUID().toString())) % 2) == 0) {
					i.s.incStat(Stat.MAPS_PLAYED, 1 << plugin.uids.get(plugin.currentWorld.getUID().toString()));
				}
			}
		}
		
		plugin.loadmap();
		
		if (lastdeath != null) {
			lastdeath.p.getInventory().setHelmet(new ItemStack(Material.WOOD, 1));
			lastdeath.s.incStat(Stat.LAST_DEATH);
		}
		if (lastkill != null) {
			lastkill.s.incStat(Stat.LAST_KILL);
		}
		
		sendMessage(Team.BOTH, ChatColor.BLUE + "Game ended, game will resume on '" + plugin.gm.toString().toLowerCase() + "_" + plugin.currentMap.name + "' in " + (waittime + 5) + " seconds");
	}
	
	public void onInteract(PlayerInteractEvent event) {
		
	}
	
	public void onKill(CPlayer attacker, CPlayer defender, Location l, Reason r) {
		if (dl) {
			d1 = l;
			d3 = attacker.p.getLocation();
		} else {
			d2 = l;
			d4 = attacker.p.getLocation();
		}
		dl = !dl;	
	}
	
	@SuppressWarnings("unchecked")
	public void destroy() {
		plugin.getServer().getScheduler().cancelTask(t1);
		plugin.getServer().getScheduler().cancelTask(t2);
		plugin.getServer().getScheduler().cancelTask(t3);
		plugin.getServer().getScheduler().cancelTask(t4);
		try {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				SpoutManager.getPlayer(player).resetCape();
			}
		} catch (NoClassDefFoundError e) {
			//This happens on server stop
		}
		for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
			i.destroy();
		}
		plugin.listeners.clear();
		plugin.activeGame = false;
	}
	
	public void afterDeath(CPlayer p) {
		if (p.todrop > 0) {
			int td = p.getVar("todrop", p.todrop);
			if (td > 0) {
				plugin.currentWorld.dropItem(p.dropl, new ItemStack(Material.FEATHER, td));
			}
			p.todrop = 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void tick() {
		for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
			i.tick();
		}
		if (plugin.playerListener.nextbalance < System.currentTimeMillis()) {
			int d = (int) Math.floor(Math.abs(plugin.diam - plugin.gold) / 2);
			Team b = Team.DIAMOND;
			if (plugin.gold > plugin.diam) {
				b = Team.GOLD;
			}
			
			if (d > 0) {
				plugin.game.sendMessage(Team.BOTH, "Balancing teams...");
				ArrayList<CPlayer> bigteam = new ArrayList<CPlayer>();
				for (CPlayer i : plugin.players.values()) {
					if (i.getTeam() == b) {
						bigteam.add(i);
					}
				}
				for (int i = 0; i < d; i++) {
					plugin.switchplayer(bigteam.remove(generator.nextInt(bigteam.size())).p);
				}
			}
		}
		for (CPlayer p : plugin.players.values()) {
			int life = (int) (System.currentTimeMillis() - p.spawn);
			if (life > 300000) {
				p.s.awardAchievement(Achievement.BEAR_GRYLLS);
			} else if (life > 180000) {
				p.s.awardAchievement(Achievement.CAMPER);
			} else if (life > 120000) {
				p.s.awardAchievement(Achievement.HIDING);
			}
			if (p.htime < System.currentTimeMillis() && p.h < 20) {
				p.incHealth(-3, null, Reason.NONE, null);
			}
			p.p.setHealth(p.h);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void tickfast() {
		for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
			i.tickfast();
		}
		for (Grenade i : (ArrayList<Grenade>) plugin.g.clone()) {
			i.explode();
		}
		for (CPlayer p : plugin.players.values()) {
			if (!p.dropped && p.time_todrop < System.currentTimeMillis()) {
				afterDeath(p);
				for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
					i.afterDeath(p);
				}
				p.dropped = true;
			}
			if (p.dead && p.tospawn < System.currentTimeMillis()) {
				spawnPlayer(p.p, false);
			}
		}
		List<Entity> r2 = new ArrayList<Entity>();
		for (Entity i : plugin.currentWorld.getEntities()) {
			if (i instanceof Arrow) {
				Location l = i.getLocation();
				if (!floc.containsKey(i)) {
					floc.put((Arrow) i, i.getLocation());
				}
				if (ploc.containsKey(i)) {
					if (l.distance(ploc.get(i)) < 0.1) {
						r2.add(i);
						floc.remove(i);
						plugin.p((Player) ((Arrow) i).getShooter()).aimbot = 0;
					}
				}
				ploc.put((Arrow) i, l);
			} else if (i instanceof Item) {
				int itemId = ((EntityItem)((CraftEntity)i).getHandle()).itemStack.id;
				if (!plugin.playerListener.allowed_pickup.contains(Material.getMaterial(itemId)) && !Killstreaks.table2.keySet().contains(Material.getMaterial(itemId)) && itemId != 332) {
					r2.add(i);
				}
			} else if (i instanceof Creature && !(i instanceof Wolf)) {
				r2.add(i);
			}
			for (Entity j : r2) {
				j.remove();
			}
		}
	}
	
	public void printScore(Player p, Team t) {};
	
	public void playermove(PlayerMoveEvent event) {};
	
	public void playerpickup(PlayerPickupItemEvent event, Material pickedup) {};
	
	public class tele extends TimerTask {
		
		@Override
		public void run() {
			for (Player i : plugin.totele) {
				jointele(i);
			}
			plugin.totele.clear();
		}
		
	}
	
	public void playerquit(PlayerQuitEvent event) {
		String nick = event.getPlayer().getDisplayName();
		CPlayer p = plugin.p(event.getPlayer());
		if (p != null) {
			nick = p.nick;
		}
		event.setQuitMessage(ChatColor.BLUE + plugin.leave_msg.replaceAll("\\$nick", nick));
	}
	
	public void playerjoin(PlayerJoinEvent event) {
		plugin.setDoors();
		plugin.totele.add(event.getPlayer());
		plugin.clearinv(event.getPlayer());
		String nick = event.getPlayer().getDisplayName();
		ResultSet r = plugin.sql.query("SELECT * FROM cod_players WHERE username = '" + event.getPlayer().getDisplayName() + "'");
		try {
			if (r.first()) {
				nick = r.getString("nick");
			} else {
				int id = plugin.sql.update("INSERT INTO cod_players (username, nick) VALUES ('" + event.getPlayer().getDisplayName() + "', '" + event.getPlayer().getDisplayName() + "')");
				plugin.sql.update("INSERT INTO cod_stats VALUES ('" + id + "', '0', '1000')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		event.setJoinMessage(ChatColor.BLUE + plugin.join_msg.replaceAll("\\$nick", nick));
		event.getPlayer().sendMessage(ChatColor.BLUE + plugin.welcome_msg);
		event.getPlayer().setHealth(20);
	}
	
	public void jointele(Player p) {};
	
	public boolean canHit(LivingEntity a, LivingEntity d, boolean killstreak) {
		return canHit(a, d, killstreak, killstreak);
	}
	
	public boolean canHit(LivingEntity a, LivingEntity d, boolean killstreak, boolean ignoreff) { return false; };
	
	public String getClaymoreText(Player p) { return "^^ CLAYMORE ^^"; };
	
	public CPlayer getTopPlayer(Team t) { return null; }
	
}