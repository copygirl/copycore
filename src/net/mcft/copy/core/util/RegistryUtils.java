package net.mcft.copy.core.util;

import java.util.Arrays;

import net.mcft.copy.core.config.ContentConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;

public final class RegistryUtils {
	
	private RegistryUtils() {  }
	
	/** Returns the mod ID of the currently active mod. <br>
	 *  <b>Warning:</b> Only use during FML events preinit, init, postinit and similar. */
	public static String getActiveModId() {
		if (Loader.instance().hasReachedState(LoaderState.AVAILABLE))
			throw new RuntimeException("getActiveModId can't be called after mods have been initialized!");
		return Loader.instance().activeModContainer().getModId();
	}
	
	/** Returns if all the required objects are enabled. */
	public static boolean isEnabled(Object... required) {
		return !Arrays.asList(required).contains(null);
	}
	
	/** Registers this object if it's enabled in the config
	 *  and all required objects are enabled as well. */
	public static <T extends IRegistrable> T registerIfEnabled(T object, ContentConfig config, Object... required) {
		return ((isEnabled(required) && config.isEnabled(object)) ? object.<T>register() : null);
	}
	
	public static interface IRegistrable {
		
		/** Returns the name of this object. */
		String getName();
		
		/** Registers this object and returns it. */
		<T extends IRegistrable> T register();
		
	}
	
}
