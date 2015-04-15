package net.mcft.copy.core.addon;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@Addon({ "MineTweaker3" })
@ZenClass("mods.copycore.Tweaks")
public class MineTweakerAddon {
	
	public MineTweakerAddon() {
		MineTweakerAPI.registerClass(getClass());
	}
	
	
	@ZenMethod
	public static void setDurability(IItemStack item, int durability) {
		Item i = getItemFromIItemStack(item);
		if (i.getHasSubtypes())
			throw new IllegalArgumentException("item has sub-items");
		MineTweakerAPI.apply(new SetDurabilityAction(i, durability));
	}
	@ZenMethod
	public static void setDurability(IItemStack[] items, int durability) {
		for (IItemStack item : items)
			setDurability(item, durability);
	}
	@ZenMethod
	public static void setArmorDurability(IItemStack head, IItemStack chestplate,
	                                      IItemStack leggings, IItemStack boots, int durabilityFactor) {
		setDurability(head, durabilityFactor * 11);
		setDurability(chestplate, durabilityFactor * 16);
		setDurability(leggings, durabilityFactor * 15);
		setDurability(boots, durabilityFactor * 13);
	}
	
	@ZenMethod
	public static void setMaxStackSize(IItemStack item, int stackSize) {
		Item i = getItemFromIItemStack(item);
		if (i.isDamageable())
			throw new IllegalArgumentException("item is damageable");
		MineTweakerAPI.apply(new SetMaxStackSizeAction(i, stackSize));
	}
	@ZenMethod
	public static void setMaxStackSize(IItemStack[] items, int stackSize) {
		for (IItemStack item : items)
			setMaxStackSize(item, stackSize);
	}
	
	
	private static Item getItemFromIItemStack(IItemStack item) {
		return ((ItemStack)item.getInternal()).getItem();
	}
	
	
	private static class SetDurabilityAction implements IUndoableAction {
		private final Item item;
		private final int durability;
		private int previousDurability;
		public SetDurabilityAction(Item item, int durability) {
			this.item = item;
			this.durability = durability;
		}
		@Override public String describe() { return "Setting durability of " + item + " to " + durability; }
		@Override public String describeUndo() { return "Resetting durability of " + item; }
		@Override public boolean canUndo() { return true; }
		@Override public void apply() { previousDurability = item.getMaxDamage(); item.setMaxDamage(durability); }
		@Override public void undo() { item.setMaxDamage(previousDurability); }
		@Override public Object getOverrideKey() { return null; }
	}
	
	private static class SetMaxStackSizeAction implements IUndoableAction {
		private final Item item;
		private final int maxStackSize;
		private int previousMaxStackSize;
		public SetMaxStackSizeAction(Item item, int maxStackSize) {
			this.item = item;
			this.maxStackSize = maxStackSize;
		}
		@Override public String describe() { return "Setting max stack size of " + item + " to " + maxStackSize; }
		@Override public String describeUndo() { return "Resetting max stack size of " + item; }
		@Override public boolean canUndo() { return true; }
		@Override public void apply() { previousMaxStackSize = item.getMaxDamage(); item.setMaxStackSize(maxStackSize); }
		@Override public void undo() { item.setMaxStackSize(previousMaxStackSize); }
		@Override public Object getOverrideKey() { return null; }
	}
	
}
