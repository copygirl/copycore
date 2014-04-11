package net.mcft.copy.core.proxy;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.network.AbstractPacket;
import net.mcft.copy.core.network.packet.PacketSyncSettings;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CommonProxy {
	
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@EventHandler
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		// Synchronize settings with newly joined players.
		AbstractPacket packet = new PacketSyncSettings(Config.getAllConfigs());
		copycore.getChannelHandler().sendToPlayer(event.player, packet);
	}
	
}
