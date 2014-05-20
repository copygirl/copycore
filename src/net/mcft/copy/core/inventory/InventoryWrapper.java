package net.mcft.copy.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/** Simple wrapper class around an existing IInventory for easy overriding.
 *  Also allows for iteration over the inventory out of the box. */
 public class InventoryWrapper extends AbstractInventory {
	
	public final IInventory base;
	
	public InventoryWrapper(IInventory base) {
		this.base = base;
	}
	
	@Override
	public String getInventoryName() { return base.getInventoryName(); }
	@Override
	public boolean hasCustomInventoryName() { return base.hasCustomInventoryName(); }
	
	@Override
	public int getSizeInventory() { return base.getSizeInventory(); }
	@Override
	public int getInventoryStackLimit() { return base.getInventoryStackLimit(); }
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return base.getStackInSlot(slot);
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		base.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return base.getStackInSlotOnClosing(slot);
	}
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return base.decrStackSize(slot, amount);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return base.isItemValidForSlot(slot, stack);
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return base.isUseableByPlayer(player);
	}
	
	@Override
	public void markDirty() { base.markDirty(); }
	@Override
	public void openInventory() { base.openInventory(); }
	@Override
	public void closeInventory() { base.closeInventory(); }

}
