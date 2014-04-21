package net.mcft.copy.core.util;

import net.mcft.copy.core.api.IRotatable4;
import net.mcft.copy.core.api.IRotatable6;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public final class RotationUtils {
	
	private RotationUtils() {  }
	
	/** Returns if this directions is a valid direction (not UNKNOWN). */
	public static boolean isValidDirection(ForgeDirection direction) {
		return (direction.ordinal() < ForgeDirection.UNKNOWN.ordinal());
	}
	
	/** Returns if this directions is a valid horizontal direction (NORTH, SOUTH, EAST, WEST). */
	public static boolean isValidHorizontalDirection(ForgeDirection direction) {
		return ((direction.ordinal() >= ForgeDirection.NORTH.ordinal()) && isValidDirection(direction));
	}
	
	/** If supported, rotates the block in this direction and returns if successful. */
	public static boolean rotateBlock(IBlockAccess world, int x, int y, int z,
	                                  ForgeDirection direction) {
		Block block = world.getBlock(x, y, z);
		if ((block instanceof IRotatable4) &&
		    ((block instanceof IRotatable6) ? isValidDirection(direction)
		                                    : isValidHorizontalDirection(direction))) {
			((IRotatable4)block).setDirection(world, x, y, z, direction);
			return true;
		} else return false;
	}
	
}
