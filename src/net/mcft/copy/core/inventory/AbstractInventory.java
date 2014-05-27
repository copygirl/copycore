package net.mcft.copy.core.inventory;

import net.mcft.copy.core.misc.Enumerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** Base abstract IInventory class which already implements all
 *  methods except for inventory size, getting and setting slots. */
public abstract class AbstractInventory implements IInventoryEnumerable {
	
	@Override
	public String getInventoryName() { return null; }
	@Override
	public boolean hasCustomInventoryName() { return false; }
	
	@Override
	public int getInventoryStackLimit() { return 64; }
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if ((stack == null) || (amount <= 0)) return null;
		
		stack = stack.copy();
		amount = Math.min(amount, stack.stackSize);
		ItemStack result = stack.splitStack(amount);
		
		setInventorySlotContents(slot, stack);
		return result;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) { return null; }
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return ((stack == null) || (stack.stackSize < getInventoryStackLimit()));
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) { return true; }
	
	@Override
	public void markDirty() {  }
	@Override
	public void openInventory() {  }
	@Override
	public void closeInventory() {  }
	
	@Override
	public Enumerator<Element> iterator() {
		return new Enumerator<Element>() {
			private int index = -1;
			@Override
			public boolean moveNext() {
				if (index < getSizeInventory() - 1) {
					index++;
					return true;
				} else return false; 
			}
			@Override
			public Element current() {
				if (index < 0)
					throw new IllegalStateException("moveNext() needs to be called once before current().");
				return new Element(AbstractInventory.this, index);
			}
		};
		
	}
	
}
