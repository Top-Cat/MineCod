package uk.co.thomasc.codmw.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.Team;
import uk.co.thomasc.codmw.killstreaks.Killstreaks;
import uk.co.thomasc.codmw.killstreaks.useable.Useable;
import uk.co.thomasc.codmw.objects.MineCodListener;
import uk.co.thomasc.codmw.objects.Grenade;
import uk.co.thomasc.codmw.objects.CPlayer;
import uk.co.thomasc.codmw.sql.Stat;

public class CODPlayerListener implements Listener {
	
	private Main plugin;
	public ArrayList<Material> allowed_pickup = new ArrayList<Material>();
	private Random generator = new Random();
	
	public CODPlayerListener(Main instance) {
		plugin = instance;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		allowed_pickup.add(Material.FEATHER);
		allowed_pickup.add(Material.DIAMOND_BLOCK);
		allowed_pickup.add(Material.GOLD_BLOCK);
		allowed_pickup.add(Material.RAW_FISH);
		allowed_pickup.add(Material.SULPHUR);
		allowed_pickup.addAll(Killstreaks.table2.keySet());
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		CraftEntity item = (CraftEntity)event.getItemDrop();
		int itemId = ((EntityItem)item.getHandle()).itemStack.id;
		if (!allowed_pickup.contains(Material.getMaterial(itemId))) {
			event.setCancelled(true);
		} else {
			CPlayer p = plugin.p(event.getPlayer());
			if (p != null) {
				p.s.incStat(Stat.ITEMS_THROWN, event.getItemDrop().getItemStack().getAmount());
			}
		}
	}
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage("no-z-fly no-z-cheat");
		plugin.game.playerjoin(event);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.game.playerquit(event);
		if (plugin.players.containsKey(event.getPlayer())) {
			plugin.p(event.getPlayer()).destroy();
			plugin.players.remove(event.getPlayer());
			
			int d = (int) Math.floor(Math.abs(plugin.diam - plugin.gold) / 2);
			if (d > 0) {
				nextbalance = System.currentTimeMillis() + 10000;
			}
		}
		for (CPlayer i : plugin.players.values()) {
			if (i.assist == event.getPlayer()) {
				i.assist = null;
			}
		}
	}
	
	public long nextbalance = 0;
	
	Block lastB;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		plugin.game.playermove(event);
		Location t = event.getTo();
		for (MineCodListener i : plugin.listeners) {
			i.onMove(event, t.getBlock() != lastB);
		}
		if (t.getBlock() != lastB) {
			lastB = t.getBlock();
			Team e;
			
			CPlayer p = plugin.p(event.getPlayer());
			if (p != null) {
				p.s.incStat(Stat.BLOCKS_MOVED);
			}
			
			if (t.getX() > -10 && t.getX() < -8 && t.getZ() > 14 && t.getZ() < 16 && t.getBlockY() == 64) {
				e = Team.GOLD;
			} else if (t.getX() > -10 && t.getX() < -8 && t.getZ() > 10 && t.getZ() < 12 && t.getBlockY() == 64) {
				e = Team.DIAMOND;
			} else if (t.getX() > -8 && t.getX() < -6 && t.getZ() > 12 && t.getZ() < 14 && t.getBlockY() == 64) {
				if (plugin.diam > plugin.gold) {
					e = Team.GOLD;
				} else if (plugin.gold > plugin.diam) {
					e = Team.DIAMOND;
				} else if (generator.nextInt(2) > 0) {
					e = Team.DIAMOND;
				} else {
					e = Team.GOLD;
				}
			} else {
				return;
			}
			if (!plugin.players.containsKey(event.getPlayer())) {
				new CPlayer(plugin, event.getPlayer(), e);
			} else {
				plugin.players.get(event.getPlayer()).setTeam(e);
				if (plugin.activeGame) {
					plugin.game.spawnTele(plugin.p(event.getPlayer()), event.getPlayer(), false);
				} else {
					plugin.players.get(event.getPlayer()).dead = false;
				}
				recount();
			}
			plugin.setDoors();
		}
	}
	
	public void recount() {
		plugin.diam = 0;
		plugin.gold = 0;
		plugin.tot = plugin.players.size();
		for (CPlayer i : plugin.players.values()) {
			if (i.getTeam() == Team.DIAMOND) {
				plugin.diam++;
			} else {
				plugin.gold++;
			}
		}
	}
	
	private int inv_count(Inventory in, List<Material> m) {
		int out = 0;
		for (ItemStack i : in.getContents()) {
			if (i != null) {
				if (m.contains(i.getType())) {
					out += i.getAmount();
				}
			}
		}
		return out;
	}
	
	private int inv_count(Inventory in, Material m) {
		return inv_count(in, Arrays.asList(m));
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		int itemId = event.getItem().getItemStack().getType().getId();
		int amm = event.getItem().getItemStack().getAmount();
		Material um = Material.getMaterial(itemId);
		if (!allowed_pickup.contains(um)) {
			event.setCancelled(true);
		} else if (um == Material.FEATHER) {
			int ammo = inv_count(event.getPlayer().getInventory(), Material.FEATHER);
			event.getPlayer().getInventory().remove(Material.FEATHER);
			if (ammo < 99) {
				event.getItem().remove();
				amm += ammo;
			} else {
				amm = ammo;
			}
			if (amm > 99) { amm = 99; }
			if (plugin.players.containsKey(event.getPlayer())) {
				plugin.p(event.getPlayer()).s.incStat(Stat.AMMO_PICKED_UP, amm - ammo);
			}
			PlayerInventory i = event.getPlayer().getInventory();
			if (i.getItem(7) != null && i.getItem(7).getType() != Material.FEATHER && i.getItem(7).getType() != Material.AIR) {
				i.addItem(new ItemStack(Material.FEATHER, amm));
			} else {
				i.setItem(7, new ItemStack(Material.FEATHER, amm));
			}
			event.setCancelled(true);
		}
		plugin.game.playerpickup(event, Material.getMaterial(itemId));
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		if (plugin.players.containsKey(event.getPlayer())) {
			event.setCancelled(true);
			for (Player i : plugin.getServer().getOnlinePlayers()) {
				i.sendMessage("<" + plugin.p(event.getPlayer()).nick + "> " + plugin.p(event.getPlayer()).getTeam().getColour() + event.getMessage());
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		event.setUseInteractedBlock(Result.DENY);
		event.setUseItemInHand(Result.ALLOW);
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Killstreaks s = Killstreaks.fromMaterial(event.getPlayer().getItemInHand().getType().getId());
			if (s != null && Useable.class.isAssignableFrom(s.getkClass())) {
				event.setUseItemInHand(Result.DENY);
				event.getPlayer().getInventory().removeItem(new ItemStack(event.getPlayer().getItemInHand().getType(), 1));
				s.callIn(plugin, event.getPlayer(), new Object[] {});
			} else if (event.getPlayer().getItemInHand().getType() == Material.BOW) {
				if (plugin.p(event.getPlayer()).rtime < System.currentTimeMillis()) {
					plugin.p(event.getPlayer()).stime = System.currentTimeMillis() + 3000;
					if (event.getPlayer().getInventory().contains(Material.ARROW)) {
						plugin.p(event.getPlayer()).s.incStat(Stat.ARROWS_FIRED);
					}
				} else {
					event.setUseItemInHand(Result.DENY);
					event.getPlayer().updateInventory();
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.SNOW_BALL) {
				new Grenade(plugin, event.getPlayer()).getOwnerplayer().s.incStat(Stat.GRENADES_THROWN);
				event.getPlayer().getInventory().removeItem(new ItemStack(Material.SNOW_BALL, 1));
				event.setUseItemInHand(Result.DENY);
			} else if (event.getPlayer().getItemInHand().getType() == Material.RAW_FISH) {
				event.setUseItemInHand(Result.DENY);
			}
		}
		for (MineCodListener i : (ArrayList<MineCodListener>) plugin.listeners.clone()) {
			i.onInteract(event);
		}
		plugin.game.onInteract(event);
	}
	
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		event.setCancelled(true);
	}
}