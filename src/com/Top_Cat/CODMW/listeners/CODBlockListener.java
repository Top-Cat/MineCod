package com.Top_Cat.CODMW.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.claymore;
import com.Top_Cat.CODMW.objects.sentry;
import com.Top_Cat.CODMW.sql.Stat;

public class CODBlockListener extends BlockListener {
    
    main plugin;
    
    public CODBlockListener(main instance) {
        plugin = instance;
    }
    
    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }
    
    public int rotateblock(Player p, Block b) {
        int yaw = (int) p.getLocation().getYaw();
        if (yaw < -45) { yaw += 360; }
        if (yaw >= 315) { yaw -= 359; }
        int r = (int) Math.ceil((yaw + 45) / 90);
        switch (r) {
            case 1: b.setData((byte) 4); break;
            case 2: b.setData((byte) 2); break;
            case 3: b.setData((byte) 5); break;
            case 4: b.setData((byte) 3); break;
        }
        return r;
    }
    
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.WALL_SIGN) {
            int r = rotateblock(event.getPlayer(), event.getBlockPlaced());
            Sign s = (Sign) event.getBlockPlaced().getState();
            if (plugin.p(event.getPlayer()).getTeam() == team.DIAMOND) {
                s.setLine(0, plugin.d + "b** DIAMOND **");
                s.setLine(3, plugin.d + "b** DIAMOND **");
            } else {
                s.setLine(0, plugin.d + "6-- GOLD --");
                s.setLine(3, plugin.d + "6-- GOLD --");
            }
            
            s.setLine(1, "This side");
            s.setLine(2, "towards enemy");
            s.update();
            plugin.clays.add(new claymore(plugin, event.getBlockPlaced(), r, event.getPlayer()));
            plugin.p(event.getPlayer()).s.incStat(Stat.CLAYMORES_USED);
        } else if (event.getBlockPlaced().getType() == Material.DISPENSER && event.getBlockPlaced().getRelative(0, 1, 0).getType() == Material.AIR) {
            plugin.p(event.getPlayer()).s.incStat(Stat.SENTRIES_PLACED);
            plugin.p(event.getPlayer()).addPoints(3);
            event.getBlockPlaced().setType(Material.FENCE);
            event.getBlockPlaced().getRelative(0, 1, 0).setType(Material.DISPENSER);
            int r = rotateblock(event.getPlayer(), event.getBlockPlaced().getRelative(0, 1, 0));
            new sentry(plugin, event.getBlockPlaced(), r, event.getPlayer());
        } else if (!event.getPlayer().isOp()) {
        	System.out.println("Hmmm???");
            event.setBuild(false);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        }
    }
    
}