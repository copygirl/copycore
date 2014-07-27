package net.mcft.copy.core.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.config.setting.Setting;
import net.minecraftforge.common.config.Configuration;

public class Config implements IConfig {
	
	private final File file;
	private final Configuration forgeConfig;
	
	private final Map<Setting, Object> settingValues = new HashMap<Setting, Object>();
	private final Map<String, String> categoryComments = new HashMap<String, String>();
	
	public Config(File file) {
		this.file = file;
		forgeConfig = new ModifiedConfiguration(file);
	}
	
	@Override
	public <T> T get(Setting<T> setting) { return (T)settingValues.get(setting); }
	
	/** Sets the value of the setting in this config. */
	public <T> void set(Setting<T> setting, T value) { settingValues.put(setting, value); }
	
	/** Adds a setting to the config. */
	public <T extends Setting> T add(T setting) {
		settingValues.put(setting, setting.defaultValue);
		return setting;
	}
	
	/** Add all static settings of the class automatically via reflection. */
	public void addAllViaReflection() {
		for (Field field : getClass().getFields())
			if (Modifier.isStatic(field.getModifiers()) &&
			    (field.getType().isAssignableFrom(Setting.class))) {
				try { add((Setting)field.get(null)); }
				catch (Exception ex) { throw new RuntimeException(ex); }
			}
	}
	
	/** Returns a collection of all settings in this config. */
	public Collection<Setting> getSettings() {
		return settingValues.keySet();
	}
	
	/** Adds a custom comment to a category. */
	public void addCategoryComment(String category, String comment) {
		categoryComments.put(category, comment);
	}
	
	/** Returns if the config file exists. */
	public boolean exists() {
		return file.exists();
	}
	
	/** Loads settings from the config file. */
	public void load() {
		forgeConfig.load();
		for (Map.Entry<Setting, Object> entry : settingValues.entrySet())
			entry.setValue(entry.getKey().load(forgeConfig));
		validate();
	}
	
	/** Saves settings to the config file. */
	public void save() {
		for (Map.Entry<Setting, Object> entry : settingValues.entrySet())
			entry.getKey().save(forgeConfig, entry.getValue());
		for (Map.Entry<String, String> entry : categoryComments.entrySet())
			forgeConfig.setCategoryComment(entry.getKey(), entry.getValue());
		forgeConfig.save();
	}
	
	/** Validates all settings. Invalid settings print
	 *  a warning and use the default value instead. */
	public void validate() {
		for (Map.Entry<Setting, Object> entry : settingValues.entrySet()) {
			Setting setting = entry.getKey();
			String message = setting.validate(entry.getValue());
			if (message != null) {
				entry.setValue(setting.defaultValue);
				copycore.log.warn(String.format(
						"Config setting {} in {} is invalid: {}. Using default value: {}.",
						setting, file.getName(), message, setting.defaultValue));
			}
		}
	}
	
	/** Called when something in the config was changed.
	 *  Updates affected objects and saves changes to disk. */
	public void onConfigChanged() {
		update();
		save();
	}
	
	/** Updates affected objects when config was loaded or changed. */
	public void update() {  }
	
	
	private static class ModifiedConfiguration extends Configuration {
		private static boolean preventLoad = false;
		private final File file;
		public ModifiedConfiguration(File file) {
			// Hacky way of preventing the config file from
			// being loaded when the configuration is constructed.
			super((preventLoad = true) ? file : null);
			preventLoad = false;
			this.file = file;
		}
		@Override public void load() {
			// Don't load/create the config file when it doesn't exist.
			if (!preventLoad && file.exists()) super.load();
		}
	}
	
}
