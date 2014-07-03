package net.mcft.copy.core.client;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

public class ModResource extends ResourceLocation {
	
	public ModResource(String modId, String location) {
		super(modId.toLowerCase(Locale.ENGLISH), location);
	}
	
}
