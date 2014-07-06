package net.mcft.copy.core.config.setting;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

public class DoubleSetting extends SinglePropertySetting<Double> {
	
	protected double minValue = Double.MIN_VALUE;
	protected double maxValue = Double.MAX_VALUE;
	
	public DoubleSetting(String fullName, Double defaultValue) {
		super(fullName, defaultValue);
	}
	public DoubleSetting(String fullName) {
		this(fullName, 0.0);
	}
	
	@Override
	public DoubleSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	
	/** Sets the valid range of values for this setting. */
	public DoubleSetting setValidRange(double min, double max) {
		minValue = min;
		maxValue = max;
		return this;
	}
	
	@Override
	public String validate(Double value) {
		if ((value < minValue) || (value > maxValue))
			return String.format("Value %s is not in valid range, %s to %s",
			                     value, minValue, maxValue);
		return null;
	}
	
	@Override
	protected Type getPropertyType() { return Type.DOUBLE; }
	
	@Override
	public Double load(Configuration config) { return getProperty(config).getDouble(defaultValue); }
	@Override
	public void save(Configuration config, Double value) { getProperty(config).set(value); }
	
	@Override
	public Double read(NBTTagCompound compound) { return compound.getDouble(fullName); }
	@Override
	public void write(NBTTagCompound compound, Double value) { compound.setDouble(fullName, value); }
	
}
