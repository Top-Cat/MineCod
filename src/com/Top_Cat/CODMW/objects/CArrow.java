package com.Top_Cat.CODMW.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.Top_Cat.CODMW.main;

import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MathHelper;

public class CArrow extends EntityArrow {

    private int moving = 0;
    private double firstY = 123.0D;
    public int speed = 2;
    main plugin;
    public Reason reason = Reason.NONE;
    public ownable killstreak;

    public CArrow(org.bukkit.World w, LivingEntity el, Block b, main instance, int yaw, int pitch, int r, Reason _reason, ownable ks) {
        super(((CraftWorld)w).getHandle());
        
        killstreak = ks;
        reason = _reason;
        plugin = instance;
        EntityLiving entityliving = ((CraftLivingEntity)el).getHandle();

        this.shooter = entityliving;
        b(0.5F, 0.5F);
        setPositionRotation(b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5, yaw, pitch);
        switch (r) {
            case 0 : this.locZ += 0.5; break;
            case 1 : this.locX -= 1.0; break;
            case 2 : this.locZ -= 1.0; break;
            case 3 : this.locX += 0.5; break;
        }
        setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        this.motX = (-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
        this.motZ = (MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
        this.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.141593F));
        a(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
    }

    public void m_() {
        super.m_();

        if (this.firstY == 123.0D) this.firstY = this.motY;
        if (this.speed > 0) {
          this.motY = this.firstY;
          this.speed -= 1;
        }

        if ((this.lastX == this.locX) && (this.lastY == this.locY) && (this.lastZ == this.locZ) && (this.moving == 0)) {
          this.moving = 1;
        }
        if (this.moving == 1) {
          destroy();
          this.moving = 2;
        }
      }

    public void destroy() {
          if(this.shooter instanceof EntityPlayer){
            EntityPlayer p = (EntityPlayer) this.shooter;
            Player player = (Player) p.getBukkitEntity();
            Location loca = getBukkitEntity().getLocation();
            loca.setY(loca.getY() - 1);
            Location newlock = loca;
            BlockBreakEvent e = new BlockBreakEvent(newlock.getBlock(), player);
            BlockBreakEvent ev = new BlockBreakEvent(loca.getBlock(),player);

             plugin.getServer().getPluginManager().callEvent(e);
             plugin.getServer().getPluginManager().callEvent(ev);
        }
    }
}