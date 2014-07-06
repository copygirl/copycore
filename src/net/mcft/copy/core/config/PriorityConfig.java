package net.mcft.copy.core.config;

import java.util.ArrayList;
import java.util.List;

import net.mcft.copy.core.config.setting.Setting;

public class PriorityConfig implements IConfig {
	
	private List<IConfig>[] configs = new List[3];
	
	public PriorityConfig() {
		for (int i = 0; i < configs.length; i++)
			configs[i] = new ArrayList<IConfig>();
	}
	
	/** Add a new config to the end of the priority list. */
	public void add(Priority priority, IConfig config) {
		configs[priority.ordinal()].add(config);
	}
	/** Removes a config from the priority list. */
	public void remove(IConfig config) {
		for (List<IConfig> list : configs)
			list.remove(config);
	}
	
	@Override
	public <T> T get(Setting<T> setting) {
		T value;
		for (int i = configs.length - 1; i >= 0; i--)
			for (IConfig config : configs[i])
				if ((value = config.get(setting)) != null)
					return value;
		return null;
	}
	
	public enum Priority {
		GLOBAL,
		WORLD,
		SYNCED
	}
	
}
