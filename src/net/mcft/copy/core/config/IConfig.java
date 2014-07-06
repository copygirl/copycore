package net.mcft.copy.core.config;

import net.mcft.copy.core.config.setting.Setting;

public interface IConfig {
	
	/** Returns the value of the setting in this config. */
	public <T> T get(Setting<T> setting);
	
}
