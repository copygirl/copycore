package net.mcft.copy.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.DoubleSetting;
import net.mcft.copy.core.config.setting.IntegerSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private final Configuration forgeConfig;
	
	private final Map<String, Setting> settings = new HashMap<String, Setting>();
	private final List<Setting> syncedSettings = new ArrayList<Setting>();
	
	public Config(File file) {
		forgeConfig = new Configuration(file);
	}
	
	/** Adds a setting to the config. */
	public void add(Setting setting) {
		settings.put(setting.fullName, setting);
	}
	/** Marks a setting to be synced from server to client. */
	public void syncSetting(Setting setting) {
		syncedSettings.add(setting);
	}
	
	/** Returns a setting with the specified full name. */
	public Setting get(String name) { return settings.get(name); }
	
	/** Returns the value of an integer setting with that name. */
	public int getInteger(String name) { return ((IntegerSetting)get(name)).getValue(); }
	/** Returns the value of a double setting with that name. */
	public double getDouble(String name) { return ((DoubleSetting)get(name)).getValue(); }
	/** Returns the value of a boolean setting with that name. */
	public boolean getBoolean(String name) { return ((BooleanSetting)get(name)).getValue(); }
	
	/** Loads settings from the config file. */
	public void load() {
		forgeConfig.load();
		for (Setting setting : settings.values())
			setting.load(forgeConfig);
	}
	/** Saves settings to the config file. */
	public void save() {
		for (Setting setting : settings.values())
			setting.save(forgeConfig);
		forgeConfig.save();
	}
	
	/** Validates all settings. Invalid settings print
	 *  a warning and use the default value instead. */
	public void validate() {
		for (Setting setting : settings.values())
			setting.validate();
	}
	
	/** Synchronizes all settings with those of the server. */
	public void read(NBTTagCompound compound) {
		for (Setting setting : syncedSettings)
			setting.read(compound);
	}
	/** Writes all synced settings to the
	 *  compound to be sent to the client. */
	public void write(NBTTagCompound compound) {
		for (Setting setting : syncedSettings)
			setting.write(compound);
	}
	
}
