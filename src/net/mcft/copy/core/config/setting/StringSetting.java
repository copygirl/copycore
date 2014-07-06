package net.mcft.copy.core.config.setting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class StringSetting extends SinglePropertySetting<String> {
	
	protected String[] validValues = null;
	
	public StringSetting(String fullName, String defaultValue) {
		super(fullName, defaultValue);
	}
	public StringSetting(String fullName) {
		this(fullName, "");
	}
	
	@Override
	public StringSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	
	/** Sets the valid values for this setting. */
	public StringSetting setValidValues(String... values) {
		validValues = values;
		return this;
	}
	
	@Override
	protected Type getPropertyType() { return Type.STRING; }
	
	@Override
	public String load(Configuration config) { return getProperty(config).getString(); }
	@Override
	public void save(Configuration config, String value) { getProperty(config).set(value); }
	
	@Override
	public String read(NBTTagCompound compound) { return compound.getString(fullName); }
	@Override
	public void write(NBTTagCompound compound, String value) { compound.setString(fullName, value); }
	
}
