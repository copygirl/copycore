package net.mcft.copy.core.config;

import net.mcft.copy.core.config.setting.Setting;

public interface ISettingChangedHandler<T> {
	
	/** Called when the setting's value is changed. */
	void onChanged(Setting<T> setting, T value);
	
}
