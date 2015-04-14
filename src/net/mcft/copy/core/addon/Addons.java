package net.mcft.copy.core.addon;

import cpw.mods.fml.common.Loader;

public final class Addons {
	
	private Addons() {  }
	
	/** Loads a class annotated with the @Addon annotation if all
	 *  required mods are loaded and returns it, otherwise returns null. */
	public static <T> T load(Class<T> addon) {
		Addon annotation = addon.getAnnotation(Addon.class);
		if (annotation == null)
			throw new IllegalArgumentException("addon doesn't have an @Addon annotation");
		
		for (String modId : annotation.value())
			if (!Loader.isModLoaded(modId))
				return null;
		
		try { return addon.newInstance(); }
		catch (Exception ex) { throw new IllegalArgumentException(ex); }
	}
	
	/** Loads a bunch of classes annotated with the @Addon
	 *  annotation as long as all required mods are loaded. */
	public static void load(Class ...addons) {
		for (Class addon : addons)
			load(addon);
	}
	
}
