package uk.co.thomasc.codmw.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.killstreaks.Killstreaks;
import uk.co.thomasc.codmw.killstreaks.placeable.Placeable;

public class CODBlockListener implements Listener {
	
	private Main plugin;
	
	public CODBlockListener(Main instance) {
		plugin = instance;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
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
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Killstreaks s = Killstreaks.fromMaterial(event.getBlockPlaced().getType().getId());
		if (s != null && Placeable.class.isAssignableFrom(s.getkClass())) {
			if ((event.getBlockPlaced().getType() != Material.WALL_SIGN || event.getBlockPlaced().getState() instanceof Sign) && (event.getBlockPlaced().getType() != Material.DISPENSER || event.getBlockPlaced().getRelative(0, 1, 0).getType() == Material.AIR)) {
				s.callIn(plugin, event.getPlayer(), new Object[] {event.getBlockPlaced()});
			} else {
				event.setCancelled(true);
			}
		} else {
			event.setBuild(false);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
} 