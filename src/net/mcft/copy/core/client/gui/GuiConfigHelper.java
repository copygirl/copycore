package net.mcft.copy.core.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.ConfigSetting;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.DoubleSetting;
import net.mcft.copy.core.config.setting.EnumSetting;
import net.mcft.copy.core.config.setting.IntegerSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.config.setting.StringSetting;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class GuiConfigHelper {
	
	private GuiConfigHelper() {  }
	
	public static List<IConfigElement> getElementsFor(Class<? extends Config> configClass, String modId) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		Map<String, List<IConfigElement>> categoryMap = new HashMap<String, List<IConfigElement>>();
		
		for (Field field : configClass.getFields())
			if (Modifier.isStatic(field.getModifiers()) &&
			    (field.getType().isAssignableFrom(Setting.class))) {
				
				Setting setting;
				try { setting = (Setting)field.get(null); }
				catch (Exception ex) { throw new RuntimeException(ex); }
				
				IConfigElement configElement;
				ConfigSetting annotation = field.getAnnotation(ConfigSetting.class);
				String configElementClassName = ((annotation != null) ? annotation.getConfigElementClass() : "");
				if (configElementClassName.isEmpty()) {
					if ((configElement = getDefaultConfigElementFor(setting, modId)) == null)
						continue;
				} else try {
					Class<? extends IConfigElement> configElementClass =
							(Class<? extends IConfigElement>)Class.forName(configElementClassName);
					configElement = configElementClass.getConstructor(Setting.class, String.class).newInstance(setting, modId);
				} catch (Exception ex) { throw new RuntimeException(ex); }
				
				if (!setting.category.equals("general")) {
					List<IConfigElement> categoryList;
					if ((categoryList = categoryMap.get(setting.category)) == null)
						categoryMap.put(setting.category, (categoryList = new ArrayList<IConfigElement>()));
					categoryList.add(configElement);
				} else list.add(configElement);
				
			}
		
		for (Map.Entry<String, List<IConfigElement>> entry : categoryMap.entrySet())
			list.add(new DummyCategoryElement(entry.getKey(),
					modId.toLowerCase() + ".config.category." + entry.getKey(), entry.getValue()));
		
		return list;
	}
	
	public static IConfigElement getDefaultConfigElementFor(Setting setting, String modId) {
		String name = setting.fullName;
		Object defaultValue = setting.defaultValue;
		ConfigGuiType type;
		String langKey = modId.toLowerCase() + ".config." + name;
		String[] validValues = null;
		Pattern pattern = null;
		Object minValue = null;
		Object maxValue = null;
		
		if (setting instanceof BooleanSetting)
			type = ConfigGuiType.BOOLEAN;
		
		else if (setting instanceof IntegerSetting) {
			
			IntegerSetting set = (IntegerSetting)setting;
			type = ConfigGuiType.INTEGER;
			minValue = set.minValue;
			maxValue = set.maxValue;
			if (set.validValues != null) {
				validValues = new String[set.validValues.length];
				for (int i = 0; i < validValues.length; i++)
					validValues[i] = String.valueOf(set.validValues[i]);
			}
			
		} else if (setting instanceof DoubleSetting) {
			
			DoubleSetting set = (DoubleSetting)setting;
			type = ConfigGuiType.DOUBLE;
			minValue = set.minValue;
			maxValue = set.maxValue;
			
		} else if (setting instanceof StringSetting) {
			
			StringSetting set = (StringSetting)setting;
			type = ConfigGuiType.STRING;
			validValues = set.validValues;
			
		} else if (setting instanceof EnumSetting) {
			
			EnumSetting set = (EnumSetting)setting;
			type = ConfigGuiType.STRING;
			Object[] values = set.defaultValue.getClass().getEnumConstants();
			validValues = new String[values.length];
			for (int i = 0; i < validValues.length; i++)
				validValues[i] = values[i].toString();
			
		} else return null;
		return new DummyConfigElement(name, defaultValue, type, langKey, validValues, pattern, minValue, maxValue);
	}
	
}
