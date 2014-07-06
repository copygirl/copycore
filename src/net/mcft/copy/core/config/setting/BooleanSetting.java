package net.mcft.copy.core.config.setting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class BooleanSetting extends SinglePropertySetting<Boolean> {
	
	public BooleanSetting(String fullName, Boolean defaultValue) {
		super(fullName, defaultValue);
	}
	public BooleanSetting(String fullName) {
		this(fullName, false);
	}
	
	@Override
	public BooleanSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	
	@Override
	protected Type getPropertyType() { return Type.BOOLEAN; }
	
	@Override
	public Boolean load(Configuration config) { return getProperty(config).getBoolean(defaultValue); }
	@Override
	public void save(Configuration config, Boolean value) { getProperty(config).set(value); }
	
	@Override
	public Boolean read(NBTTagCompound compound) { return compound.getBoolean(fullName); }
	@Override
	public void write(NBTTagCompound compound, Boolean value) { compound.setBoolean(fullName, value); }
	
}
