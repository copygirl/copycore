package net.mcft.copy.core.misc.rotatable;

import net.mcft.copy.core.misc.BlockLocation;
import net.minecraftforge.common.util.ForgeDirection;

/** When implemented in a Block, suggests it can be rotated in the 4 horizontal directions. */
public interface IRotatable4 {
	
	/** Gets the direction the block is facing.
	 *  May return UNKNOWN if something went wrong. */
	ForgeDirection getDirection(BlockLocation block);
	
	/** Sets the direction the block is facing. */
	void setDirection(BlockLocation block, ForgeDirection direction);
	
}
