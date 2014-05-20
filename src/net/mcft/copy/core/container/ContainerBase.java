package net.mcft.copy.core.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mcft.copy.core.inventory.slot.InventorySlots;
import net.mcft.copy.core.inventory.slot.SlotBase;
import net.mcft.copy.core.inventory.slot.SlotGroup;
import net.mcft.copy.core.util.InventoryUtils;
import net.mcft.copy.core.util.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {
	
	public final EntityPlayer player;
	public final IInventory inventory;
	
	private int startHotbar = -1;
	
	private List<SlotGroup> groups = new ArrayList<SlotGroup>();
	private List<SlotGroup> groupLookup = new ArrayList<SlotGroup>();
	private Map<SlotGroup, SlotGroup> transferMap = new HashMap<SlotGroup, SlotGroup>();
	
	public ContainerBase(EntityPlayer player, IInventory inventory) {
		this.player = player;
		this.inventory = inventory;
		
		inventory.openInventory();
	}
	
	/** Adds a slot group along with its slots to this container. */
	public void addGroup(SlotGroup group) {
		for (SlotBase slot : group) {
			addSlotToContainer(slot);
			groupLookup.add(slot.slotNumber, group);
		}
	}
	
	/** Adds a new transfer entry. When transferring items (default: shift-click)
	 *  from one group, they'll get moved to another. */
	public void addTransferEntry(SlotGroup from, SlotGroup to, boolean bidirectional) {
		transferMap.put(from, to);
		if (bidirectional) transferMap.put(to, from);
	}
	
	/** Returns the slot group the slot is part of, or null if none. */ 
	public SlotGroup findSlotGroup(SlotBase slot) {
		return groupLookup.get(slot.slotNumber);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		SlotBase slot = (SlotBase)inventorySlots.get(slotId);
		if ((slot == null) || !slot.getHasStack()) return null;
		
		SlotGroup group = findSlotGroup(slot);
		if (group == null) return null;
		
		ItemStack stack = slot.getStack();
		IInventory targetSlots = new InventorySlots(group.slots);
		slot.putStack(InventoryUtils.insertStack(targetSlots, stack, false));
		
		return stack;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int button, int special, EntityPlayer player) {
		Slot slot = null;
		if ((slotId >= 0) && (slotId < inventorySlots.size()))
			slot = (Slot)inventorySlots.get(slotId);
		if (slot != null) {
			if (special == 0) {
				if ((button == 0) || (button == 1)) {
					// Override default behavior to use putStack
					// instead of manipulating stackSize directly.
					ItemStack slotStack = slot.getStack();
					ItemStack holding = player.inventory.getItemStack();
					if ((slotStack != null) && (holding != null) &&
					    ((holding == null) ? slot.canTakeStack(player) : slot.isItemValid(holding)) &&
					    StackUtils.matches(slotStack, holding)) {
						int amount = ((button == 0) ? holding.stackSize : 1);
						amount = Math.min(amount, slot.getSlotStackLimit() - slotStack.stackSize);
						amount = Math.min(amount, slotStack.getMaxStackSize() - slotStack.stackSize);
						if (amount > 0) {
							if ((holding.stackSize -= amount) <= 0)
								player.inventory.setItemStack(null);
							slot.putStack(StackUtils.copy(slotStack, slotStack.stackSize + amount));
						}
						return slotStack;
					}
				}
			} else if (special == 1) {
				// Override default shift-click so it doesn't call
				// retrySlotClick, whatever that was meant for.
				return (slot.canTakeStack(player) ? transferStackInSlot(player, slotId) : null);
			} else if ((special == 2) && (button >= 0) && (button < 9)) {
				// Override default hotbar switching to make sure
				// the items can be taken and put into those slots.
				if (startHotbar < 0) return null;
				Slot slot2 = (Slot)inventorySlots.get(startHotbar + button);
				ItemStack stack = slot.getStack();
				if (!slot2.canTakeStack(player) ||
				    ((stack != null) && !slot2.isItemValid(stack)))
					return null;
			}
		}
		return super.slotClick(slotId, button, special, player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventory.closeInventory();
	}
	
	/** Sends a packet to all players looking at
	 *  this GUI for them to update something. */
	public void sendUpdate(int id, int value) {
		for (Object c : crafters)
			((ICrafting)c).sendProgressBarUpdate(this, id, value);
	}
	/** Sends a packet to all players looking at this GUI for them to update
	 *  something, but only if the value is different from the previous one. */
	public void sendUpdateIfChanged(int id, int value, int previousValue) {
		if (value != previousValue)
			sendUpdate(id, value);
	}
	
}
