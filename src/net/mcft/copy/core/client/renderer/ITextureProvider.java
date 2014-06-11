package net.mcft.copy.core.client.renderer;

import net.mcft.copy.core.client.Color;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ITextureProvider {
	
	/** Returns the amount of render passes for this object's renderer. */
	@SideOnly(Side.CLIENT)
	public int getRenderPasses();
	
	/** Returns the color to be used for this object's renderer in this render pass. */
	@SideOnly(Side.CLIENT)
	public Color getColor(int pass);
	
	/** Returns the texture to be used for this object's renderer in this render pass. */
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture(int pass);
	
}