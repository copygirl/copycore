package net.mcft.copy.core.base;

import java.util.ArrayList;

import net.mcft.copy.core.misc.BlockLocation;
import net.mcft.copy.core.util.RegistryUtils;
import net.mcft.copy.core.util.RegistryUtils.IRegistrable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class BlockTileEntityBase extends BlockBase implements ITileEntityProvider {
	
	private TileEntityBase tileEntityBeforeRemoved;
	
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
		tileEntityBeforeRemoved = getTileEntity(world, x, y, z);
		tileEntityBeforeRemoved.onBlockDestroyed();
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntityBase tileEntity = BlockLocation.get(world, x, y, z).getTileEntity(getTileEntityClass());
		// Tile entity might already be removed. (Damn that inconsistency!)
		// In this case let's use the value from onBlockPreDestroy:
		if (tileEntity == null) tileEntity = tileEntityBeforeRemoved;
		
		ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, metadata, fortune);
		tileEntity.getBlockDrops(drops, fortune);
		return drops;
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
	
	// IRegistrable
	
	@Override
	public <T extends IRegistrable> T register() {
		super.register();
		registerTileEntity();
		return (T)this;
	}
	
	protected void registerTileEntity() {
		GameRegistry.registerTileEntity(getTileEntityClass(),
				RegistryUtils.getActiveModId() + ":" + getName());
	}
	
}
