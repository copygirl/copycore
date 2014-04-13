package net.mcft.copy.core.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public final class StackUtils {
	
	private StackUtils() {  }
	
	/** Gets the real damage value stored in the item stack,
	 *  instead of what the item's getDamage method would return. */
	public static int getDamage(ItemStack stack) {
		return Items.diamond.getDamage(stack);
	}
	
	// Matching related functions
	
	/** Returns if the two item stacks are equal.
	 *  Checks if the item is the same and optionally NBT data, damage and stack size. */
	public static boolean equals(ItemStack stack1, ItemStack stack2,
	                             boolean matchStackSize, boolean matchNBT, boolean matchDamage) {
		return ((stack1 == stack2) ||
		        ((stack1 != null) && (stack2 != null) &&
		         (stack1.getItem() == stack2.getItem()) &&
		         (!matchStackSize || (stack1.stackSize == stack2.stackSize)) &&
		         (!matchNBT || isNbtDataEqual(stack1, stack2)) &&
		         (!matchDamage || (getDamage(stack1) == getDamage(stack2)))));
	}
	/** Returns if the two item stacks are equal.
	 *  Checks if the item and damage are the same and optionally stack size and NBT data. */
	public static boolean equals(ItemStack stack1, ItemStack stack2,
	                             boolean matchStackSize, boolean matchNBT) {
		return equals(stack1, stack2, matchStackSize, matchNBT, true);
	}
	/** Returns if the two item stacks are equal.
	 *  Checks if the item, damage and NBT data are the same and optionally stack size. */
	public static boolean equals(ItemStack stack1, ItemStack stack2,
	                             boolean matchStackSize) {
		return equals(stack1, stack2, matchStackSize, true, true);
	}
	/** Returns if the two item stacks are equal.
	 *  Checks if the item, stack size, damage and NBT data are the same. */
	// This should, in theory, be the same as ItemStack.areItemStacksEqual.
	public static boolean equals(ItemStack stack1, ItemStack stack2) {
		return equals(stack1, stack2, true, true, true);
	}
	
	/** Replacement for {@link ItemStack.areItemStackTagsEqual}. <br>
	 *  Allows items with empty NBT compounds to be equal to ones without NBT compound. */
	public static boolean isNbtDataEqual(ItemStack stack1, ItemStack stack2) {
		NBTTagCompound compound1 = stack1.getTagCompound();
		NBTTagCompound compound2 = stack2.getTagCompound();
		return ((compound1 == null) ? ((compound2 == null) || compound2.hasNoTags())
		                            : ((compound2 == null) ? compound1.hasNoTags()
		                                                   : compound1.equals(compound2)));
	}
	
	/** Returns if the item stack matches the match stack. Works the same as equals, except for: <br>
	 *  - Stack size is always ignored. <br>
	 *  - If the match stack's damage is the wildcard value, it doesn't have to match. <br>
	 *  - If the match stack doesn't have an NBT compound, it doesn't have to match. <br>
	 *  - If the match stack's NBT compound is empty, the stack can't have any NBT data. */
	public static boolean matches(ItemStack match, ItemStack stack) {
		if (match == null) return (stack == null);
		boolean matchDamage = (getDamage(match) != OreDictionary.WILDCARD_VALUE);
		boolean matchHasNbt = match.hasTagCompound();
		boolean matchNbtEmpty = (matchHasNbt && match.getTagCompound().hasNoTags());
		return (equals(match, stack, false, (matchHasNbt && !matchNbtEmpty), matchDamage) &&
		        (!matchNbtEmpty || !stack.hasTagCompound()));
	}
	
	// NBT related functions
	
	/** Gets an NBT tag from the ItemStack's custom NBT data. */
	public static NBTBase getTag(ItemStack stack, String... tags) {
		if (!stack.hasTagCompound()) return null;
		String tag = null;
		NBTTagCompound compound = stack.getTagCompound();
		for (int i = 0; i < tags.length; i++) {
			tag = tags[i];
			if (!compound.hasKey(tag)) return null;
			if (i == tags.length - 1) break;
			compound = compound.getCompoundTag(tag);
		}
		return compound.getTag(tag);
	}
	
	/** Gets the type of a tag from the ItemStack's custom NBT data. <br>
	 *  See {@link NBTBase#NBTTypes} for possible return values. <br>
	 *  Returns null if the tag doesn't exist. */
	public static String getType(ItemStack stack, String... tags) {
		NBTBase tag = getTag(stack, tags);
		return ((tag != null) ? NBTBase.NBTTypes[tag.getId()] : null);
	}
	
	/** Gets a value from the ItemStack's custom NBT data. Example: <br>
	 *  <code> int color = StackUtils.get(stack, -1, "display", "color"); </code> <br>
	 *  Returns defaultValue if any parent compounds or the value tag don't exist. */
	public static <T> T get(ItemStack stack, T defaultValue, String... tags) {
		NBTBase tag = getTag(stack, tags);
		return ((tag != null) ? NbtUtils.<T>getTagValue(tag) : defaultValue);
	}
	
	
	/** Sets a tag in the ItemStack's custom NBT data. <br>
	 *  Creates parent compounds if they don't exist. */
	public static void set(ItemStack stack, NBTBase nbtTag, String... tags) {
		String tag = null;
		NBTTagCompound compound = stack.getTagCompound();
		if (compound == null)
			stack.setTagCompound((compound = new NBTTagCompound()));
		for (int i = 0; i < tags.length; i++) {
			tag = tags[i];
			if (i == tags.length - 1) break;
			if (!compound.hasKey(tag)) {
				NBTTagCompound newCompound = new NBTTagCompound();
				compound.setTag(tag, newCompound);
				compound = newCompound;
			} else compound = compound.getCompoundTag(tag);
		}
		compound.setTag(tag, nbtTag);
	}
	
	/** Sets a value in the ItemStack's custom NBT data. Example: <br>
	 *  <code> StackUtils.set(stack, 0xFF0000, "display", "color"); </code> <br>
	 *  Creates parent compounds if they don't exist. */
	public static <T> void set(ItemStack stack, T value, String... tags) {
		set(stack, NbtUtils.createTag(value), tags);
	}
	
	
	/** Returns if the tag exists in the ItemStack's custom NBT data. Example: <br>
	 *  <code> if (StackUtils.has(stack, "display", "color")) ... </code> */
	public static boolean has(ItemStack stack, String... tags) {
		return (getTag(stack, tags) != null);
	}
	
	/** Removes a value from the ItemStack's custom NBT data. <br>
	 *  Gets rid of any empty parent compounds. Example: <br>
	 *  <code> StackUtils.remove(stack, "display", "color"); </code> */
	public static void remove(ItemStack stack, String... tags) {
		if (!stack.hasTagCompound()) return;
		String tag = null;
		NBTTagCompound compound = stack.getTagCompound();
		for (int i = 0; i < tags.length; i++) {
			tag = tags[i];
			if (!compound.hasKey(tag)) return;
			if (i == tags.length - 1) break;
			compound = compound.getCompoundTag(tag);
		}
		compound.removeTag(tag);
	}
	
}
