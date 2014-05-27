package net.mcft.copy.core.base;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TileEntityBase extends TileEntity {
	
	private String customName = null;
	private boolean brokenInCreative = false;
	
	/** Returns the custom name of the tile entity, usually from being . */
	public String getCustomName() { return customName; }
	
	/** Returns if a custom name can be set on this tile entity. */
	public boolean canSetCustomName() { return false; }
	
	/** Sets the custom name of this tile entity, if it can be set. */
	public void setCustomName(String title) { if (canSetCustomName()) customName = title; }
	
	// Actions passed from blocks
	
	/** Called when a block is placed by a player. May set data of the
	 *  tile entity from the item stack, like storing the custom name. */
	public void onBlockPlaced(EntityLivingBase entity, ItemStack stack,
	                          ForgeDirection side, float hitX, float hitY, float hitZ) {
		if (stack.hasDisplayName())
			setCustomName(stack.getDisplayName());
	}
	
	/** Called then the block is activated (default: right click).
	 *  Returns if an action happened (causes hand to animate) */
	public boolean onBlockActivated(EntityPlayer player, ForgeDirection side,
	                                float hitX, float hitY, float hitZ) { return false; }
	
	/** Returns which  (default: middle mouse). */
	public ItemStack onPickBlock(ItemStack defaultBlock, MovingObjectPosition target) { return defaultBlock; }
	
	/** Called when a block is attempted to be broken by a player.
	 *  Returns if the block should actually be broken. */
	public final boolean onBlockBreak(EntityPlayer player) {
		brokenInCreative = player.capabilities.isCreativeMode;
		return onBlockBreak(player, brokenInCreative);
	}
	/** Called when a block is attempted to be broken by a player.
	 *  Returns if the block should actually be broken. */
	public boolean onBlockBreak(EntityPlayer player, boolean brokenInCreative) {
		return true;
	}
	
	/** Called after the block is destroyed, drops contents etc. */
	public final void onBlockDestroyed() {
		onBlockDestroyed(brokenInCreative);
		brokenInCreative = false;
	}
	/** Called after the block is destroyed, drops contents etc. */
	public void onBlockDestroyed(boolean brokenInCreative) {  }
	
	/** Called when the block drops as an item, allows modification of items dropped. */
	public void getBlockDrops(List<ItemStack> drops, int fortune) {  }
	
	/** Called before the tile entity is being rendered as an item,
	 *  allows setting values in the tile entity, which is then rendered. */
	@SideOnly(Side.CLIENT)
	public void onRenderAsItem(ItemStack stack) {  }
	
	// Loading, saving and syncing
	
	/** Handles writing data to both the save and description packet. */
	public void write(NBTTagCompound compound) {  }
	/** Handles read data from both the save and description packet. */
	public void read(NBTTagCompound compound) {  }

	/** Handles writing data to the save. */
	public void writeToSave(NBTTagCompound compound) {  }
	/** Handles read data from the save. */
	public void readFromSave(NBTTagCompound compound) {  }
	
	/** Handles writing data to the description packet, to be sent to the client. */
	public void writeToDescriptionPacket(NBTTagCompound compound) {  }
	/** Handles read data from the description packet, sent from the server. */
	public void readFromDescriptionPacket(NBTTagCompound compound) {  }
	
	/** Returns if this tile entity has a "description packet". */
	public boolean hasDescriptionPacket() { return false; }
	
	
	@Override
	public final void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		write(compound);
		writeToSave(compound);
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		read(compound);
		readFromSave(compound);
	}
	
	@Override
	public final Packet getDescriptionPacket() {
		if (!hasDescriptionPacket()) return null;
		NBTTagCompound compound = new NBTTagCompound();
		write(compound);
		writeToDescriptionPacket(compound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, compound);
	}
	
	@Override
	public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		if (!hasDescriptionPacket()) return;
		NBTTagCompound compound = packet.func_148857_g();
		read(compound);
		readFromDescriptionPacket(compound);
	}
	
}
