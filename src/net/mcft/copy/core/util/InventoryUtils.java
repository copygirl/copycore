package net.mcft.copy.core.util;

import net.mcft.copy.core.api.ItemIdentifier;
import net.mcft.copy.core.inventory.IInventoryEnumerable;
import net.mcft.copy.core.inventory.IInventoryEnumerable.Element;
import net.mcft.copy.core.inventory.InventoryWrapper;
import net.mcft.copy.core.inventory.util.InventorySpaceEnumerator;
import net.mcft.copy.core.misc.Enumerator;
import net.mcft.copy.core.misc.IEnumerable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InventoryUtils {
	
	private InventoryUtils() {  }
	
	/** Returns an enumerable version of the inventory. */
	public static IInventoryEnumerable asIterable(IInventory inventory) {
		return ((inventory instanceof IInventoryEnumerable)
				? (IInventoryEnumerable)inventory
				: new InventoryWrapper(inventory));
	}
	
	/** Returns an enumerable that will look for places to insert an item into.
	 *  First it will look for stacks of the same type as the item and return those.
	 *  While doing so it will keep track of a list of empty slots and return those afterwards. */
	public static IEnumerable<Element> lookupSpaceFor(final IInventory inventory, final ItemIdentifier item) {
		return new IEnumerable<Element>() {
			@Override public Enumerator<Element> iterator() {
				return new InventorySpaceEnumerator(InventoryUtils.asIterable(inventory), item);
			} };
	}
	
	/** Attempts to insert a stack into an inventory. Returns what couldn't be inserted. */
	public static ItemStack insertStack(IInventory inventory, ItemStack stack, boolean simulate) {
		ItemIdentifier identifier = new ItemIdentifier(stack);
		int count = stack.stackSize;
		for (Element e : lookupSpaceFor(inventory, identifier)) {
			ItemStack current = e.get();
			int amount = Math.min(inventory.getInventoryStackLimit() -
			                      ((current != null) ? current.stackSize : 0), count);
			if (amount <= 0) continue;
			ItemStack test = identifier.createStack(current.stackSize + amount);
			if (!e.isValid(test)) continue;
			if (!simulate) e.set(test);
			if ((count -= amount) <= 0) return null;
		}
		return identifier.createStack(count);
	}
	
}
