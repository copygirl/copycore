package net.mcft.copy.core.util;

import java.util.Arrays;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.setting.BooleanSetting;
import cpw.mods.fml.common.Loader;

public final class RegistryUtils {
	
	private RegistryUtils() {  }
	
	/** Returns the mod ID of the currently active mod. <br>
	 *  <b>Warning:</b> Only use during FML events preinit, init, postinit and similar. */
	public static String getActiveModId() {
		return Loader.instance().activeModContainer().getModId();
	}
	
	/** Returns if all the required objects are enabled. */
	public static boolean isEnabled(Object... required) {
		return !Arrays.asList(required).contains(null);
	}

	/** Registers the object if all required objects are enabled. */
	public static <T extends IRegistrable> T registerIfEnabled(
			T objectToRegister, Object... required) {
		return (isEnabled(required) ? (T)objectToRegister.register() : null);
	}
	
	/** Registers the object if it is enabled in the
	 *  configuration and all required objects are enabled. */
	public static <T extends IRegistrable> T registerIfEnabled(
			Config config, String category, boolean defaultEnabled, String comment,
			T objectToRegister, Object... required) {
		String name = objectToRegister.getName();
		BooleanSetting setting = new BooleanSetting(config, category + "." + name, defaultEnabled).setComment(comment);
		return (setting.getValue() ? registerIfEnabled(objectToRegister, required) : null);
	}
	/** Registers the object if it is enabled in the
	 *  configuration and all required objects are enabled. */
	public static <T extends IRegistrable> T registerIfEnabled(
			Config config, String category, boolean defaultEnabled,
			T objectToRegister, Object... required) {
		return registerIfEnabled(config, category, defaultEnabled, null, objectToRegister, required);
	}
	/** Registers the object if it is enabled in the
	 *  configuration and all required objects are enabled. */
	public static <T extends IRegistrable> T registerIfEnabled(
			Config config, String category, String comment,
			T objectToRegister, Object... required) {
		return registerIfEnabled(config, category, true, comment, objectToRegister, required);
	}
	/** Registers the object if it is enabled in the
	 *  configuration and all required objects are enabled. */
	public static <T extends IRegistrable> T registerIfEnabled(
			Config config, String category,
			T objectToRegister, Object... required) {
		return registerIfEnabled(config, category, true, null, objectToRegister, required);
	}
	
	
	public static interface IRegistrable {
		
		/** Returns the name of this object. */
		String getName();
		
		/** Registers this object and returns it. */
		<T extends IRegistrable> T register();
		
	}
	
}
