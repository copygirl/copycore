package net.mcft.copy.core;

import net.mcft.copy.core.network.ChannelHandler;
import net.mcft.copy.core.network.packet.PacketOpenGui;
import net.mcft.copy.core.network.packet.PacketSyncProperties;
import net.mcft.copy.core.network.packet.PacketSyncSettings;
import net.mcft.copy.core.proxy.CommonProxy;
import net.mcft.copy.core.tweak.TweakAutoReplace;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = copycore.MOD_ID, version = "${version}")
public class copycore
{
	
	public static final String MOD_ID = "copycore";
	
	@SidedProxy(clientSide = "net.mcft.copy.core.proxy.ClientProxy",
	            serverSide = "net.mcft.copy.core.proxy.CommonProxy")
	private static CommonProxy proxy;
	
	public static Logger log;
	public static ChannelHandler channelHandler;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		log = event.getModLog();
		
		channelHandler = new ChannelHandler(MOD_ID);
		channelHandler.addDiscriminator(0, PacketSyncSettings.class);
		channelHandler.addDiscriminator(1, PacketSyncProperties.class);
		channelHandler.addDiscriminator(2, PacketOpenGui.class);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.init();
		
		TweakAutoReplace.instance.enable();
		
	}
	
}
