package com.Top_Cat.CODMW.Killstreaks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.team;
import com.Top_Cat.CODMW.sql.Stat;

public class carepackage extends useable {
    
    Location dropl;
    
    public carepackage(main instance, Player owner, Object[] args) {
        super(instance, owner, args);
        plugin.game.sendMessage(team.BOTH, plugin.d + getOwnerplayer().getTeam().getColour() + getOwnerplayer().nick + " called in a care package!");
        getOwnerplayer().s.incStat(Stat.CAREPACKAGES_USED);
        dropl = getOwner().getEyeLocation();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (getLifetime() >= 10) {
            Killstreaks i = Killstreaks.getRandom();
            plugin.currentWorld.dropItemNaturally(dropl, new ItemStack(i.getMat(), i.getAmm()));
            getOwner().sendMessage("Care package dropped " + i.toString());
            destroy();
        }
    }
    
}