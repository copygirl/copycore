package net.mcft.copy.core.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.mcft.copy.core.client.model.CoreModelBase;
import net.minecraft.util.ResourceLocation;

public interface IModelProvider {
	
	/** Returns the model to be used for this object's renderer. */
	@SideOnly(Side.CLIENT)
	public CoreModelBase getModel();
	
}