package net.mcft.copy.core;

import net.mcft.copy.core.network.NetworkChannel;
import net.mcft.copy.core.network.packet.MessageOpenGui;
import net.mcft.copy.core.network.packet.MessageSyncProperties;
import net.mcft.copy.core.network.packet.MessageSyncSettings;
import net.mcft.copy.core.proxy.CommonProxy;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = copycore.MOD_ID, version = "${version}")
public class copycore {
	
	public static final String MOD_ID = "copycore";
	
	@SidedProxy(clientSide = "net.mcft.copy.core.proxy.ClientProxy",
	            serverSide = "net.mcft.copy.core.proxy.CommonProxy")
	private static CommonProxy proxy;
	
	public static Logger log;
	public static CoreConfig config;
	public static NetworkChannel channel;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		log = event.getModLog();
		
		config = new CoreConfig(event.getSuggestedConfigurationFile());
		config.load();
		
		// TODO: Add debug items?
		
		proxy.init();
		
		config.save();
		
		channel = new NetworkChannel();
		channel.register(0, Side.CLIENT, MessageSyncSettings.class);
		channel.register(1, Side.CLIENT, MessageSyncProperties.class);
		channel.register(2, Side.CLIENT, MessageOpenGui.class);
		
	}
	
}
