package net.mcft.copy.core;

import java.io.File;

import net.mcft.copy.core.addon.Addons;
import net.mcft.copy.core.addon.MineTweakerAddon;
import net.mcft.copy.core.config.ISettingChangedHandler;
import net.mcft.copy.core.config.PriorityConfig;
import net.mcft.copy.core.config.PriorityConfig.Priority;
import net.mcft.copy.core.config.SyncedConfig;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.network.NetworkChannel;
import net.mcft.copy.core.network.packet.MessageOpenGui;
import net.mcft.copy.core.network.packet.MessageSyncProperties;
import net.mcft.copy.core.network.packet.MessageSyncSettings;
import net.mcft.copy.core.proxy.CommonProxy;
import net.mcft.copy.core.tweak.SimpleTweakChangedHandler;
import net.mcft.copy.core.tweak.TweakAutoReplace;
import net.mcft.copy.core.tweak.TweakDoubleDoorInteraction;
import net.mcft.copy.core.tweak.TweakPlayerDeathItemDespawnTime;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = copycore.MOD_ID, version = "@VERSION@",
     guiFactory = "net.mcft.copy.core.client.gui.CoreGuiFactory")
public class copycore {
	
	public static final String MOD_ID = "copycore";
	
	@SidedProxy(clientSide = "net.mcft.copy.core.proxy.ClientProxy",
	            serverSide = "net.mcft.copy.core.proxy.CommonProxy")
	private static CommonProxy proxy;
	
	public static Logger log;
	public static NetworkChannel channel;
	
	public static PriorityConfig config = new PriorityConfig();
	public static CoreConfig globalConfig;
	public static SyncedConfig syncedConfig;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		log = event.getModLog();
		
		setupConfig(event.getSuggestedConfigurationFile());
		
		channel = new NetworkChannel();
		channel.register(0, Side.CLIENT, MessageSyncSettings.class);
		channel.register(1, Side.CLIENT, MessageSyncProperties.class);
		channel.register(2, Side.CLIENT, MessageOpenGui.class);
		
		proxy.init();
		
		Addons.load(MineTweakerAddon.class);
		
	}
	
	private void setupConfig(File configFile) {
		
		registerSettingChangedHandlers();
		
		globalConfig = new CoreConfig(configFile);
		globalConfig.load();
		globalConfig.save();
		
		syncedConfig = new SyncedConfig(MOD_ID);
		syncedConfig.addFromReflection(config, globalConfig);
		
		config.add(Priority.GLOBAL, globalConfig);
		config.add(Priority.SYNCED, syncedConfig);
		
	}
	
	private void registerSettingChangedHandlers() {
		
		config.registerSettingChangedHandler(CoreConfig.tweakAutoReplace,
				new SimpleTweakChangedHandler(TweakAutoReplace.instance));
		
		config.registerSettingChangedHandler(CoreConfig.tweakDoubleDoorInteraction,
				new SimpleTweakChangedHandler(TweakDoubleDoorInteraction.instance));
		
		config.registerSettingChangedHandler(CoreConfig.tweakPlayerDeathItemDespawnTime,
				new ISettingChangedHandler<Integer>() {
					@Override public void onChanged(Setting<Integer> setting, Integer value) {
						TweakPlayerDeathItemDespawnTime.instance.setEnabled(value >= 0);
						TweakPlayerDeathItemDespawnTime.instance.despawnTime = value;
					}
				});
		
	}
	
}
