package net.mcft.copy.core.client.renderer;

import net.mcft.copy.core.client.model.CoreModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRenderer extends TileEntitySpecialRenderer {
	
	private final ResourceLocation defaultTexture;
	private final CoreModelBase defaultModel;
	
	public TileEntityRenderer(ResourceLocation defaultTexture, CoreModelBase defaultModel) {
		this.defaultTexture = defaultTexture;
		this.defaultModel = defaultModel;
	}
	
	private CoreModelBase getModel(TileEntity tileEntity) {
		CoreModelBase model = null;
		if (tileEntity instanceof IModelProvider)
			model = ((IModelProvider)tileEntity).getModel();
		return ((model != null) ? model : defaultModel);
	}
	
	private ResourceLocation getTexture(TileEntity tileEntity) {
		ResourceLocation texture = null;
		if (tileEntity instanceof ITextureProvider)
			texture = ((ITextureProvider)tileEntity).getTexture();
		return ((texture != null) ? texture : defaultTexture);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
		bindTexture(getTexture(tileEntity));
		CoreModelBase model = getModel(tileEntity);
		if (model != null)
			model.render(tileEntity, x + 0.5, y, z + 0.5, partialTick);
	}
	
}
