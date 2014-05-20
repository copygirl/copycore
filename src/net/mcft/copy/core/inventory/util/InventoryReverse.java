package net.mcft.copy.core.inventory.util;

import net.minecraft.inventory.IInventory;

public class InventoryReverse extends InventoryTransformedIndex {
	
	private final int size;
	
	public InventoryReverse(IInventory base) {
		super(base);
		size = base.getSizeInventory() - 1;
	}
	
	@Override
	protected int transformSlot(int slot) { return (size - slot); }
	
}
