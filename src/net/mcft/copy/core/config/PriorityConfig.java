package net.mcft.copy.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.mcft.copy.core.config.setting.Setting;

public class PriorityConfig extends AbstractConfig implements ISettingChangedHandler {
	
	private List<AbstractConfig>[] configs = new List[3];
	private Map<Setting, Object> previousValues = new HashMap<Setting, Object>();
	
	public PriorityConfig() {
		for (int i = 0; i < configs.length; i++)
			configs[i] = new ArrayList<AbstractConfig>();
	}
	
	/** Add a new config to the end of the priority list. */
	public void add(Priority priority, AbstractConfig config) {
		configs[priority.ordinal()].add(config);
		config.registerSettingChangedHandler(this);
		for (Setting setting : config.getSettings())
			onChanged(setting, null);
	}
	/** Removes a config from the priority list. */
	public void remove(AbstractConfig config) {
		for (List<AbstractConfig> list : configs)
			list.remove(config);
		for (Setting setting : config.getSettings())
			onChanged(setting, null);
	}
	
	@Override
	public <T> T get(Setting<T> setting) {
		T value;
		for (int i = configs.length - 1; i >= 0; i--)
			for (AbstractConfig config : configs[i])
				if ((value = config.get(setting)) != null)
					return value;
		return null;
	}
	
	@Override
	public Collection<Setting> getSettings() { return previousValues.keySet(); }
	
	@Override
	public void onChanged(Setting setting, Object value) {
		Object currentValue = get(setting);
		if (Objects.equals(previousValues.get(setting), currentValue)) return;
		if (currentValue != null)
			previousValues.put(setting, currentValue);
		else previousValues.remove(setting);
		onSettingChanged(setting, currentValue);
	}
	
	public enum Priority {
		GLOBAL,
		WORLD,
		SYNCED
	}
	
}
