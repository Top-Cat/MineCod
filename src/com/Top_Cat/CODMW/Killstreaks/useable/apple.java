package com.Top_Cat.CODMW.Killstreaks.useable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.objects.Reason;
import com.Top_Cat.CODMW.objects.player;
import com.Top_Cat.CODMW.sql.Stat;

public class apple extends useable {

    @SuppressWarnings("deprecation")
    public apple(main instance, Player owner, Object[] args) {
        super(instance, owner, args);
        getOwnerplayer().s.incStat(Stat.APPLES_USED);
        setArmour();
        getOwner().updateInventory();
    }
    
    public void setArmour() {
        if (getOwnerplayer().getTeam() == team.GOLD) {
            getOwner().getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE, 1));
            getOwner().getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS, 1));
            getOwner().getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS, 1));
        } else {
            getOwner().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
            getOwner().getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
            getOwner().getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS, 1));
        }
    }
    
    @Override
    public int onDamage(int damage, Player attacker, Player defender, Reason reason, Object ks) {
        if (defender == getOwner()) {
            damage = 0;
        }
        return damage;
    }
    
    @Override
    public void onKill(player attacker, player defender, Reason reason, Object ks) {
        attacker.s.incStat(Stat.INVULNERABLE_KILLS);
    }
    
    @Override
    public void teamSwitch() {
        super.teamSwitch();
        setArmour();
    }
    
    @Override
    public void destroy() {
        super.destroy();
        getOwner().getInventory().clear(38);
        getOwner().getInventory().clear(37);
        getOwner().getInventory().clear(36);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getLifetime() > 10) {
            destroy();
        }
    }
    
}