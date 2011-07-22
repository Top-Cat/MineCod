package com.Top_Cat.CODMW.Killstreaks;

import java.util.Date;

import net.minecraft.server.World;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.listeners.CODBlockListener;
import com.Top_Cat.CODMW.sql.Achievement;
import com.Top_Cat.CODMW.sql.Stat;

public class claymore extends placeable {

    public Block b, d1, d2;
    public boolean exploded = false;
    public long explode = 0, init = 0;
    public int r;

    public claymore(main instance, Player _o, Object[] args) {
        super(instance, _o, args);
        b = (Block) args[0];
        r = (Integer) CODBlockListener.rotateblock(_o, b);
        init = new Date().getTime() + 1000;
        getOwnerplayer().s.incStat(Stat.CLAYMORES_USED);
        setText();
        switch (r) {
            case 0: d1 = b.getRelative(0, 0, 1); d2 = b.getRelative(0, 0, 2); break;
            case 1: d1 = b.getRelative(-1, 0, 0); d2 = b.getRelative(-2, 0, 0); break;
            case 2: d1 = b.getRelative(0, 0, -1); d2 = b.getRelative(0, 0, -2); break;
            case 3: d1 = b.getRelative(1, 0, 0); d2 = b.getRelative(2, 0, 0); break;
        }
    }
    
    @Override
    public void teamSwitch() {
        super.teamSwitch();
        setText();
    }

    @Override
    public void destroy() {
        super.destroy();
        b.setType(Material.AIR);
    }
    
    @Override
    public void onMove(PlayerMoveEvent event, boolean blockmove) {
        super.onMove(event, blockmove);
        if (exploded == false) {
            detect(event.getPlayer());
        }
    }
    
    public void setText() {
        Sign s = (Sign) b.getState();
        s.setLine(0, plugin.game.getClaymoreText(getOwner()));
        s.setLine(3, plugin.game.getClaymoreText(getOwner()));
        
        s.setLine(1, "This side");
        s.setLine(2, "towards enemy");
        s.update();
    }
    
    @Override
    public void tickfast() {
        super.tickfast();
        if (init < new Date().getTime() && b.getType() != Material.WALL_SIGN) {
            b.setType(Material.WALL_SIGN);
            
            switch (r) {
                case 1: b.setData((byte) 4); break;
                case 2: b.setData((byte) 2); break;
                case 3: b.setData((byte) 5); break;
                case 4: b.setData((byte) 3); break;
            }
            
            if (b.getState() instanceof Sign) {
                setText();
            }
        }
        if (exploded && explode < new Date().getTime()) {
            kill();
            destroy();
        }
        for (Entity i : plugin.currentWorld.getEntities()) {
            if (i instanceof Arrow) {
                Location l = i.getLocation();
                if (b.getLocation().add(0.5, 0, 0.5).distance(l) < 1) {
                    setOwner((Player) ((Arrow)i).getShooter(), plugin.p((Player) ((Arrow)i).getShooter()));
                    kill();
                }
            }
        }
        
    }
    
    public void kill() {
        int kill = 0;
        for (Player p : plugin.players.keySet()) {
            if ((plugin.game.canHit(getOwner(), p) || p == getOwner()) && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
                plugin.p(p).incHealth(20, getOwner(), 3, this);
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
        destroy();
    }

    public void detect(Player p) {
        if (plugin.game.canHit(getOwner(), p) && (p.getLocation().getBlock() == b || p.getLocation().getBlock() == d1 || p.getLocation().getBlock() == d2)) {
            if (!exploded) {
                plugin.currentWorld.playEffect(p.getLocation(), Effect.CLICK2, 0);
            }
            exploded = true;
            explode = new Date().getTime() + 400;
        }
    }

}