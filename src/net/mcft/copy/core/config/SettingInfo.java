package net.mcft.copy.core.config;

import java.lang.reflect.Field;

import net.mcft.copy.core.config.setting.Setting;

public class SettingInfo<T> {
	
	public final Setting<T> setting;
	
	public final boolean requiresWorldRestart;
	public final boolean requiresMinecraftRestart;
	
	public final boolean showInConfigGui;
	public final String configElementClass;
	public final String configEntryClass;
	
	public SettingInfo(Setting<T> setting, boolean requiresWorldRestart, boolean requiresMinecraftRestart,
	                   boolean showInConfigGui, String configElementClass, String configEntryClass) {
		this.setting = setting;
		this.requiresWorldRestart = requiresWorldRestart;
		this.requiresMinecraftRestart = requiresMinecraftRestart;
		this.showInConfigGui = showInConfigGui;
		this.configElementClass = configElementClass;
		this.configEntryClass = configEntryClass;
	}
	public SettingInfo(Setting<T> setting, boolean requiresWorldRestart, boolean requiresMinecraftRestart) {
		this(setting, requiresWorldRestart, requiresMinecraftRestart, true, "", "");
	}
	
	public SettingInfo(Field field) {
		try {
			setting = (Setting<T>)field.get(null);
			
			ConfigSetting annotation = field.getAnnotation(ConfigSetting.class);
			
			requiresMinecraftRestart = ((annotation != null) && annotation.requiresMinecraftRestart());
			requiresWorldRestart     = (requiresMinecraftRestart || ((annotation != null) && annotation.requiresWorldRestart()));
			
			showInConfigGui = true;
			configElementClass = ((annotation != null) ? annotation.getConfigElementClass() : "");
			configEntryClass   = ((annotation != null) ? annotation.getConfigEntryClass() : "");
		} catch (Exception e) { throw new RuntimeException(e); }
	}
	
}
