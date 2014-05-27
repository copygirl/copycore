package net.mcft.copy.core.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public final class NbtUtils {
	
	public static final String TAG_INDEX = "index";
	public static final String TAG_STACK = "stack";
	
	private NbtUtils() {  }
	
	/** Returns the primitive value of a tag, casted to the return type. */
	public static <T> T getTagValue(NBTBase tag) {
		if (tag == null)
			throw new IllegalArgumentException("tag is null");
		if (tag instanceof NBTTagByte)      return (T)(Object)((NBTTagByte)tag).func_150290_f();
		if (tag instanceof NBTTagShort)     return (T)(Object)((NBTTagShort)tag).func_150289_e();
		if (tag instanceof NBTTagInt)       return (T)(Object)((NBTTagInt)tag).func_150287_d();
		if (tag instanceof NBTTagLong)      return (T)(Object)((NBTTagLong)tag).func_150291_c();
		if (tag instanceof NBTTagFloat)     return (T)(Object)((NBTTagFloat)tag).func_150288_h();
		if (tag instanceof NBTTagDouble)    return (T)(Object)((NBTTagDouble)tag).func_150286_g();
		if (tag instanceof NBTTagString)    return (T)((NBTTagString)tag).func_150285_a_();
		if (tag instanceof NBTTagByteArray) return (T)((NBTTagByteArray)tag).func_150292_c();
		if (tag instanceof NBTTagIntArray)  return (T)((NBTTagIntArray)tag).func_150302_c();
		throw new IllegalArgumentException(NBTBase.NBTTypes[tag.getId()] + " isn't a primitive NBT tag");
	}
	
	/** Creates and returns a primitive NBT tag from a value.
	 *  If the value already is an NBT tag, it is returned instead. */
	public static NBTBase createTag(Object value) {
		if (value == null)
			throw new IllegalArgumentException("value is null");
		if (value instanceof NBTBase) return (NBTBase)value;
		if (value instanceof Byte)    return new NBTTagByte((Byte)value);
		if (value instanceof Short)   return new NBTTagShort((Short)value);
		if (value instanceof Integer) return new NBTTagInt((Integer)value);
		if (value instanceof Long)    return new NBTTagLong((Long)value);
		if (value instanceof Float)   return new NBTTagFloat((Float)value);
		if (value instanceof Double)  return new NBTTagDouble((Double)value);
		if (value instanceof String)  return new NBTTagString((String)value);
		if (value instanceof byte[])  return new NBTTagByteArray((byte[])value);
		if (value instanceof int[])   return new NBTTagIntArray((int[])value);
		throw new IllegalArgumentException("Can't create an NBT tag of value: " + value);
	}
	
	
	/** Creates an NBT list with the values, all of the single type. */
	public static NBTTagList createList(Object... values) {
		NBTTagList list = new NBTTagList();
		for (Object value : values)
			list.appendTag(createTag(value));
		return list;
	}
	
	/** Creates an NBT compound from the name-value pairs in the parameters. Example: <br>
	 *  <code> NbtUtils.createCompound("id", 1, "name", "copygirl") </code> */
	public static NBTTagCompound createCompound(Object... nameValuePairs) {
		NBTTagCompound compound = new NBTTagCompound();
		for (int i = 0; i < nameValuePairs.length; i += 2)
			compound.setTag((String)nameValuePairs[i], createTag(nameValuePairs[i + 1]));
		return compound;
	}
	
	
	/** Writes an item stack to an NBT compound. */
	public static NBTTagCompound writeItem(ItemStack item) {
		return ((item != null) ? item.writeToNBT(new NBTTagCompound())
		                       : new NBTTagCompound());
	}
	
	/** Reads an item stack from an NBT compound. */
	public static ItemStack readItem(NBTTagCompound compound) {
		return (((compound != null) && !compound.hasNoTags())
				? ItemStack.loadItemStackFromNBT(compound) : null);
	}
	
	
	/** Writes an item stack array to an NBT list. */
	public static NBTTagList writeItems(ItemStack[] items) {
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < items.length; i++)
			if (items[i] != null)
				list.appendTag(createCompound(
						TAG_INDEX, (short)i,
						TAG_STACK, writeItem(items[i])));
		return list;
	}
	
	/** Reads items from an NBT list to an item stack array.
	 *  Any items falling outside the range of the items array
	 *  will get added to the invalid list if that's non-null. */
	public static void readItems(NBTTagList list, ItemStack[] items, List<ItemStack> invalid) {
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			int index = compound.getShort(TAG_INDEX);
			ItemStack stack = readItem(compound.getCompoundTag(TAG_STACK));
			if ((index >= 0) || (index < items.length))
				items[index] = stack;
			else if (invalid != null)
				invalid.add(stack);
		}
	}
	/** Reads items from an NBT list to an item stack array. */
	public static void readItems(NBTTagList list, ItemStack[] items) {
		readItems(list, items, null);
	}
	
}
