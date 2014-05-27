package net.mcft.copy.core.inventory.util;

import net.mcft.copy.core.inventory.InventoryWrapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryCombined extends InventoryWrapper {
	
	public final IInventory[] inventories;
	public final int size;
	
	private final int[] inventorySizes;
	
	public InventoryCombined(IInventory... inventories) {
		super(inventories[0]);
		this.inventories = inventories;
		int size = 0;
		inventorySizes = new int[inventories.length];
		for (int i = 0; i < inventories.length; i++)
			size += (inventorySizes[i] = inventories[i].getSizeInventory());
		this.size = size;
	}
	
	private int tempSlot;
	private IInventory getInventoryAndSlot(int slot) {
		tempSlot = slot;
		for (int i = 0; ; i++) {
			if (tempSlot < inventorySizes[i])
				return inventories[i];
			tempSlot -= inventorySizes[i];
		}
	}
	
	@Override
	public int getSizeInventory() { return size; }
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return getInventoryAndSlot(slot).getStackInSlot(tempSlot);
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		getInventoryAndSlot(slot).setInventorySlotContents(tempSlot, stack);
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getInventoryAndSlot(slot).getStackInSlotOnClosing(tempSlot);
	}
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return getInventoryAndSlot(slot).decrStackSize(tempSlot, amount);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return getInventoryAndSlot(slot).isItemValidForSlot(tempSlot, stack);
	}
	
}
