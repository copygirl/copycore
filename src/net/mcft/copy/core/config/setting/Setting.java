package net.mcft.copy.core.config.setting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

public abstract class Setting<T> {
	
	/** The name including the category, for example "block.storage.flint". */
	public final String fullName;
	/** The name not including the category, for example "flint". */
	public final String name;
	/** The category, for example "block.storage". */
	public final String category;
	
	/** The default value of this setting. */
	public final T defaultValue;
	/** The comment of this setting setting, null if none. */
	public String comment = null;
	
	public Setting(String fullName, T defaultValue) {
		this.fullName = fullName;
		int dotIndex = fullName.lastIndexOf('.');
		if (dotIndex < 1) throw new IllegalArgumentException("fullName doesn't contain a category.");
		name = fullName.substring(dotIndex + 1);
		category = fullName.substring(0, dotIndex);
		
		this.defaultValue = defaultValue;
	}
	
	/** Sets the comment for this setting. */
	public Setting<T> setComment(String comment) {
		this.comment = comment;
		return this;
	}
	
	/** Validates the setting and returns a warning
	 *  string, or null if validation was successful. */
	public String validate(T value) { return null; }
	
	/** Loads the setting's value from the config. */
	public abstract T load(Configuration config);
	/** Saves the setting's value to the config. */
	public abstract void save(Configuration config, T value);
	
	/** Reads the setting's synced value from the compound tag. */
	public abstract T read(NBTTagCompound compound);
	/** Writes the setting's value to the compound tag. */
	public abstract void write(NBTTagCompound compound, T value);
	
	// Equals, hashCode and toString
	
	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof Setting) ? fullName.equals(((Setting)obj).fullName) : false);
	}
	
	@Override
	public int hashCode() { return fullName.hashCode(); }
	
	@Override
	public String toString() { return "[" + fullName + "]"; }
	
}
