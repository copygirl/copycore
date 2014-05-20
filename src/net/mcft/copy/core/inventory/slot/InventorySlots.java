package net.mcft.copy.core.inventory.slot;

import java.util.List;

import net.mcft.copy.core.inventory.AbstractInventory;
import net.minecraft.item.ItemStack;

/** Simplified IInventory wrapper around an item stack list or array. */
public class InventorySlots extends AbstractInventory {
	
	// TODO: InventorySlots is limited to IInventory methods and
	//       doesn't make use of some of the slots' abilities.
	
	public final List<SlotBase> slots;
	
	public InventorySlots(List<SlotBase> stacks) {
		this.slots = stacks;
	}
	
	@Override
	public int getSizeInventory() { return slots.size(); }
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots.get(slot).getStack();
	}
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots.get(slot).putStack(stack);
	}
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return slots.get(slot).decrStackSize(amount);
	}
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slots.get(slot).isItemValid(stack);
	}
	
}
