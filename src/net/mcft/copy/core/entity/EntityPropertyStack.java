package net.mcft.copy.core.entity;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class EntityPropertyStack extends EntityProperty<ItemStack> {
	
	public EntityPropertyStack(String name, ItemStack value) {
		super(name, value);
	}
	public EntityPropertyStack(String name) {
		this(name, null);
	}
	
	@Override
	public NBTBase write() {
		NBTTagCompound compound = new NBTTagCompound();
		if (get() != null) get().writeToNBT(compound);
		return compound;
	}
	@Override
	public void write(PacketBuffer buffer) {
		NBTTagCompound compound = null;
		if (get() != null) get().writeToNBT(compound = new NBTTagCompound());
		try { buffer.writeNBTTagCompoundToBuffer(compound); }
		catch (IOException ex) { throw new RuntimeException(ex); }
	}
	
	@Override
	public void read(NBTBase tag) {
		NBTTagCompound compound = (NBTTagCompound)tag;
		if (!compound.hasNoTags())
			set(ItemStack.loadItemStackFromNBT(compound));
	}
	@Override
	public void read(PacketBuffer buffer) {
		NBTTagCompound compound;
		try { compound = buffer.readNBTTagCompoundFromBuffer(); }
		catch (IOException ex) { throw new RuntimeException(ex); }
		set((compound != null) ? ItemStack.loadItemStackFromNBT(compound) : null);
	}
	
}
