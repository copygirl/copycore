package net.mcft.copy.core.base;

import net.mcft.copy.core.misc.rotatable.IRotatableBounds;
import net.mcft.copy.core.util.NameUtils;
import net.mcft.copy.core.util.RegistryUtils;
import net.mcft.copy.core.util.RegistryUtils.IRegistrable;
import net.mcft.copy.core.util.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockBase extends Block implements IRegistrable {
	
	private String name;
	
	public BlockBase(Material material) {
		super(material);
	}
	
	/** Called only once, then used as a return value in getName(). */
	protected String getNameInternal() {
		return NameUtils.getGameItemName(this);
	}
	
	/** Returns the item class used to register this block with. */
	public Class<? extends ItemBlock> getItemClass() { return ItemBlock.class; }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if (this instanceof IRotatableBounds)
			RotationUtils.setBlockBoundsFromRotation(this, world, x, y, z);
	}
	
	// IRegistrable implementation
	
	@Override
	public String getName() {
		return ((name == null) ? (name = getNameInternal()) : name);
	}
	
	/** Registers the block in the GameRegistry, as well as sets the block name. */
	@Override
	public <T extends IRegistrable> T register() {
		setBlockName(RegistryUtils.getActiveModId().toLowerCase() + "." + getName());
		GameRegistry.registerBlock(this, getItemClass(), getName());
		return (T)this;
	}
	
}
