package net.mcft.copy.core.inventory.slot;

import net.mcft.copy.core.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotBase extends Slot {
	
	public SlotBase(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	
	/** Returns the item type of the stack in this slot. */
	public Item getItem() { return (getHasStack() ? getStack().getItem() : null); }
	
	/** Returns if the player can set the slot to this stack. */
	public boolean isItemValid(EntityPlayer player, ItemStack stack) {
		return ((stack == null) || (stack.stackSize <= getSlotStackLimit()));
	}
	
	/** Returns the maximum amount of items of this
	 *  type can be in this slot (can be above 64). */
	public int getMaxStackSize(ItemStack stack) {
		return Math.min(getSlotStackLimit(), stack.getMaxStackSize());
	}
	
	
	/** Takes items out of the slot. */
	public ItemStack take(EntityPlayer player, int amount) {
		ItemStack stack = getStack();
		if ((stack == null) || (amount <= 0)) return null;
		amount = Math.min(amount, stack.stackSize);
		ItemStack result = StackUtils.copy(stack, amount);
		stack = StackUtils.copy(stack, stack.stackSize - amount);
		if (!isItemValid(player, stack)) return null;
		putStack(stack);
		return result;
	}
	
	/** Inserts items into the slot. */
	public ItemStack insert(EntityPlayer player, ItemStack insert) {
		ItemStack stack = getStack();
		if ((stack != null) && !StackUtils.equals(stack, insert, false))
			return insert;
		int stackSize = ((stack != null) ? stack.stackSize : 0);
		int amount = Math.min(insert.stackSize, getMaxStackSize(insert) - stackSize);
		if (amount <= 0) return insert;
		stack = StackUtils.copy(insert, stackSize + amount);
		if (!isItemValid(player, stack)) return null;
		putStack(stack);
		return StackUtils.copy(insert, insert.stackSize - amount);
	}
	
	
	/** Called when the slot is right or left clicked, returns
	 *  which item the player should be holding afterwards. */
	public ItemStack onClick(EntityPlayer player, ItemStack holding, boolean rightclick) {
		if (holding == null) {
			ItemStack stack = getStack();
			if (stack != null) {
				int amount = (rightclick ? (stack.stackSize / 2) : stack.stackSize);
				amount = Math.min(Math.min(amount, stack.getMaxStackSize()),
				                  player.inventory.getInventoryStackLimit());
				return take(player, amount);
			} else return null;
		} else if (!rightclick)
			return insert(player, holding);
		else if (insert(player, StackUtils.copy(holding, 1)) == null)
			return StackUtils.copy(holding, holding.stackSize - 1);
		else return holding;
	}
	
	/** Called when the pick block key (usually middle click)
	 *  is pressed. Returns which item the player should be
	 *  holding afterwards (usually a copy of the stack). */
	public ItemStack pick(EntityPlayer player) {
		return ItemStack.copyItemStack(getStack());
	}
	
	/** Called when the drop key (usually Q) is pressed.
	 *  Returns which item should be dropped. The dropAll
	 *  parameter is true when the shift key is held. */
	public ItemStack drop(EntityPlayer player, boolean dropAll) {
		ItemStack stack = getStack();
		if (stack == null) return null;
		return take(player,
				(dropAll ? Math.min(stack.stackSize,
				                    stack.getMaxStackSize()) : 1));
	}
	
	// Slot methods
	
	@Override
	public boolean isItemValid(ItemStack stack) { return isItemValid(null, stack); }
	
	@Override
	public boolean canTakeStack(EntityPlayer player) { return isItemValid(player, null); }
	
	@Override
	public ItemStack decrStackSize(int amount) { return take(null, amount); }
	
}
