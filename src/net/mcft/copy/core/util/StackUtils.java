package net.mcft.copy.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public final class StackUtils {
	
	private StackUtils() {  }
	
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
