package net.mcft.copy.core.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

public interface ITextureProvider {
	
	/** Returns the texture to be used for this tile object's renderer. */
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture();
	
}