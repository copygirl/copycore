package net.mcft.copy.core.base;

import net.mcft.copy.core.misc.BlockLocation;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockTileEntityBase extends BlockBase implements ITileEntityProvider {
	
	private ForgeDirection side;
	private float hitX, hitY, hitZ;
	
	public BlockTileEntityBase(Material material) {
		super(material);
	}
	
	/** Returns the tile entity class of this block. */
	public abstract Class<? extends TileEntityBase> getTileEntityClass();
	
	/** Returns the tile entity for this block at that position in the world. */
	public TileEntityBase getTileEntity(World world, int x, int y, int z) {
		return BlockLocation.get(world, x, y, z).getTileEntityStrict(getTileEntityClass());
	}
	
	// Pass actions to tile entity
	
	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int event, int value) {
		return getTileEntity(world, x, y, z).receiveClientEvent(event, value);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z,
	                         int side, float hitX, float hitY, float hitZ,
	                         int metadata) {
		this.side = ForgeDirection.getOrientation(side);
		this.hitX = hitX; this.hitY = hitY; this.hitZ = hitZ;
		return metadata;
	}
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
	                            EntityLivingBase entity, ItemStack stack) {
		getTileEntity(world, x, y, z).onBlockPlaced(entity, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!getTileEntity(world, x, y, z).onBlockBreak(player)) return false;
		return super.removedByPlayer(world, player, x, y, z);
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		getTileEntity(world, x, y, z).onBlockDestroyed();
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		TileEntityBase tileEntity = getTileEntity(world, x, y, z);
		ItemStack defaultBlock = super.getPickBlock(target, world, x, y, z);
		return ((tileEntity != null) ? tileEntity.onPickBlock(defaultBlock, target) : defaultBlock);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
	                                int side, float hitX, float hitY, float hitZ) {
		return getTileEntity(world, x, y, z).onBlockActivated(
				player, ForgeDirection.getOrientation(side), hitX, hitY, hitZ);
	}
	
	// ITileEntityProvider implementation
	
	@Override
	public TileEntityBase createNewTileEntity(World world, int metadata) {
		TileEntityBase tileEntity = null;
		try { return getTileEntityClass().newInstance(); }
		catch (Exception e) {  }
		return tileEntity;
	}
	
}
