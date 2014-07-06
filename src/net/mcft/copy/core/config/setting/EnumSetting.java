package net.mcft.copy.core.config.setting;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class EnumSetting<T extends Enum<T>> extends SinglePropertySetting<T> {
	
	private final Map<String, T> enumMap = new HashMap<String, T>();
	private final String validValues;
	
	public EnumSetting(String fullName, T defaultValue) {
		super(fullName, defaultValue);
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
	public String validate(T value) {
		return ((value == null) ? "Must be one of " + validValues + "." : null);
	}
	
	@Override
	protected Type getPropertyType() { return Type.STRING; }
	
	@Override
	public T load(Configuration config) { return enumMap.get(getProperty(config).getString()); }
	@Override
	public void save(Configuration config, T value) { getProperty(config).set(value.toString()); }
	
	@Override
	public T read(NBTTagCompound compound) { return enumMap.get(compound.getString(fullName)); }
	@Override
	public void write(NBTTagCompound compound, T value) { compound.setString(fullName, value.toString()); }
	
}
