package net.mcft.copy.core.client.model;

import net.mcft.copy.core.handler.ModelReloader;
import net.mcft.copy.core.handler.ModelReloader.IReloadableModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CoreModelBase extends ModelBase implements IReloadableModel {
	
	public final ResourceLocation resource;
	public IModelCustom model;
	
	public CoreModelBase(ResourceLocation resource) {
		this.resource = resource;
		ModelReloader.registerModel(this);
	}
	
	@Override
	public void reload() {
		model = AdvancedModelLoader.loadModel(resource);
	}
	
	
	public void render(Entity entity, double x, double y, double z, float partialTick) {
		renderInternal(entity, x, y, z, partialTick);
	}
	
	public void render(TileEntity tileEntity, double x, double y, double z, float partialTick) {
		renderInternal(tileEntity, x, y, z, partialTick);
	}
	
	private void renderInternal(Object entity, double x, double y, double z, float partialTick) {
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);
		
		if (model == null) reload();
		renderModel(entity, partialTick);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		
	}
	
	protected void renderModel(Object entity, float partialTick) {
		model.renderAll();
	}
	
}
