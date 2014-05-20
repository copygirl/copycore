package net.mcft.copy.core.inventory.util;

import net.minecraft.inventory.IInventory;

public class InventoryRange extends InventoryTransformedIndex {
	
	public final int start, length;
	
	public InventoryRange(IInventory base, int start, int length) {
		super(base);
		this.start = start;
		this.length = length;
		if ((start < 0) || (length < 0) ||
		    (start + length >= base.getSizeInventory()))
			throw new IllegalArgumentException(String.format(
					"The range [%s-%s] is not a valid range of the inventory with size %s",
					start, start + length, base.getSizeInventory()));
	}
	
	@Override
	public int getSizeInventory() { return length; }
	
	@Override
	protected int transformSlot(int slot) { return (slot + start); }
	
}
