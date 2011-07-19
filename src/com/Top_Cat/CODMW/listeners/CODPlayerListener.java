package com.Top_Cat.CODMW.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import net.minecraft.server.EntityItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.CWolfPack;
import com.Top_Cat.CODMW.objects.chopper;
import com.Top_Cat.CODMW.objects.claymore;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.objects.sentry;
import com.Top_Cat.CODMW.sql.Stat;

public class CODPlayerListener extends PlayerListener {
    
    main plugin;
    public ArrayList<Material> allowed_pickup = new ArrayList<Material>();
    Random generator = new Random();
    
    public CODPlayerListener(main instance) {
        plugin = instance;
        allowed_pickup.add(Material.FEATHER);
        allowed_pickup.add(Material.WALL_SIGN);
        allowed_pickup.add(Material.APPLE);
        allowed_pickup.add(Material.BONE);
        allowed_pickup.add(Material.DISPENSER);
        allowed_pickup.add(Material.DIAMOND);
        allowed_pickup.add(Material.DIAMOND_BLOCK);
        allowed_pickup.add(Material.GOLD_BLOCK);
    }
    
    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        CraftEntity item = (CraftEntity)event.getItemDrop();
        int itemId = ((EntityItem)item.getHandle()).itemStack.id;
        if (!allowed_pickup.contains(Material.getMaterial(itemId))) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.game.playerjoin(event);
    }
    
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.players.containsKey(event.getPlayer())) {
            plugin.p(event.getPlayer()).destroy();
            plugin.players.remove(event.getPlayer());
            
            int d = (int) Math.floor(Math.abs(plugin.diam - plugin.gold) / 2);
            if (d > 0) {
                nextbalance = new Date().getTime() + 10000;
                
            }
        }
        for (player i : plugin.players.values()) {
            if (i.assist == event.getPlayer()) {
                i.assist = null;
            }
        }
    }
    
    long nextbalance = 0;
    
    public class balanceteams extends TimerTask {

        @Override
        public void run() {
            if (nextbalance < new Date().getTime()) {
                int d = (int) Math.floor(Math.abs(plugin.diam - plugin.gold) / 2);
                team b = team.DIAMOND;
                if (plugin.gold > plugin.diam) {
                    b = team.GOLD;
                }
                
                if (d > 0) {
                    plugin.game.sendMessage(team.BOTH, "Balancing teams...");
                    ArrayList<player> bigteam = new ArrayList<player>();
                    for (player i : plugin.players.values()) {
                        if (i.getTeam() == b) {
                            bigteam.add(i);
                        }
                    }
                    for (int i = 0; i < d; i++) {
                        plugin.switchplayer(bigteam.remove(generator.nextInt(bigteam.size())).p);
                    }
                }
            }
        }
        
    }
    
    Block lastB;

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        for (claymore i : plugin.clays) {
            if (i.exploded == false) {
                i.detect(event.getPlayer());
            }
        }
        if (event.getTo().getBlock().getRelative(0, -1, 0).getType() == Material.DISPENSER) {
            for (sentry i : plugin.sentries) {
                if (i.b == event.getTo().getBlock().getRelative(0, -2, 0)) {
                    event.setTo(plugin.game.spawnTele(plugin.p(event.getPlayer()), event.getPlayer(), false));
                    event.getPlayer().sendMessage(plugin.d + "bOnly Gigs stand on dispensers, you have been respawned!");
                }
            }
        }
        
        plugin.game.playermove(event);
        Location t = event.getTo();
        if (t.getBlock() != lastB) {
        	lastB = t.getBlock();
	        team e;
	        
	        player p = plugin.p(event.getPlayer());
	        if (p != null) {
	        	p.s.incStat(Stat.BLOCKS_MOVED);
	        }
	        
	        if (t.getX() > -10 && t.getX() < -8 && t.getZ() > 14 && t.getZ() < 16 && t.getBlockY() == 64) {
	            e = team.GOLD;
	        } else if (t.getX() > -10 && t.getX() < -8 && t.getZ() > 10 && t.getZ() < 12 && t.getBlockY() == 64) {
	            e = team.DIAMOND;
	        } else if (t.getX() > -8 && t.getX() < -6 && t.getZ() > 12 && t.getZ() < 14 && t.getBlockY() == 64) {
	            //Random team
	            if (plugin.diam > plugin.gold) {
	                e = team.GOLD;
	            } else if (plugin.gold > plugin.diam) {
	                e = team.DIAMOND;
	            } else if (generator.nextInt(2) > 0) {
	                e = team.DIAMOND;
	            } else {
	                e = team.GOLD;
	            }
	        } else {
	            return;
	        }
	        //Location l = plugin.prespawn;
	        if (!plugin.players.containsKey(event.getPlayer())) {
	            new player(plugin, event.getPlayer(), e);
	        } else {
	            plugin.players.get(event.getPlayer()).setTeam(e);
	            if (plugin.activeGame) {
	                /*l = */plugin.game.spawnTele(plugin.p(event.getPlayer()), event.getPlayer(), false);
	            } else {
	                plugin.players.get(event.getPlayer()).dead = false;
	            }
	            recount();
	        }
	        plugin.setDoors();
	        //event.setTo(l);
        }
    }
    
    public void recount() {
        plugin.diam = 0;
        plugin.gold = 0;
        plugin.tot = plugin.players.size();
        for (player i : plugin.players.values()) {
            if (i.getTeam() == team.DIAMOND) {
                plugin.diam++;
            } else {
                plugin.gold++;
            }
        }
    }
    
    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        CraftEntity item = (CraftEntity)event.getItem();
        int itemId = ((EntityItem)item.getHandle()).itemStack.id;
        int amm = ((EntityItem)item.getHandle()).itemStack.count;
        if (!allowed_pickup.contains(Material.getMaterial(itemId))) {
            event.setCancelled(true);
        } else if (Material.getMaterial(itemId) == Material.FEATHER) {
            int ammo = 0;
            for (ItemStack i : event.getPlayer().getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.FEATHER) {
                        ammo += i.getAmount();
                        event.getPlayer().getInventory().remove(i);
                    }
                }
            }
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
    
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (plugin.players.containsKey(event.getPlayer())) {
            event.setMessage(plugin.d + plugin.p(event.getPlayer()).getTeam().getColour() + event.getMessage());
        }
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        event.setUseInteractedBlock(Result.DENY);
        event.setUseItemInHand(Result.ALLOW);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material um = event.getPlayer().getItemInHand().getType();
            if (um == Material.BOW) {
                plugin.p(event.getPlayer()).stime = new Date().getTime() + 5000;
                if (event.getPlayer().getInventory().contains(Material.ARROW)) {
                    plugin.p(event.getPlayer()).s.incStat(Stat.ARROWS_FIRED);
                }
            } else if (um == Material.APPLE) {
                plugin.p(event.getPlayer()).vtime = new Date().getTime() + 10000;
                plugin.p(event.getPlayer()).s.incStat(Stat.APPLES_USED);
                plugin.p(event.getPlayer()).addPoints(3);
                Player i = event.getPlayer();
                if (plugin.p(event.getPlayer()).getTeam() == team.GOLD) {
                    i.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
                    i.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
                    i.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
                } else {
                    i.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
                    i.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
                    i.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));
                }
                i.updateInventory();
                plugin.p(event.getPlayer()).inv = true;
            } else if (um == Material.BONE) {
                event.getPlayer().getInventory().removeItem(new ItemStack(Material.BONE, 1));
                plugin.p(event.getPlayer()).s.incStat(Stat.DOGS_USED);
                plugin.p(event.getPlayer()).addPoints(3);
                plugin.game.sendMessage(team.BOTH, plugin.d + plugin.p(event.getPlayer()).getTeam().getColour() + plugin.p(event.getPlayer()).nick + plugin.d + "f called in a pack of dogs!");
                List<Wolf> wl = new ArrayList<Wolf>();
                for (Player i : plugin.players.keySet()) {
                    if (plugin.game.canHit(event.getPlayer(), i)) {
                        double theta = (generator.nextFloat() * Math.PI * 2);
                        Location l = i.getLocation().clone(); 
                        for (int j = 1; j <= 15; j++) {
                            if (i.getLocation().add(j * Math.cos(theta), 0, j * Math.sin(theta)).getBlock().getType() == Material.AIR) {
                                l = i.getLocation().add(j * Math.cos(theta), 0, j * Math.sin(theta));
                            } else {
                                break;
                            }
                        }
                        Wolf w = (Wolf) plugin.currentWorld.spawnCreature(l, CreatureType.WOLF);
                        w.setTarget(i);
                        w.setAngry(true);
                        wl.add(w);
                    }
                }
                plugin.wolves.add(new CWolfPack(plugin, wl, event.getPlayer(), new Date().getTime() + 30000));
            } else if (um == Material.DIAMOND) {
                event.getPlayer().getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));
                plugin.p(event.getPlayer()).s.incStat(Stat.CHOPPERS_USED);
                plugin.p(event.getPlayer()).addPoints(3);
                new chopper(plugin, event.getPlayer());
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().getItemInHand().getType() == Material.IRON_SWORD) {
            ArrayList<sentry> r = new ArrayList<sentry>();
            for (sentry i : plugin.sentries) {
                if (event.getClickedBlock() == i.bt && plugin.game.canHit(event.getPlayer(), i.getOwner())) {
                    i.destroy();
                    r.add(i);
                    plugin.p(event.getPlayer()).addPoints(3);
                    plugin.p(event.getPlayer()).s.incStat(Stat.SENTRIES_DESTROYED);
                }
            }
            plugin.sentries.removeAll(r);
        }
    }
    
    @Override
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }
}