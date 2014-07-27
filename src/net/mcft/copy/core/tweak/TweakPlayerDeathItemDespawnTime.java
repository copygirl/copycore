package net.mcft.copy.core.tweak;

import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/** Sets items dropped by  */
public final class TweakPlayerDeathItemDespawnTime extends Tweak {
	
	public static final TweakPlayerDeathItemDespawnTime instance = new TweakPlayerDeathItemDespawnTime();
	
	public double despawnTime = 5 * 60;
	
	private TweakPlayerDeathItemDespawnTime() {  }
	
	@Override
	public void enable() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void disable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDrops(PlayerDropsEvent event) {
		
		for (EntityItem item : event.drops)
			item.lifespan = (int)(despawnTime * 20);
		
	}
	
}
