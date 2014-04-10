package net.mcft.copy.core;

import net.mcft.copy.core.proxy.CommonProxy;

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
	
	private static Logger logger;
	
	public static Logger getLogger() { return logger; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.init();
		
	}
	
}
