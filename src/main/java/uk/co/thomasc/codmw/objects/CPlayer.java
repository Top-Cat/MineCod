package uk.co.thomasc.codmw.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.Spout;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.SpoutServer;
import org.getspout.spoutapi.plugin.SpoutPlugin;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.killstreaks.Killstreaks;
import uk.co.thomasc.codmw.perks.Perks;
import uk.co.thomasc.codmw.perks.Tiers;
import uk.co.thomasc.codmw.perks.Perk;
import uk.co.thomasc.codmw.sql.Achievement;
import uk.co.thomasc.codmw.sql.Stat;
import uk.co.thomasc.codmw.sql.Stats;

public class CPlayer {
	
	public int kill, streak, death, assists, points;
	public HashMap<Killstreaks, Integer> last = new HashMap<Killstreaks, Integer>();
	public HashMap<Tiers, Perk> perks = new HashMap<Tiers, Perk>(); 
	private final Main plugin;
	public Player p;
	public String nick;
	public int dbid;
	private Team t;
	public Stats s;
	public int h = 20;
	public long htime = 0;
	public long stime = 0;
	public long rtime = 0;
	public int todrop = 0;
	public int regens = 0;
	public Location dropl;
	public Player assist;
	public Player lastkiller;
	public long spawn = System.currentTimeMillis();
	public long tospawn = 0;
	public long time_todrop = 0;
	public boolean dropped = true;
	CPlayer lastk;
	int lastk_count = 0, lastk_top_count = 0, hshot_streak = 0, melee_streak = 0;
	public int aimbot = 0;
	long laststreak = 0, lastkill = 0;
	public boolean premium, fish = false;
	public List<Killstreaks> yks = new ArrayList<Killstreaks>();
	public boolean dead = false, regen = false;
	
