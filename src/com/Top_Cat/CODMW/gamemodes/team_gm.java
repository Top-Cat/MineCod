package com.Top_Cat.CODMW.gamemodes;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.CWolfPack;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.sql.Stat;

public class team_gm extends gamemode {
    
    public team_gm(main instance) {
        super(instance);
    }
    
    @Override
    public void startGame() {
        super.startGame();
        /*for (player i : plugin.players.values()) {
            for (player j : plugin.players.values()) {
                if (i.getTeam() != j.getTeam() || i.getTeam() == team.BOTH) {
                    BukkitContrib.getAppearanceManager().hidePlayerTitle((ContribPlayer) i.p, j.p);
                }
            }
        }*/
    }
    
    @Override
    public void playerjoin(PlayerJoinEvent event) {
        super.playerjoin(event);
        event.getPlayer().sendMessage(plugin.d + "9Please choose your team!");
    }
    
    @Override
    public void onWin(team winners, player lastkill, player lastdeath) {
        team lost = winners == team.DIAMOND ? team.GOLD : team.DIAMOND;
        sendMessage(winners, winmesssages.get(generator.nextInt(winmesssages.size())));
        sendMessage(lost, lossmesssages.get(generator.nextInt(lossmesssages.size())));
        
        for (player i : plugin.players.values()) {
            switch(i.getTeam()) {
                case GOLD: i.p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET, 1)); break;
                case DIAMOND: i.p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET, 1)); break;
            }
            if (winners != team.BOTH) {
                if (i.getTeam() == winners) {
                    i.s.incStat(Stat.WINS);
                    i.addPoints(10);
                } else {
                    i.s.incStat(Stat.LOSSES);
                    i.addPoints(-5);
                }
            }
        }
        
        super.onWin(winners, lastkill, lastdeath);
    }
    
    @Override
    public void jointele(Player p) {
        p.teleport(plugin.teamselect);
        plugin.players.remove(p);
    }
    
    @Override
    public boolean canHit(LivingEntity a, LivingEntity d) {
        team t1 = team.BOTH;
        team t2 = team.BOTH;
        if (a instanceof Player) {
            t1 = plugin.p((Player) a).getTeam();
        } else if (a instanceof Wolf) {
            for (CWolfPack i : plugin.wolves) {
                if (i.wolf.contains(a)) {
                    t1 = i.getOwnerplayer().getTeam();
                }
            }
        }
        if (d instanceof Player) {
            t2 = plugin.p((Player) d).getTeam();
        } else if (d instanceof Wolf) {
            for (CWolfPack i : plugin.wolves) {
                if (i.wolf.contains(d)) {
                    t2 = i.getOwnerplayer().getTeam();
                }
            }
        }
        return (t1 != t2);
    }
    
    @Override
    public String getClaymoreText(Player p) {
        if (plugin.p(p).getTeam() == team.DIAMOND) {
            return plugin.d + "b** DIAMOND **";
        } else {
            return plugin.d + "6-- GOLD --";
        }
    }
    
}