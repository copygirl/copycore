package net.mcft.copy.core.config.setting;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property.Type;

import org.apache.commons.lang3.ArrayUtils;

public class IntegerSetting extends SinglePropertySetting<Integer> {
	
	public int minValue = Integer.MIN_VALUE;
	public int maxValue = Integer.MAX_VALUE;
	public int[] validValues = null;
	
	public IntegerSetting(String fullName, Integer defaultValue) {
		super(fullName, defaultValue);
	}
	public IntegerSetting(String fullName) {
		this(fullName, 0);
	}
	
	@Override
	public IntegerSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	
	/** Sets the valid range of values for this setting. */
	public IntegerSetting setValidRange(int min, int max) {
		minValue = min;
		maxValue = max;
		return this;
	}
	/** Sets the valid values for this setting. */
	public IntegerSetting setValidValues(int... values) {
		validValues = values;
		return this;
	}
	
	@Override
	public String validate(Integer value) {
		if ((value < minValue) || (value > maxValue))
			return String.format("Value %s is not in valid range, %s to %s",
			                     value, minValue, maxValue);
		if ((validValues != null) && !ArrayUtils.contains(validValues, value))
			return String.format("Value %s is not valid, needs to be one of %s",
			                     value, Arrays.toString(validValues));
		return null;
	}
	
	@Override
	protected Type getPropertyType() { return Type.INTEGER; }
	
	@Override
	public Integer load(Configuration config) { return getProperty(config).getInt(); }
	@Override
	public void save(Configuration config, Integer value) { getProperty(config).set(value); }
	
	@Override
	public Integer read(NBTTagCompound compound) { return compound.getInteger(fullName); }
	@Override
	public void write(NBTTagCompound compound, Integer value) { compound.setInteger(fullName, value); }
	
}
