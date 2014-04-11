package net.mcft.copy.core;

import net.mcft.copy.core.network.ChannelHandler;
import net.mcft.copy.core.proxy.CommonProxy;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = copycore.MOD_ID, version = "${version}")
public class copycore
{
	
	public static final String MOD_ID = "copycore";
	
	@Instance(MOD_ID)
	public static copycore instance;
	
	@SidedProxy(clientSide = "net.mcft.copy.core.proxy.ClientProxy",
	            serverSide = "net.mcft.copy.core.proxy.CommonProxy")
	private static CommonProxy proxy;
	
	
	/** Gets the logger for copycore. <br>
	 *  Shouldn't be used by anything but copycore itself. */
	public static Logger getLogger() { return instance.logger; }
	
	/** Gets the channel handler for copycore. */
	public static ChannelHandler getChannelHandler() { return instance.channelHandler; }
	
	
	private Logger logger;
	
	private ChannelHandler channelHandler;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		channelHandler = new ChannelHandler(MOD_ID);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.init();
		
	}
	
}
