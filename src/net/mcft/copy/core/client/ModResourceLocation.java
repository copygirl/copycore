package net.mcft.copy.core.client;

import net.mcft.copy.core.util.RegistryUtils;
import net.minecraft.util.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {
	
	public ModResourceLocation(String location) {
		super(RegistryUtils.getActiveModId().toLowerCase(), location);
	}
	
}