package net.mcft.copy.core.entity;

import java.io.IOException;

import net.mcft.copy.core.util.NbtUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.network.PacketBuffer;

public class EntityPropertyPrimitive<T> extends EntityProperty<T> {
	
	public EntityPropertyPrimitive(String name, T value) {
		super(name, value);
	}
	
	@Override
	public NBTBase write() {
		T value = get();
		if (value instanceof Boolean)
			return new NBTTagByte((Boolean)value ? (byte)1 : (byte)0);
		else return NbtUtils.createTag(get());
	}
	@Override
	public void write(PacketBuffer buffer) {
		T value = get();
		if (value == null) throw new NullPointerException("value is null");
		else if (value instanceof Boolean) buffer.writeBoolean((Boolean)value);
		else if (value instanceof Byte) buffer.writeByte((Byte)value);
		else if (value instanceof Short) buffer.writeShort((Short)value);
		else if (value instanceof Integer) buffer.writeInt((Integer)value);
		else if (value instanceof Long) buffer.writeLong((Long)value);
		else if (value instanceof Float) buffer.writeFloat((Float)value);
		else if (value instanceof Double) buffer.writeDouble((Double)value);
		else if (value instanceof String) {
			try { buffer.writeStringToBuffer((String)value); }
			catch (IOException ex) { throw new RuntimeException(ex); }
		} else throw new UnsupportedOperationException(value.getClass().getName() + " is not supported.");
	}
	
	@Override
	public void read(NBTBase tag) {
		T value = get();
		if (value instanceof Boolean)
			set((T)(Boolean)(NbtUtils.<Byte>getTagValue(tag) > 0));
		else set(NbtUtils.<T>getTagValue(tag));
	}
	@Override
	public void read(PacketBuffer buffer) {
		T value = get();
		if (value == null) throw new NullPointerException("value is null");
		else if (value instanceof Boolean) set((T)(Object)buffer.readBoolean());
		else if (value instanceof Byte) set((T)(Object)buffer.readByte());
		else if (value instanceof Short) set((T)(Object)buffer.readShort());
		else if (value instanceof Integer) set((T)(Object)buffer.readInt());
		else if (value instanceof Long) set((T)(Object)buffer.readLong());
		else if (value instanceof Float) set((T)(Object)buffer.readFloat());
		else if (value instanceof Double) set((T)(Object)buffer.readDouble());
		else if (value instanceof String) {
			try { set((T)buffer.readStringFromBuffer(128)); }
			catch (IOException ex) { throw new RuntimeException(ex); }
		} else throw new UnsupportedOperationException(value.getClass().getName() + " is not supported.");
	}
	
}
