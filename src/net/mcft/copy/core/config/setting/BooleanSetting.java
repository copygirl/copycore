package net.mcft.copy.core.config.setting;

import net.mcft.copy.core.config.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class BooleanSetting extends SinglePropertySetting<Boolean> {
	
	public BooleanSetting(Config config, String fullName, Boolean defaultValue) {
		super(config, fullName, defaultValue);
	}
	public BooleanSetting(Config config, String fullName) {
		this(config, fullName, false);
	}
	
	@Override
	public BooleanSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public BooleanSetting setSynced() {
		super.setSynced();
		return this;
	}
	
	@Override
	protected Type getPropertyType() { return Type.BOOLEAN; }
	
	@Override
	protected Boolean loadInternal(Configuration config) { return getProperty(config).getBoolean(defaultValue); }
	@Override
	protected void saveInternal(Configuration config, Boolean value) { getProperty(config).set(value); }
	
	@Override
	protected Boolean readInternal(NBTTagCompound compound) { return compound.getBoolean(fullName); }
	@Override
	protected void writeInternal(NBTTagCompound compound, Boolean value) { compound.setBoolean(fullName, value); }
	
}
