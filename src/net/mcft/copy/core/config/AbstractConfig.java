package net.mcft.copy.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mcft.copy.core.config.setting.Setting;

public abstract class AbstractConfig {
	
	private final List<ISettingChangedHandler> mainChangedHandlers =
			new ArrayList<ISettingChangedHandler>();
	private final Map<Setting, List<ISettingChangedHandler>> changedHandlers =
			new HashMap<Setting, List<ISettingChangedHandler>>();
	
	/** Adds a changed handler for the setting. Its onChanged method
	 *  is called when the config's value for that setting is changed. */
	public <T> void registerSettingChangedHandler(Setting<T> setting, ISettingChangedHandler<T> handler) {
		List<ISettingChangedHandler> handlers = changedHandlers.get(setting);
		if (handlers == null) {
			handlers = new ArrayList<ISettingChangedHandler>();
			changedHandlers.put(setting, handlers);
		}
		handlers.add(handler);
	}
	/** Adds a changed handler for all settings. Its onChanged method
	 *  is called when any of the config's values are changed. */
	public <T> void registerSettingChangedHandler(ISettingChangedHandler<T> handler) {
		mainChangedHandlers.add(handler);
	}
	
	protected <T> void onSettingChanged(Setting<T> setting, T value) {
		List<ISettingChangedHandler> handlers = changedHandlers.get(setting);
		for (ISettingChangedHandler handler : mainChangedHandlers)
			handler.onChanged(setting, value);
		if (handlers != null)
			for (ISettingChangedHandler handler : handlers)
				handler.onChanged(setting, value);
	}
	
	/** Returns the value of the setting in this config. */
	public abstract <T> T get(Setting<T> setting);
	
	/** Returns all settings handled by this config. */
	public abstract Collection<Setting> getSettings();
	
}
