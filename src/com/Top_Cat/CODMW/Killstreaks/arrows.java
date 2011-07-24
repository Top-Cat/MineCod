package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.sql.Stat;

public class arrows extends useable {

    public arrows(main instance, Player owner, Object[] args) {
        super(instance, owner, args);
        getOwnerplayer().s.incStat(Stat.UARROWS_USED);
    }
    
    @Override
    public void onInteract(PlayerInteractEvent event) {
        super.onInteract(event);
        if (event.getPlayer() == getOwner() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && getOwner().getItemInHand().getType() == Material.BOW) {
            int arrows = 0;
            for (ItemStack i : getOwner().getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.ARROW) {
                        arrows += i.getAmount();
                    }
                }
            }
            getOwner().getInventory().addItem(new ItemStack(Material.ARROW, 15 - arrows));
            getOwner().updateInventory();
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getLifetime() > 10) {
            destroy();
        }
    }
    
}