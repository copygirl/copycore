package net.mcft.copy.core.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mcft.copy.core.config.setting.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class SyncedConfig extends AbstractConfig {
	
	private static final Map<String, SyncedConfig> allConfigs = new HashMap<String, SyncedConfig>();
	
	/** Returns a collection of all copycore managed configs. */
	public static Collection<SyncedConfig> getAllConfigs() { return allConfigs.values(); }
	
	/** Returns a config by its ID, null if there isn't one. */
	public static SyncedConfig getConfigById(String id) { return allConfigs.get(id); }
	
	/** Clears all synced config values. Called
	 *  when the client disconnects from a server. */
	public static void resetAllConfigs() {
		for (SyncedConfig config : getAllConfigs()) config.reset();
	}
	
	
	public final String id;
	
	private final Map<Setting, ConfigValuePair> settingValues = new HashMap<Setting, ConfigValuePair>();
	
	public SyncedConfig(String id) {
		this.id = id;
		allConfigs.put(id, this);
	}
	
	@Override
	public <T> T get(Setting<T> setting) {
		ConfigValuePair pair = settingValues.get(setting);
		return ((pair != null) ? (T)pair.value : null);
	}
	
	@Override
	public Collection<Setting> getSettings() { return settingValues.keySet(); }
	
	/** Adds a setting to be synced to the config. */
	public void add(AbstractConfig config, Setting setting) {
		settingValues.put(setting, new ConfigValuePair(config));
	}
	
	/** Add all static settings with the Synced annotation
	 *  from the config's class automatically via reflection. */
	public void addFromReflection(AbstractConfig getValueFrom, Config getSettingsFrom) {
		for (Field field : getSettingsFrom.getClass().getFields())
			if (Modifier.isStatic(field.getModifiers()) &&
			    (field.getType().isAssignableFrom(Setting.class)) &&
			    (field.isAnnotationPresent(SyncedSetting.class))) {
				try { add(getValueFrom, (Setting)field.get(null)); }
				catch (Exception ex) { throw new RuntimeException(ex); }
			}
	}
	/** Add all static settings with the Synced annotation
	 *  from the config's class automatically via reflection. */
	public void addFromReflection(Config config) {
		addFromReflection(config, config);
	}
	
	
	/** Reads all settings to be synced from the compound. */
	public void read(NBTTagCompound compound) {
		for (Map.Entry<Setting, ConfigValuePair> entry : settingValues.entrySet()) {
			Object value = entry.getKey().read(compound);
			entry.getValue().value = value;
			onSettingChanged(entry.getKey(), value);
		}
	}
	
	/** Writes all settings to be synced to the compound. */
	public NBTTagCompound write(NBTTagCompound compound) {
		for (Map.Entry<Setting, ConfigValuePair> entry : settingValues.entrySet()) {
			entry.getValue().value = null;
			entry.getKey().write(compound, entry.getValue().config.get(entry.getKey()));
		}
		return compound;
	}
	
	public void reset() {
		for (Map.Entry<Setting, ConfigValuePair> entry : settingValues.entrySet()) {
			entry.getValue().value = null;
			onSettingChanged(entry.getKey(), null);
		}
	}
	
	
	private static class ConfigValuePair {
		public final AbstractConfig config;
		public Object value = null;
		public ConfigValuePair(AbstractConfig config) {
			this.config = config;
		}
	}
	
}
