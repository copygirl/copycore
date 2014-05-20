package net.mcft.copy.core.inventory;

import net.mcft.copy.core.misc.IEnumerable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IInventoryEnumerable extends IInventory, IEnumerable<IInventoryEnumerable.Element> {
	
	// No additional fields, just an interface for an enumerable inventory.
	
	public static class Element {
		
		private final IInventory inventory;
		private final int slot;
		
		public Element(IInventory inventory, int slot) {
			this.inventory = inventory;
			this.slot = slot;
		}
		
		public ItemStack get() {
			return inventory.getStackInSlot(slot);
		}
		public void set(ItemStack stack) {
			inventory.setInventorySlotContents(slot, stack);
		}
		public boolean isValid(ItemStack stack) {
			return inventory.isItemValidForSlot(slot, stack);
		}
		
	}
	
}
