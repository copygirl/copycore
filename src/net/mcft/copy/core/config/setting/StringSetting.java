package net.mcft.copy.core.config.setting;

import net.mcft.copy.core.config.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class StringSetting extends SinglePropertySetting<String> {
	
	protected String[] validValues = null;
	
	public StringSetting(Config config, String fullName, String defaultValue) {
		super(config, fullName, defaultValue);
	}
	public StringSetting(Config config, String fullName) {
		this(config, fullName, "");
	}
	
	@Override
	public StringSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public StringSetting setSynced() {
		super.setSynced();
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
	protected String loadInternal(Configuration config) { return getProperty(config).getString(); }
	@Override
	protected void saveInternal(Configuration config, String value) { getProperty(config).set(value); }
	
	@Override
	protected String readInternal(NBTTagCompound compound) { return compound.getString(fullName); }
	@Override
	protected void writeInternal(NBTTagCompound compound, String value) { compound.setString(fullName, value); }
	
}
