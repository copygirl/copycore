package net.mcft.copy.core.client.renderer;

import net.mcft.copy.core.base.TileEntityBase;
import net.mcft.copy.core.util.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemRendererTileEntity implements IItemRenderer {
	
	private final TileEntity tileEntity;
	private final TileEntitySpecialRenderer renderer;
	
	private float scale = 1;
	private float offset = 0;
	private float inventoryRotation = 0;
	private float firstPersonRotation = 0;
	private float thirdPersonRotation = 0;
	
	public ItemRendererTileEntity(Class<? extends TileEntity> tileEntityClass,
	                              TileEntitySpecialRenderer renderer) {
		try { tileEntity = tileEntityClass.newInstance(); }
		catch (Exception e) { throw new RuntimeException(); }
		this.renderer = renderer;
	}
	
	public ItemRendererTileEntity setScale(float scale) {
		this.scale = scale;
		return this;
	}
	public ItemRendererTileEntity setOffset(float offset) {
		this.offset = offset;
		return this;
	}
	public ItemRendererTileEntity setInventoryRotation(float rotation) {
		this.inventoryRotation = rotation;
		return this;
	}
	public ItemRendererTileEntity setFirstPersonRotation(float rotation) {
		this.firstPersonRotation = rotation;
		return this;
	}
	public ItemRendererTileEntity setThirdPersonRotation(float rotation) {
		this.thirdPersonRotation = rotation;
		return this;
	}
	
	// IItemRenderer implementation
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) { return true; }
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
	                                     ItemRendererHelper helper) { return true; }
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
		tileEntity.blockType = Block.getBlockFromItem(item.getItem());
		tileEntity.blockMetadata = StackUtils.getDamage(item);
		
		if (tileEntity instanceof TileEntityBase)
			((TileEntityBase)tileEntity).onRenderAsItem(item);
		
		GL11.glPushMatrix();
		
		boolean equippedFirstPerson = (type == ItemRenderType.EQUIPPED_FIRST_PERSON);
		boolean equipped = ((type == ItemRenderType.EQUIPPED) || equippedFirstPerson);
		boolean inventory = (type == ItemRenderType.INVENTORY);
		
		if (equipped) {
			GL11.glTranslatef(0.5F, 0, 0.5F);
			float rotation = (equippedFirstPerson ? firstPersonRotation : (thirdPersonRotation - 90));
			GL11.glRotatef(rotation, 0, -1, 0);
		} else {
			GL11.glTranslatef(0, -0.5F, 0);
			if (inventory) {
				GL11.glTranslatef(0, offset, 0);
				GL11.glRotatef(inventoryRotation, 0, -1, 0);
			}
		}
		
		if (scale != 1.0F)
			GL11.glScalef(scale, scale, scale);
		
		renderer.renderTileEntityAt(tileEntity, -0.5, 0, -0.5, 1);
		
		GL11.glPopMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
	}
	
}
