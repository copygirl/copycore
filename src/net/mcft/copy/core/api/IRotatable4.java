package net.mcft.copy.core.api;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/** When implemented in a Block, suggests it can be rotated in the 4 horizontal directions. */
public interface IRotatable4 {
	
	/** Gets the direction the block is facing.
	 *  May return UNKNOWN if something went wrong. */
	public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z);
	
	/** Sets the direction the block is facing. */
	public void setDirection(IBlockAccess world, int x, int y, int z, ForgeDirection direction);
	
}
