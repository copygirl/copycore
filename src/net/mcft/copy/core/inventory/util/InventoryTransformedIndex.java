package net.mcft.copy.core.inventory.util;

import net.mcft.copy.core.inventory.InventoryWrapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/** Utility class which serves as a base for InventoryRange and InventoryReverse. */
public abstract class InventoryTransformedIndex extends InventoryWrapper {
	
	public InventoryTransformedIndex(IInventory base) {
		super(base);
	}
	
	protected abstract int transformSlot(int slot);
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(transformSlot(slot));
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(transformSlot(slot), stack);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return super.getStackInSlotOnClosing(transformSlot(slot));
	}
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return super.decrStackSize(transformSlot(slot), amount);
	}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return super.isItemValidForSlot(transformSlot(slot), stack);
	}
	
}
