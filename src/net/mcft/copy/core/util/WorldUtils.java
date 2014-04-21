package net.mcft.copy.core.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public final class WorldUtils {
	
	private WorldUtils() {  }
	
	public static <T extends TileEntity> T getTileEntity(IBlockAccess world, int x, int y, int z,
	                                                     Class<T> tileEntityClass) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		return (tileEntityClass.isInstance(tileEntity) ? (T)tileEntity : null);
	}
	
}
