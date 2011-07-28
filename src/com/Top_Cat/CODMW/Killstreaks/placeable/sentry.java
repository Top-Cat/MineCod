package com.Top_Cat.CODMW.Killstreaks.placeable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.listeners.CODBlockListener;
import com.Top_Cat.CODMW.objects.CArrow;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.sql.Stat;

public class sentry extends placeable {
    
    public Block b, bt;
    int rot;
    
    public sentry(main instance, Player _o, Object[] args) {
        super(instance, _o, args);
        b = (Block) args[0];
        b.setType(Material.FENCE);
        bt = b.getRelative(0, 1, 0);
        bt.setType(Material.DISPENSER);
        rot = (Integer) CODBlockListener.rotateblock(_o, bt);
        getOwnerplayer().s.incStat(Stat.SENTRIES_PLACED);
    }
    
    @Override
    public void destroy() {
        super.destroy();
        b.setType(Material.AIR);
        bt.setType(Material.AIR);
    }
    
    @Override
    public void onMove(PlayerMoveEvent event, boolean blockmove) {
        super.onMove(event, blockmove);
        if (blockmove && event.getTo().getBlock().getRelative(0, -2, 0) == b) {
            plugin.game.spawnTele(plugin.p(event.getPlayer()), event.getPlayer(), false);
            event.getPlayer().sendMessage(plugin.d + "bOnly Gigs stand on dispensers, you have been respawned!");
        }
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && (event.getPlayer().getItemInHand().getType() == Material.IRON_SWORD || event.getPlayer().getItemInHand().getType() == Material.RAW_FISH)) {
            if (event.getClickedBlock() == bt && plugin.game.canHit(event.getPlayer(), getOwner(), false)) {
                destroy();
                plugin.p(event.getPlayer()).addPoints(3);
                if (plugin.p(event.getPlayer()).getTeam() != getOwnerplayer().getTeam()) {
                    plugin.p(event.getPlayer()).s.incStat(Stat.SENTRIES_DESTROYED);
                }
            }
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getLifetime() >= 60) {
            destroy();
        }
    }
    
    @Override
    public void tickfast() {
        super.tickfast();
        for (player i : plugin.players.values()) {
            Location l1 = i.p.getLocation();
            Location l2 = b.getLocation();
            if (plugin.game.canHit(getOwner(), i.p, true) && l1.distance(l2) < 10 && Math.abs(l1.getY() - l2.getY()) < 3) {
                int yaw = (int) (Math.toDegrees(Math.atan2(l1.getX() - (l2.getX() + 0.5), (l2.getZ() + 0.5) - l1.getZ())) + 180);
                if (yaw >= 315) { yaw -= 359; }
                int r = (int) Math.ceil((yaw + 45) / 90);
                if (r == rot) {
                    CArrow arrow = new CArrow(plugin.currentWorld, getOwner(), bt, plugin, yaw, 0, r, Reason.SENTRY, this);
                    arrow.world.addEntity(arrow);
                }
            }
        }
    }

}