package net.mcft.copy.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public final class DirectionUtils {
	
	private DirectionUtils() {  }
	
	/** Returns if this directions is a valid direction (not UNKNOWN). */
	public static boolean isValid(ForgeDirection direction) {
		return (direction.ordinal() < ForgeDirection.UNKNOWN.ordinal());
	}
	
	/** Returns if this directions is a valid horizontal direction (NORTH, SOUTH, EAST, WEST). */
	public static boolean isHorizontal(ForgeDirection direction) {
		return ((direction.ordinal() >= ForgeDirection.NORTH.ordinal()) && isValid(direction));
	}
	
	/** Gets the ForgeDirection from the direction an entity is facing. */
	public static ForgeDirection getOrientation(Entity entity) {
		int dir = MathHelper.floor_double(entity.rotationYaw * 4.0 / 360.0 + 0.5) & 3;
		switch (dir) {
			case 1: return ForgeDirection.WEST;
			case 2: return ForgeDirection.NORTH;
			case 3: return ForgeDirection.EAST;
			default: return ForgeDirection.SOUTH;
		}
	}
	
	/** Gets the direction from a ForgeDirection in degrees. */
	public static int getRotation(ForgeDirection dir) {
		if (dir == ForgeDirection.WEST) return 90;
		else if (dir == ForgeDirection.NORTH) return 180;
		else if (dir == ForgeDirection.EAST) return 270;
		else return 0;
	}
	
	/** Returns the difference between the two angles in degrees (-180 to 180). */
	public static double angleDifference(double angle1, double angle2) {
		return ((angle2 - angle1) % 360 + 540) % 360 - 180;
	}
	
	/** Returns the angle between two points on a plane (-180 to 180). */
	public static double angleBetween(double x1, double y1, double x2, double y2) {
		return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
	}
	
}
