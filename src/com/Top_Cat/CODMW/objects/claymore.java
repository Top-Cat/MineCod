package com.Top_Cat.CODMW.objects;

import java.util.Date;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;

public class claymore {

    team t;
    public Block b, d1, d2;
    main plugin;
    Player owner;
    public boolean exploded = false;
    public long explode = 0;

    public claymore(main instance, Block _c, int r, Player _o) {
        plugin = instance;
        t = plugin.players.get(_o).getTeam();
        b = _c;
        owner = _o;
        switch (r) {
            case 0: d1 = b.getRelative(0, 0, 1); d2 = b.getRelative(0, 0, 2); break;
            case 1: d1 = b.getRelative(-1, 0, 0); d2 = b.getRelative(-2, 0, 0); break;
            case 2: d1 = b.getRelative(0, 0, -1); d2 = b.getRelative(0, 0, -2); break;
            case 3: d1 = b.getRelative(1, 0, 0); d2 = b.getRelative(2, 0, 0); break;
        }
    }

    public void kill() {
        int kill = 0;
        for (Player p : plugin.players.keySet()) {
            if (plugin.players.get(p).getTeam() != t && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
                plugin.players.get(p).incHealth(2, owner, 3);
                kill++;
            }
        }
        plugin.currentWorld.createExplosion(b.getLocation(), 0);
        b.setType(Material.AIR);
    }

    public void detect(Player p) {
        if (plugin.players.get(p).getTeam() != t && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
            if (!exploded) {
                plugin.currentWorld.playEffect(p.getLocation(), Effect.CLICK2, 0);
            }
            exploded = true;
            explode = new Date().getTime() + 400;
        }
    }

}