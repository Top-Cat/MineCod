package com.Top_Cat.CODMW.objects;

import java.util.Date;

import net.minecraft.server.World;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.Achievement;

public class claymore extends ownable {

    public Block b, d1, d2;
    main plugin;
    public boolean exploded = false;
    public long explode = 0, init = 0;
    public int r;

    public claymore(main instance, Block _c, int _r, Player _o) {
        plugin = instance;
        b = _c;
        setOwner(_o, plugin.p(_o));
        init = new Date().getTime() + 1000;
        switch (_r) {
            case 0: d1 = b.getRelative(0, 0, 1); d2 = b.getRelative(0, 0, 2); break;
            case 1: d1 = b.getRelative(-1, 0, 0); d2 = b.getRelative(-2, 0, 0); break;
            case 2: d1 = b.getRelative(0, 0, -1); d2 = b.getRelative(0, 0, -2); break;
            case 3: d1 = b.getRelative(1, 0, 0); d2 = b.getRelative(2, 0, 0); break;
        }
        r = _r;
    }

    public void kill() {
        int kill = 0;
        for (Player p : plugin.players.keySet()) {
            if ((plugin.game.canHit(getOwner(), p) || p == getOwner()) && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
                plugin.p(p).incHealth(2, getOwner(), 3, this);
                kill++;
            }
        }
        if (kill > 1) {
            plugin.p(getOwner()).s.awardAchievement(Achievement.CLAYMORE_MULTI);
        }
        init = new Date().getTime() + 10000;
        plugin.currentWorld.createExplosion(b.getLocation(), 0);
        ((World) ((CraftWorld)plugin.currentWorld).getHandle()).a("explode", b.getX(), b.getY(), b.getZ(), 1, 1, 1);
        ((World) ((CraftWorld)plugin.currentWorld).getHandle()).a("smoke", b.getX(), b.getY(), b.getZ(), 1, 1, 1);
        b.setType(Material.AIR);
    }

    public void detect(Player p) {
        if (plugin.game.canHit(getOwner(), p) && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
        	System.out.println("Explode?");
            if (!exploded) {
                plugin.currentWorld.playEffect(p.getLocation(), Effect.CLICK2, 0);
            }
            exploded = true;
            explode = new Date().getTime() + 400;
        }
    }

}