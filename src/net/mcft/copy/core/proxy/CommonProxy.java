package net.mcft.copy.core.proxy;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.config.SyncedConfig;
import net.mcft.copy.core.entity.EntityPropertiesBase;
import net.mcft.copy.core.network.AbstractMessage;
import net.mcft.copy.core.network.packet.MessageSyncSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CommonProxy {
	
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		// Synchronize settings with newly joined players.
		AbstractMessage packet = new MessageSyncSettings(SyncedConfig.getAllConfigs());
		copycore.channel.sendTo(packet, event.player);
	}
	
	@SubscribeEvent
	public void onStartTracking(PlayerEvent.StartTracking event) {
		EntityPropertiesBase.syncProperties(event.entityPlayer, event.target);
	}
	
}
