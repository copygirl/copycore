package net.mcft.copy.core.entity;

import java.util.Objects;

import net.minecraft.nbt.NBTBase;
import net.minecraft.network.PacketBuffer;

public abstract class EntityProperty<T> {
	
	public final String name;
	public int id;
	
	private final T initialValue;
	private T value;
	private T previousValue;
	
	private boolean saved = false;
	private boolean synced = false;
	private boolean syncToAll = false;
	
	public EntityProperty(String name, T value) {
		this.name = name;
		initialValue = value;
		set(value);
		previousValue = value;
	}
	
	
	/** Returns the property's value. */
	public T get() { return value; }
	/** Sets the property's value. */
	public T set(T value) { this.value = value; return value; }
	
	/** Returns if the value has changed since the last call to hasChanged. */
	public boolean hasChanged() {
		boolean changed = !Objects.equals(get(), previousValue);
		previousValue = get();
		return changed;
	}
	
	
	/** Marks this property to be saved to the entity / disk. */
	public EntityProperty<T> setSaved() { saved = true; return this; }
	/** Marks this property to be synchronized with clients. */
	public EntityProperty<T> setSynced(boolean syncToAll) {
		synced = true; this.syncToAll = syncToAll; return this; }
	
	/** Returns whether this property is saved to the entity / disk. */
	public boolean isSaved() { return (saved && !Objects.equals(initialValue, get())); }
	/** Returns whether this property is synchronized with clients. */
	public boolean isSynced() { return synced; }
	/** Returns whether this property is synchronized to
	 *  all players or just the player entity itself. */
	public boolean isSyncedToAll() { return syncToAll; }
	
	
	/** Writes the property to an NBT tag. */
	public abstract NBTBase write();
	/** Writes the property to a packet buffer. */
	public abstract void write(PacketBuffer buffer);

	/** Reads the property from an NBT tag. */
	public abstract void read(NBTBase tag);
	/** Reads the property from a packet buffer. */
	public abstract void read(PacketBuffer buffer);
	
}
