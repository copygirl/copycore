package net.mcft.copy.core.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.util.RegistryUtils.IRegistrable;

public class ContentConfig extends Config {
	
	private final Map<IRegistrable, Setting<Boolean>> registrableMap =
			new IdentityHashMap<IRegistrable, Setting<Boolean>>();
	
	public ContentConfig(File file) {
		super(file);
	}
	
	/** Adds a single IRegistrable to the content config. */
	public Setting<Boolean> add(String group, IRegistrable object) {
		Setting<Boolean> setting = new BooleanSetting(group + "." + object.getName(), true);
		registrableMap.put(object, setting);
		return add(new SettingInfo<Boolean>(setting, true, true));
	}
	
	/** Add all static IRegistrable fields of the class automatically via reflection. */
	public void addContentSettingsViaReflection(String group, Class contentClass) {
		for (Field field : contentClass.getFields())
			if (Modifier.isStatic(field.getModifiers())) {
				Object obj;
				try { obj = field.get(null); }
				catch (Exception ex) { throw new RuntimeException(ex); }
				if (IRegistrable.class.isInstance(obj))
					add(group, (IRegistrable)obj);
			}
	}
	
	/** Returns if the IRegistrable object is enabled in the config. */
	public boolean isEnabled(IRegistrable object) {
		Setting<Boolean> setting = registrableMap.get(object);
		return ((setting != null) ? get(setting) : false);
	}
	
}
