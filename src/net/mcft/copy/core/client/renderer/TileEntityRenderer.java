package net.mcft.copy.core.client.renderer;

import net.mcft.copy.core.client.model.CoreModelBase;
import net.mcft.copy.core.misc.BlockLocation;
import net.mcft.copy.core.misc.rotatable.IRotatable4;
import net.mcft.copy.core.util.DirectionUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

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
		if (model == null) return;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(x + 0.5, y, z + 0.5);
		
		// Rotate any IRotatable automatically
		if ((tileEntity.getBlockType() instanceof IRotatable4)) {
			IRotatable4 rotatable = ((IRotatable4)tileEntity.getBlockType());
			ForgeDirection dir = ((tileEntity.getWorldObj() != null)
					? rotatable.getDirection(BlockLocation.get(tileEntity))
					: ForgeDirection.WEST);
			float rot = DirectionUtils.getRotation(dir) + 180;
			GL11.glRotatef(rot, 0, -1, 0);
		}
		
		model.render(tileEntity, 0, 0, 0, partialTick);
		
		GL11.glPopMatrix();
	}
	
}
