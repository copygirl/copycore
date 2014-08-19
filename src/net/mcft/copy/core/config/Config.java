package net.mcft.copy.core.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.config.setting.Setting;
import net.minecraftforge.common.config.Configuration;

public class Config extends AbstractConfig {
	
	private final File file;
	private final Configuration forgeConfig;
	
	private final Map<Setting, Object> settingValues = new HashMap<Setting, Object>();
	private final Map<String, String> categoryComments = new HashMap<String, String>();
	
	private final List<SettingInfo> settings = new ArrayList<SettingInfo>();
	
	public Config(File file) {
		this.file = file;
		forgeConfig = new ModifiedConfiguration(file);
	}
	
	/** Adds a setting to the config. */
	public <T> Setting<T> add(SettingInfo<T> settingInfo) {
		Setting<T> setting = settingInfo.setting;
		settingValues.put(setting, setting.defaultValue);
		settings.add(settingInfo);
		onSettingChanged(setting, setting.defaultValue);
		return setting;
	}
	
	/** Add all static settings of the class automatically via reflection. */
	public void addAllViaReflection() {
		for (Field field : getClass().getFields())
			if (Modifier.isStatic(field.getModifiers()) &&
			    (field.getType().isAssignableFrom(Setting.class)))
				add(new SettingInfo(field));
	}
	
	@Override
	public <T> T get(Setting<T> setting) { return (T)settingValues.get(setting); }
	
	/** Sets the value of the setting in this config. */
	public <T> void set(Setting<T> setting, T value) {
		if (Objects.equals(get(setting), value)) return;
		settingValues.put(setting, value);
		onSettingChanged(setting, value);
	}
	
	@Override
	public Collection<Setting> getSettings() { return settingValues.keySet(); }
	
	/** Returns a collection of all setting infos in this config. */
	public Collection<SettingInfo> getSettingInfos() { return settings; }
	
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
		for (Map.Entry<Setting, Object> entry : settingValues.entrySet()) {
			Setting setting = entry.getKey();
			Object value = setting.load(forgeConfig);
			String validationError = setting.validate(value);
			if (validationError != null) {
				value = setting.defaultValue;
				copycore.log.warn(String.format(
						"Config setting %s in %s is invalid: %s. Using default value: %s.",
						setting, file.getName(), validationError, value));
			}
			set(setting, value);
		}
	}
	
	/** Saves settings to the config file. */
	public void save() {
		for (Map.Entry<Setting, Object> entry : settingValues.entrySet())
			entry.getKey().save(forgeConfig, entry.getValue());
		for (Map.Entry<String, String> entry : categoryComments.entrySet())
			forgeConfig.setCategoryComment(entry.getKey(), entry.getValue());
		forgeConfig.save();
	}
	
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
