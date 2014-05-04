package net.mcft.copy.core.util;

import net.mcft.copy.core.misc.BlockLocation;
import net.mcft.copy.core.misc.rotatable.IRotatable4;
import net.mcft.copy.core.misc.rotatable.IRotatable6;
import net.mcft.copy.core.misc.rotatable.IRotatableBounds;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public final class RotationUtils {
	
	private RotationUtils() {  }
	
	/** If supported, rotates the block in this direction and returns if successful. */
	public static boolean rotateBlock(BlockLocation blockLoc, ForgeDirection direction) {
		Block block = blockLoc.getBlock();
		if ((block instanceof IRotatable4) &&
		    ((block instanceof IRotatable6) ? DirectionUtils.isValid(direction)
		                                    : DirectionUtils.isHorizontal(direction))) {
			((IRotatable4)block).setDirection(blockLoc, direction);
			return true;
		} else return false;
	}
	
	/** Sets the block's bounds from its rotation.
	 *  Block has to be an IRotatableBounds. */
	public static void setBlockBoundsFromRotation(Block block, IBlockAccess world, int x, int y, int z) {
		if (!(block instanceof IRotatableBounds))
			throw new IllegalArgumentException("block is not an IRotatableBounds");
		
		IRotatableBounds rotatable = (IRotatableBounds)block;
		float w = rotatable.getBoundsWidth();
		float h = rotatable.getBoundsHeight();
		float d = rotatable.getBoundsDepth();
		
		ForgeDirection rotation = rotatable.getDirection(BlockLocation.get(world, x, y, z));
		if ((rotation == ForgeDirection.NORTH) || (rotation == ForgeDirection.SOUTH))
			block.setBlockBounds(0.5F - w / 2, 0.0F, 0.5F - d / 2, 0.5F + w / 2, h, 0.5F + d / 2);
		else if ((rotation == ForgeDirection.WEST) || (rotation == ForgeDirection.EAST))
			block.setBlockBounds(0.5F - d / 2, 0.0F, 0.5F - w / 2, 0.5F + d / 2, h, 0.5F + w / 2);
		else block.setBlockBounds(0.5F - w / 2, 0.0F, 0.5F - w / 2, 0.5F + w / 2, h, 0.5F + w / 2);
	}
	
}
