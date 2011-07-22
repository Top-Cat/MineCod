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
import com.Top_Cat.CODMW.Killstreaks.Killstreaks;
import com.Top_Cat.CODMW.Killstreaks.placeable;

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
    
    public static int rotateblock(Player p, Block b) {
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
        Killstreaks s = Killstreaks.fromMaterial(event.getBlockPlaced().getType());
        if (s != null && placeable.class.isAssignableFrom(s.getkClass())) {
            if ((event.getBlockPlaced().getType() != Material.WALL_SIGN || event.getBlockPlaced().getState() instanceof Sign) && (event.getBlockPlaced().getType() != Material.DISPENSER || event.getBlockPlaced().getRelative(0, 1, 0).getType() == Material.AIR)) {
                s.callIn(plugin, event.getPlayer(), new Object[] {event.getBlockPlaced()});
            } else {
                event.setCancelled(true);
            }
        } else if (!event.getPlayer().isOp()) {
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