	public CPlayer(Main instance, Player _p, Team _t) {
		plugin = instance;
		p = _p;
		t = _t;
		plugin.players.put(_p, this);
		
		ResultSet r = plugin.sql.query("SELECT * FROM cod_players WHERE username = '" + _p.getDisplayName() + "'");
		try {
			r.next();
			nick = r.getString("nick");
			dbid = r.getInt("Id");
			s = new Stats(plugin, this);
			premium = r.getBoolean("premium");
			if (premium) {
				fish = r.getBoolean("fish");
			}
			for (String i : r.getString("killstreaks").split(",")) {
				Killstreaks s = Killstreaks.valueOf(Integer.parseInt(i));
				if (yks.size() < 3 && !yks.contains(s)) { 
					yks.add(s);
				}
			}
			for (String i : r.getString("perks").split(",")) {
				if (i.length() > 0) {
					Perks s = Perks.valueOf(Integer.parseInt(i));
					perks.put(s.getTier(), s.create(plugin, this));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		s.incStat(Stat.LOGIN);
		
		switch(t) {
			case GOLD: plugin.gold++; break;
			case DIAMOND: plugin.diam++; break;
		}
		setinv(false);
		plugin.tot++;
		
		if (plugin.activeGame == true) {
			dead = true;
		}
		
		p.teleport(plugin.prespawn);
	}
	
	public void destroy() {
		switch(t) {
			case GOLD:
				plugin.gold--;
				break;
			case DIAMOND:
				plugin.diam--;
				break;
		}
		plugin.tot--;
		plugin.setDoors();
		s.destroy();
	}
	
	public Team getTeam() {
		return t;
	}
	
	public void setTeam(Team _t) {
		t = _t;
	}
	
	public void resetScore() {
		kill = 0;
		death = 0;
		streak = 0;
		assists = 0;
		points = 0;
		regens = 0;
	}
	
	public void addPoints(int c) {
	points += c;
	s.incStat(Stat.POINTS, c);
	s.maxStat(Stat.MAX_POINTS, points);
	}
	
	int slot = 0;
	
	public int getStreak() {
		return streak + getVar("streakoffset", 0);
	}
	
	public void addStreak() {
		streak++;
		for (Killstreaks ks : yks) {
			if (ks.getKills() == getStreak()) {
				giveItem(slot++ + 3, new ItemStack(Material.getMaterial(ks.getMat().getRawId()), ks.getAmm()));
				s.incStat(ks.getStat());
				
				if ((System.currentTimeMillis() - laststreak) < 20000) {
					s.awardAchievement(Achievement.WARGASM);
				}
				laststreak = System.currentTimeMillis();
				
				break;
			}
		}
		s.maxStat(Stat.MAX_STREAK, streak);
	}
	
	@SuppressWarnings("deprecation")
	public void giveItem(int pslot, ItemStack s) {
		if (s.getAmount() > 0) {
			PlayerInventory i = p.getInventory();
			if (i.contains(s.getType()) || (i.getItem(pslot) != null && i.getItem(pslot).getType() != s.getType() && i.getItem(pslot).getType() != Material.AIR )) {
				i.addItem(s);
			} else {
				i.setItem(pslot, s);
			}
		}
		p.updateInventory();
	}
	
	private int getDistance(CPlayer p, Arrow l) {
		int out = 0;
		try {
			out = (int) p.p.getLocation().distance(plugin.game.floc.get(l));
		} catch (Exception e) { }
		return out;
	}
	
	public void onKill(CPlayer killed, Reason reason, Object l) {
		for (MineCodListener i : plugin.listeners) {
			i.onKill(this, killed, reason, l);
		}
		if (dead) {
			s.incStat(Stat.DEAD_KILLS);
		}
		if ((System.currentTimeMillis() - lastkill) < 5000) {
			s.awardAchievement(Achievement.RAPID_FIRE);
		}
		lastkill = System.currentTimeMillis();
		if (killed.p.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
			s.awardAchievement(Achievement.DEFYING_GRAVITY);
		}
		s.incStat(Stat.KILLS);
		kill++;
		s.maxStat(Stat.MAX_KILLS, kill);
		if (reason == Reason.BOW || reason == Reason.HEADSHOT) {
			s.maxStat(Stat.FURTHEST_KILL, getDistance(killed, (Arrow) l));
		}
		if (reason == Reason.HEADSHOT) {
			s.maxStat(Stat.FURTHEST_HEADSHOT, getDistance(killed, (Arrow) l));
		}
		if (plugin.game.floc.containsKey(l)) {
			plugin.game.floc.remove(l);
		}
		if (killed.p.getDisplayName().equalsIgnoreCase("Gigthank")) {
			s.awardAchievement(Achievement.KILL_GIG);
		} else if (killed.p.getDisplayName().equalsIgnoreCase("Notch")) {
			s.awardAchievement(Achievement.KILL_NOTCH);
		}
		if (reason.getStreak() || getVar("allstreak", 0) == 1) {
			addStreak();
		}
		
		int ammo = 0;
		for (ItemStack i : p.getInventory().getContents()) {
			if (i != null) {
				if (i.getType() == Material.FEATHER || i.getType() == Material.ARROW) {
					ammo += i.getAmount();
				}
			}
		}
		
		if (ammo == 0 && reason == Reason.KNIFE) {
			s.awardAchievement(Achievement.LAST_RESORT);
		}
		if (killed.getStreak() == 10) {
			s.awardAchievement(Achievement.CLOSE_CHOPPER);
		}
		if (p.getFireTicks() > 0) {
			s.awardAchievement(Achievement.FIREARMS);
		}
		
		if (killed == lastk) {
			lastk_count++;
			if (lastk_count >= 3) {
				s.awardAchievement(Achievement.NEMESIS);
			}
		} else {
			lastk = killed;
			lastk_count = 1;
		}
		
		if (killed == plugin.game.getTopPlayer(t == Team.GOLD ? Team.DIAMOND : Team.GOLD)) {
			lastk_top_count++;
			if (lastk_count >= 3) {
				s.awardAchievement(Achievement.FALL_HARD);
			}
		} else {
			lastk_top_count = 0;
		}
		if (reason == Reason.HEADSHOT) {
			hshot_streak++;
			if (hshot_streak > 3) {
				s.awardAchievement(Achievement.HOTSHOT);
			}
		} else {
			hshot_streak = 0;
		}
		if (reason == Reason.KNIFE) {
			melee_streak++;
			if (melee_streak > 3) {
				s.awardAchievement(Achievement.COMMANDO);
			}
		} else {
			melee_streak = 0;
		}
	}
	
	public double getVar(String name, double def) {
		for (MineCodListener i : plugin.listeners) {
			def = i.getVar(this, name, def);
		}
		return def;
	}
	
	public int getVar(String name, int def) {
		return (int) getVar(name, (double) def);
	}
	
	public boolean incHealth(int _h, Player attacker, Reason r, Object ks) {
		for (MineCodListener i : plugin.listeners) {
			_h = i.onDamage(_h, attacker, p, r, ks);
		}
		if (_h < 0 && !regen) {
			regen = true;
			regens++;
			s.maxStat(Stat.LIFE_REGENS, regens);
		}
		if ((plugin.game.melee == 1 && (r == Reason.BOW || r == Reason.HEADSHOT || r == Reason.GRENADE)) || (plugin.game.melee == 2 && (r == Reason.KNIFE || r == Reason.FISH))) {
			_h = _h > 0 ? 0 : _h;
		}
		if (_h > 0 && (r == Reason.BOW || r == Reason.HEADSHOT)) {
			plugin.p(attacker).aimbot++;
			if (plugin.p(attacker).aimbot >= 15) {
				plugin.p(attacker).s.awardAchievement(Achievement.AIMBOT);
			}
		}
		h -= _h;
		if (h > 20) { h = 20; }
		if (_h > 0) {
			regen = false;
			htime = System.currentTimeMillis() + getVar("healwait", 10000);
			stime = System.currentTimeMillis() + 5000;
		}
		if (r == Reason.FALL) {
			s.incStat(Stat.FALL_DAMAGE, _h);
		}
		if (h <= 0) {
			regens = 0;
			lastk_count = 0;
			lastk_top_count = 0;
			hshot_streak = 0;
			melee_streak = 0;
			aimbot = 0;
			tospawn = System.currentTimeMillis() + (plugin.game.respawntime * 1000);
			time_todrop = System.currentTimeMillis() + 500;
			dropped = false;
			lastkiller = attacker;
			CPlayer a = plugin.p(attacker);
			if (a.getTeam() != getTeam() || a.getTeam() == Team.BOTH) {
				a.onKill(this, r, ks);
				switch (r) {
				case KNIFE: plugin.p(attacker).s.incStat(Stat.KNIFE_KILLS); break;
				case BOW: plugin.p(attacker).s.incStat(Stat.BOW_KILLS); break;
				case CLAYMORE: plugin.p(attacker).s.incStat(Stat.CLAYMORE_KILLS); break;
				case DOGS: plugin.p(attacker).s.incStat(Stat.DOG_KILLS); break;
				case SENTRY: plugin.p(attacker).s.incStat(Stat.SENTRY_KILLS); break;
				case CHOPPER: plugin.p(attacker).s.incStat(Stat.CHOPPER_KILLS); break;
				case HEADSHOT: plugin.p(attacker).s.incStat(Stat.HEADSHOTS); plugin.p(attacker).s.incStat(Stat.BOW_KILLS); break;
				case FISH: plugin.p(attacker).s.incStat(Stat.FISH_KILLS); break;
				case GRENADE: plugin.p(attacker).s.incStat(Stat.GRENADE_KILLS); break;
			}
			} else {
				kill--;
			}
			String assist_txt = "";
			if (assist != null && assist != attacker) {
				assist_txt = ChatColor.RED + " (Assist: " + plugin.p(assist).getTeam().getColour() + plugin.p(assist).nick + ChatColor.RED + ")";
				plugin.p(assist).assists++;
				plugin.p(assist).s.incStat(Stat.ASSISTS);
				plugin.p(assist).addPoints(2);
			}
			s.incStat(Stat.DEATHS);
			death++;
			s.maxStat(Stat.MAX_DEATHS, death);
			streak = 0;
			
			h = 20;
			
			int ammo = 0;
			for (ItemStack i : p.getInventory().getContents()) {
				if (i != null) {
					if (i.getType() == Material.FEATHER || i.getType() == Material.ARROW) {
						ammo += i.getAmount();
					}
				}
			}
			ammo = (int) (ammo / 15);
			setStreaks();
			
			String desc = "";
			String as = "";
			switch (r) {
				case FALL: plugin.game.sendMessage(Team.BOTH, ChatColor.RED + plugin.p(p).nick + " fell to his death. LOL!" + assist_txt); plugin.p(p).s.incStat(Stat.FALL_DEATHS); break;
				case KNIFE: desc = " knifed"; plugin.p(p).s.incStat(Stat.KNIFE_DEATHS); break;
				case BOW: desc = " shot"; plugin.p(p).s.incStat(Stat.BOW_DEATHS); break;
				case CLAYMORE: desc = " claymored"; plugin.p(p).s.incStat(Stat.CLAYMORE_DEATHS); break;
				case DOGS: as = "'s"; desc = " dogs mauled"; plugin.p(p).s.incStat(Stat.DOG_DEATHS); break;
				case SENTRY: as = "'s"; desc = " sentry shot"; plugin.p(p).s.incStat(Stat.SENTRY_DEATHS); break;
				case CHOPPER: as = "'s"; desc = " chopper battered"; plugin.p(p).s.incStat(Stat.CHOPPER_DEATHS); break;
				case HEADSHOT: desc = " headshot"; plugin.p(p).s.incStat(Stat.BOW_DEATHS); break;
				case FISH_SMITE: plugin.game.sendMessage(Team.BOTH, ChatColor.RED + plugin.p(p).nick + " was smited for using a premium fish!" + assist_txt); break;
				case FISH: desc = " got a FISH KILL on"; plugin.p(p).s.incStat(Stat.FISH_DEATHS); break;
				case GRENADE: desc = " grenaded"; plugin.p(p).s.incStat(Stat.GRENADE_DEATHS); break;
			}
			if (r != Reason.FALL && r != Reason.FISH_SMITE) {
				plugin.game.sendMessage(Team.BOTH, plugin.p(attacker).getTeam().getColour() + plugin.p(attacker).nick + as + ChatColor.RED + desc + " " + t.getColour() + nick + assist_txt);
			}
			if (ks != null && ks instanceof Ownable) {
				((Ownable) ks).incKills();
			}
			clearinv();
			todrop += ammo;
			dropl = p.getLocation();
			
			List<Block> bs = p.getLineOfSight(null, 3);
			for (Block i : bs) {
				if (i.getType() != Material.AIR) {
					if (i.getType() == Material.BOOKSHELF) {
						s.awardAchievement(Achievement.READINGABOOK);
					}
					break;
				}
			}
			
			Location l = p.getLocation();
			p.teleport(plugin.prespawn);
			plugin.game.onKill(plugin.p(attacker), this, l, r);
			dead = true;
			slot = 0;
		}
		if (_h > 0) {
			assist = attacker;
		}
		p.setHealth(h);
		return dead;
	}
	
	public void setStreaks() {
		last = new HashMap<Killstreaks, Integer>();
		for (ItemStack i : p.getInventory().getContents()) {
			if (i != null) {
				Killstreaks s = Killstreaks.fromMaterial(i.getType().getId());
				if (s != null) {
					int c = last.containsKey(s) ? last.get(s) : 0;
					last.put(s, c + i.getAmount());
				}
			}
		}
	}
	
	public void setinv() {
		setinv(true);
	}
	
	public void setinv(boolean weapons) {
		SpoutManager.getPlayer(p).setTitle(t.getColour() + nick);
		if (t == Team.GOLD) {
			p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1));
			SpoutManager.getPlayer(p).setCape("http://www.thorgaming.com/minecraft/redteamcape.png");
		} else {
			p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1));
			SpoutManager.getPlayer(p).setCape("http://www.thorgaming.com/minecraft/blueteamcape.png");
		}
		if (weapons) {
			SpawnItems spawninv = new SpawnItems();
			spawninv.setItem(Material.BOW, 1, 0);
			spawninv.setItem(Material.ARROW, 15, 8);
			spawninv.setItem(Material.FEATHER,75, 7);
			if (fish) {
				spawninv.setItem(Material.RAW_FISH, 1, 1);
			} else {
				spawninv.setItem(Material.IRON_SWORD, 1, 1);
			}
			spawninv.setItem(Material.SNOW_BALL, 2, 2);
		
			for (MineCodListener i : plugin.listeners) {
				i.onRespawn(this, spawninv);
			}
			
			if (plugin.game.melee == 1) {
				spawninv.removeItem(Material.BOW);
				spawninv.removeItem(Material.ARROW);
				spawninv.removeItem(Material.FEATHER);
			} else if (plugin.game.melee == 2) {
				spawninv.removeItem(Material.RAW_FISH);
				spawninv.removeItem(Material.IRON_SWORD);
			}
			if (plugin.game.melee != 0) {
				spawninv.removeItem(Material.SNOW_BALL);
			}
			
			spawninv.give(this);
		}
	}
	
	public void clearinv() {
		plugin.clearinv(p);
	}
	
}