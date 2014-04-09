package net.mcft.copy.core.api;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemIdentifier {
	
	private final Item item;
	private final int damage;
	private final NBTTagCompound data;
	
	private int hashCode;
	private boolean calculatedHashCode = false;
	
	public ItemIdentifier(Item item, int damage, NBTTagCompound data) {
		this.item = item;
		this.damage = damage;
		this.data = data;
	}
	public ItemIdentifier(ItemStack stack) {
		this(stack.getItem(), Items.diamond.getDamage(stack),
		     (stack.hasTagCompound() ? (NBTTagCompound)stack.getTagCompound().copy() : null));
	}
	
	public ItemStack createStack(int size) {
		ItemStack stack = new ItemStack(item, size, damage);
		if (data != null)
			stack.stackTagCompound = (NBTTagCompound)data.copy();
		return stack;
	}
	
	public boolean matches(Item item, int damage, NBTTagCompound data) {
		return ((item == this.item) && (damage == this.damage) &&
		        ((data == this.data) || ((data != null) && data.equals(this.data))));
	}
	
	public boolean matches(ItemStack stack) {
		return matches(stack.getItem(), Items.diamond.getDamage(stack), stack.getTagCompound());
	}
	
	@Override
	public int hashCode() {
		if (!calculatedHashCode) {
			hashCode = Item.getIdFromItem(item) ^ (damage << 8);
			if (data != null) hashCode ^= data.hashCode();
			calculatedHashCode = true;
		}
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ItemIdentifier)) return false;
		ItemIdentifier other = (ItemIdentifier)obj;
		return matches(other.item, other.damage, other.data);
	}
	
	@Override
	public String toString() {
		return item.getUnlocalizedName() + ":" + damage;
	}
	
}
