package net.mcft.copy.core.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotProtected extends SlotBase {
	
	public SlotProtected(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean isItemValid(EntityPlayer player, ItemStack stack) { return false; }
	
}
