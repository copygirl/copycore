package net.mcft.copy.core.container;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.client.gui.GuiContainerBase;
import net.mcft.copy.core.inventory.slot.InventorySlots;
import net.mcft.copy.core.inventory.slot.SlotBase;
import net.mcft.copy.core.inventory.slot.SlotGroup;
import net.mcft.copy.core.inventory.util.InventoryReverse;
import net.mcft.copy.core.network.packet.PacketOpenGui;
import net.mcft.copy.core.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerBase extends Container {
	
	// To anyone reading through the code of this class,
	// I promise, it made sense to put all of this in here,
	// even if it looks really messy. I hope you can make
	// some sense to you.
	
	public static final String TAG_TITLE     = "title";
	public static final String TAG_LOCALIZED = "localized";
	public static final String TAG_SLOTS     = "slots";
	
	
	public final EntityPlayer player;
	public final IInventory inventory;
	
	public final String title;
	public final boolean titleLocalized;
	
	protected SlotGroup containerInventorySlots;
	protected SlotGroup playerInventorySlots;
	protected SlotGroup playerHotbarSlots;
	
	private int hotbarStart = -1;
	
	private List<SlotGroup> groups = new ArrayList<SlotGroup>();
	private List<SlotGroup> groupLookup = new ArrayList<SlotGroup>();
	private Map<SlotGroup, IInventory> transferMap = new HashMap<SlotGroup, IInventory>();
	
	protected ContainerBase(EntityPlayer player, IInventory inventory) {
		this.player = player;
		this.inventory = inventory;
		
		this.title = inventory.getInventoryName();
		this.titleLocalized = inventory.hasCustomInventoryName();
		
		inventory.openInventory();
	}
	
	@SideOnly(Side.CLIENT)
	public ContainerBase(EntityPlayer player, NBTTagCompound data) {
		this(player, new InventoryBasic(data.getString(TAG_TITLE),
		                                data.getBoolean(TAG_LOCALIZED),
		                                data.getInteger(TAG_SLOTS)));
	}
	
	/** Create a new ContainerBase instance for the server. */
	public static ContainerBase create(EntityPlayer player, IInventory inventory) {
		ContainerBase container = new ContainerBase(player, inventory);
		container.initialize();
		return container;
	}
	
	/** Create a new ContainerBase instance for the
	 *  client using data sent from the server. */
	@SideOnly(Side.CLIENT)
	public static <T extends ContainerBase> T create(Class<T> containerClass, EntityPlayer player, NBTTagCompound data) {
		T container;
		try {
			Constructor<T> constructor = containerClass.getConstructor(EntityPlayer.class, NBTTagCompound.class);
			container = constructor.newInstance(player, data);
		} catch (Exception e) { throw new Error(e); }
		container.initialize();
		return container;
	}
	
	/** Opens the container and sends a packet
	 *  to the player to open a GUI on eir side. */
	public void open() {
		EntityPlayerMP player = (EntityPlayerMP)this.player;
		player.getNextWindowId();
		player.closeContainer();
		player.openContainer = this;
		windowId = player.currentWindowId;
		copycore.channelHandler.sendToPlayer(player, new PacketOpenGui(this));
		addCraftingToCrafters(player);
	}
	
	/** Returns the GUI class to display on the client. */
	@SideOnly(Side.CLIENT)
	public Class<? extends GuiContainerBase> getGuiClass() { return GuiContainerBase.class; }
	
	
	/** Returns the Y position of where the container inventory starts. */
	public int getStart() { return 18; }
	/** Returns height of the container inventory part of the container. */
	public int getHeight() { return inventory.getSizeInventory() * 2; }
	/** Returns the separation between container inventory and player inventory. */
	public int getSeparation() { return 14; }
	
	protected void initialize() {
		setupInventorySlots();
		setupPlayerSlots();
		setupTransferEntries();
	}
	protected void setupInventorySlots() {
		int rows = inventory.getSizeInventory() / 9;
		containerInventorySlots = addGroup(new SlotGroup(SlotBase.class, inventory, 0, 8, getStart(), 9, rows));
	}
	protected void setupPlayerSlots() {
		int y = getStart() + getHeight() + getSeparation();
		playerInventorySlots = addGroup(new SlotGroup(SlotBase.class, player.inventory, 9, 8, y, 9, 3));
		playerHotbarSlots = addGroup(new SlotGroup(SlotBase.class, player.inventory, 0, 8, y + 3 * 18 + 4, 9, 1));
	}
	protected void setupTransferEntries() {
		List<SlotBase> list = new ArrayList<SlotBase>();
		list.addAll(playerInventorySlots.slots);
		list.addAll(playerHotbarSlots.slots);
		IInventory playerReversed = new InventoryReverse(new InventorySlots(list));
		
		addTransferEntry(containerInventorySlots, playerReversed);
		addTransferEntry(playerInventorySlots, containerInventorySlots);
		addTransferEntry(playerHotbarSlots, containerInventorySlots);
	}
	
	
	/** Writes all data the client needs to know
	 *  about the container to an NBT compound. */
	public void writeToCompound(NBTTagCompound data) {
		data.setString(TAG_TITLE, inventory.getInventoryName());
		data.setBoolean(TAG_LOCALIZED, inventory.hasCustomInventoryName());
		data.setInteger(TAG_SLOTS, inventory.getSizeInventory());
	}
	
	
	/** Returns the title of the container, localized if it needs to be. */
	public String getTitleLocalized() {
		return (titleLocalized ? title : StatCollector.translateToLocal(title));
	}
	
	// Slot / SlotGroup related functions
	
	@Override
	protected Slot addSlotToContainer(Slot slot) {
		// Detect start of player hotbar.
		if ((slot.inventory == player.inventory) &&
		    (slot.getSlotIndex() == 0))
			hotbarStart = slot.slotNumber;
		return super.addSlotToContainer(slot);
	}
	
	/** Adds a slot group along with its slots to this container.
	 *  Returns the group. */
	public SlotGroup addGroup(SlotGroup group) {
		for (SlotBase slot : group) {
			addSlotToContainer(slot);
			groupLookup.add(slot.slotNumber, group);
		}
		return group;
	}
	
	/** Adds a new transfer entry. When transferring items (default: shift-click)
	 *  from one group, they'll get moved to the target inventory. */
	public void addTransferEntry(SlotGroup from, IInventory to) {
		transferMap.put(from, to);
	}
	/** Adds a new transfer entry. When transferring items (default: shift-click)
	 *  from one group, they'll get moved to another. */
	public void addTransferEntry(SlotGroup from, SlotGroup... to) {
		List<SlotBase> slots = new ArrayList<SlotBase>();
		for (SlotGroup t : to) slots.addAll(t.slots);
		addTransferEntry(from, new InventorySlots(slots));
	}
	
	/** Returns the slot group the slot is part of, or null if none. */ 
	public SlotGroup findSlotGroup(SlotBase slot) {
		return groupLookup.get(slot.slotNumber);
	}
	
	// Container related functions
	
	/** Returns where an item from a slot should be attempted
	 *  to be moved when transferred (default: shift-clicked). */
	public IInventory getTransferTargets(EntityPlayer player, SlotBase slot) {
		SlotGroup group = findSlotGroup(slot);
		if (group == null) return null;
		IInventory target = transferMap.get(group);
		if (target == null) return null;
		return target;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		SlotBase slot = (SlotBase)inventorySlots.get(slotId);
		if ((slot == null) || !slot.getHasStack() ||
		    !slot.canTakeStack(player)) return null;
		
		IInventory target = getTransferTargets(player, slot);
		if (target == null) return null;
		ItemStack stack = slot.take(player, slot.getStack().stackSize);
		if (stack == null) return null;
		slot.putStack(InventoryUtils.insertStack(target, stack, false));
		
		return stack;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int button, int special, EntityPlayer player) {
		SlotBase slot = null;
		ItemStack holding = player.inventory.getItemStack();
		if ((slotId >= 0) && (slotId < inventorySlots.size()))
			slot = (SlotBase)inventorySlots.get(slotId);
		if (slot != null) {
			ItemStack stack = slot.getStack();
			if (special == 0) {
				player.inventory.setItemStack(slot.onClick(player, holding, (button == 1)));
				return stack;
			} else if (special == 1)
				return transferStackInSlot(player, slotId);
			else if ((special == 2) && (button >= 0) && (button < 9)) {
				// Override default hotbar switching to make sure
				// the items can be taken and put into those slots.
				if (hotbarStart < 0) return null;
				Slot slot2 = (Slot)inventorySlots.get(hotbarStart + button);
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
