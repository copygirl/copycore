package net.mcft.copy.core.inventory;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

/** Simplified IInventory wrapper around an item stack list or array. */
public class InventoryStacks extends AbstractInventory {
	
	public final List<ItemStack> stacks;

	public InventoryStacks(List<ItemStack> stacks) {
		this.stacks = stacks;
	}
	public InventoryStacks(ItemStack[] stacks) {
		this.stacks = Arrays.asList(stacks);
	}
	
	@Override
	public int getSizeInventory() { return stacks.size(); }
	
	@Override
	public ItemStack getStackInSlot(int slot) { return stacks.get(slot); }
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) { stacks.set(slot, stack); }
	
}
