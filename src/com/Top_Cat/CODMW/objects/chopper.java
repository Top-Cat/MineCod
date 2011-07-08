package com.Top_Cat.CODMW.objects;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;

public class chopper extends ownable {

    Location p;
    public Location l;
    main plugin;
    double mx = 0, mz = 0, tx = 0, tz = 0;
    int tick = 0;
    Timer k = new Timer();
    boolean started = false;
    int health = 5;
    
    public void arrowhit() {
        health--;
    }

    public chopper(main instance, Player _o) {
        plugin = instance;
        setOwner(_o);
        plugin.game.sendMessage(team.BOTH, plugin.d + plugin.p(getOwner()).getTeam().getColour() + plugin.p(getOwner()).nick + plugin.d + "f called in a chopper!");
        
        plugin.choppers.add(this);
        
        k.schedule(new tick(), 150, 150);
        k.schedule(new destroyTask(), 60000);
        l = avgEnemies();
        moveto();
    }

    public class destroyTask extends TimerTask {

        @Override
        public void run() {
            destroy();
            plugin.choppers.remove(this);
        }

    }

    public void destroy() {
        k.cancel();
        l.getBlock().setType(Material.AIR);
    }

    public class tick extends TimerTask {

        @Override
        public void run() {
            if (health <= 0) {
                destroy();
                plugin.choppers.remove(this);
            } else {
                tick++;
                if (tick <= 10) {
                    try {
                        if (started) {
                            l.getBlock().setType(Material.AIR);
                        }
                        started = true;
                        tx += mx;
                        tz += mz;
                        l = p.clone().add(tx, 0, tz);
                        l.setY(l.getWorld().getHighestBlockYAt(l) + 7);
                        switch (plugin.p(getOwner()).getTeam()) {
                            case DIAMOND: l.getBlock().setType(Material.DIAMOND_BLOCK); break;
                            case GOLD: l.getBlock().setType(Material.GOLD_BLOCK); break;
                            case BOTH: l.getBlock().setType(Material.OBSIDIAN); break;
                        }
                    } catch (Exception e) {
                        System.out.println("Error moving chopper");
                    }
                } else if (tick > 18) {
                    tick = 0;
                    moveto();
                } else {
                    
                    for (player i : plugin.players.values()) {
                        Location l1 = i.p.getLocation();
                        if (plugin.game.canHit(getOwner(), i.p)) {
                            int yaw = (int) (Math.toDegrees(Math.atan2(l1.getX() - (l.getX() + 0.5), (l.getZ() + 0.5) - l1.getZ())) + 180);
                            if (yaw >= 315) { yaw -= 359; }
                            int r = (int) Math.ceil((yaw + 45) / 90);
                            
                            Location l2 = l1.clone();
                            l2.setY(l.getY());
                            int pitch = (int) ((Math.toDegrees(Math.atan2(l2.distance(l), (l.getY() + 0.5) - l1.getY())) - 85) * 1.1);
                            
                            CArrow arrow = new CArrow(plugin.currentWorld, getOwner(), l.getBlock(), plugin, yaw, -pitch, r, 6);
                            arrow.world.addEntity(arrow);
                        }
                    }
                    
                }
            }
        }
        
    }

    public Location avgEnemies() {
        int ax = 0;
        int az = 0;
        int a = 0;
        for (Player i : plugin.players.keySet()) {
            if (plugin.game.canHit(getOwner(), i) && plugin.p(i).dead == false) {
                ax = (int) (((ax * a) + i.getLocation().getX()) / (a + 1));
                az = (int) (((az * a) + i.getLocation().getZ()) / (a + 1));
                a++;
            }
        }
        Location out = new Location(plugin.currentWorld, (ax - 3) + plugin.game.generator.nextInt(7), 0, (az - 3) + plugin.game.generator.nextInt(7));
        out.setY(out.getWorld().getHighestBlockYAt(out) + 7);
        return out;
    }
    
    public void moveto() {
        Location l2 = avgEnemies();
        mx = (l2.getX() - l.getX()) / 10;
        mz = (l2.getZ() - l.getZ()) / 10;
        tx = 0;
        tz = 0;
        p = l;
    }

}