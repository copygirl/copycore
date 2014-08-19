package net.mcft.copy.core.proxy;

import net.mcft.copy.core.config.SyncedConfig;
import net.mcft.copy.core.handler.ModelReloader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void init() {
		
		super.init();
		
		ModelReloader.register();
		
	}
	
	@SubscribeEvent
	public void onClientDisconnected(ClientDisconnectionFromServerEvent event) {
		// Clear synced config values on disconnect.
		SyncedConfig.resetAllConfigs();
	}
	
}
