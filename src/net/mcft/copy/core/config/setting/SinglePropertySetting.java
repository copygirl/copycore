package net.mcft.copy.core.config.setting;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public abstract class SinglePropertySetting<T> extends Setting<T> {
	
	public SinglePropertySetting(String fullName, T defaultValue) {
		super(fullName, defaultValue);
	}
	
	protected Property getProperty(Configuration config) {
		return config.get(category, name, String.valueOf(defaultValue), comment, getPropertyType());
	}
	
	protected abstract Type getPropertyType();
	
}
