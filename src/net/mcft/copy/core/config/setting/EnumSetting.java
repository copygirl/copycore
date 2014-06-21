package net.mcft.copy.core.config.setting;

import java.util.HashMap;
import java.util.Map;

import net.mcft.copy.core.config.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class EnumSetting<T extends Enum<T>> extends SinglePropertySetting<T> {
	
	private final Map<String, T> enumMap = new HashMap<String, T>();
	private final String validValues;
	
	public EnumSetting(Config config, String fullName, T defaultValue) {
		super(config, fullName, defaultValue);
		StringBuilder sb = new StringBuilder();
		for (T value : ((Class<T>)defaultValue.getClass()).getEnumConstants()) {
			enumMap.put(value.toString(), value);
			sb.append(", ").append(value);
		}
		validValues = sb.substring(2);
	}
	
	@Override
	public EnumSetting<T> setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public EnumSetting<T> setSynced() {
		super.setSynced();
		return this;
	}
	
	@Override
	protected String validateInternal(T value) {
		return ((value == null) ? "Must be one of " + validValues + "." : null);
	}
	
	@Override
	protected Type getPropertyType() { return Type.STRING; }
	
	@Override
	protected T loadInternal(Configuration config) { return enumMap.get(getProperty(config).getString()); }
	@Override
	protected void saveInternal(Configuration config, T value) { getProperty(config).set(value.toString()); }
	
	@Override
	protected T readInternal(NBTTagCompound compound) { return enumMap.get(compound.getString(fullName)); }
	@Override
	protected void writeInternal(NBTTagCompound compound, T value) { compound.setString(fullName, value.toString()); }
	
}
