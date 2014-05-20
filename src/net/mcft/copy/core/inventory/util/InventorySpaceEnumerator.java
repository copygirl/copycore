package net.mcft.copy.core.inventory.util;

import java.util.LinkedList;
import java.util.List;

import net.mcft.copy.core.api.ItemIdentifier;
import net.mcft.copy.core.inventory.IInventoryEnumerable;
import net.mcft.copy.core.inventory.IInventoryEnumerable.Element;
import net.mcft.copy.core.misc.Enumerator;
import net.minecraft.item.ItemStack;

public class InventorySpaceEnumerator extends Enumerator<Element> {
	
	private final ItemIdentifier item;
	private final List<Element> emptySlots = new LinkedList<Element>();
	
	private Enumerator<Element> enumerator;
	private boolean enumeratingEmptySlots = false;
	
	public InventorySpaceEnumerator(IInventoryEnumerable inventory, ItemIdentifier item) {
		this.item = item;
		enumerator = Enumerator.of(inventory);
	}
	
	@Override
	public boolean moveNext() {
		if (!enumeratingEmptySlots) {
			while (enumerator.moveNext()) {
				ItemStack stack = current().get();
				if (stack == null)
					emptySlots.add(current());
				else if (item.matches(stack))
					return true;
			}
			enumeratingEmptySlots = true;
			enumerator = Enumerator.of(emptySlots);
		}
		return enumerator.moveNext();
	}
	
	@Override
	public Element current() { return enumerator.current(); }
	
}