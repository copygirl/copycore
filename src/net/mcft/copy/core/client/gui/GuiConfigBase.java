package net.mcft.copy.core.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.ConfigSetting;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.DoubleSetting;
import net.mcft.copy.core.config.setting.EnumSetting;
import net.mcft.copy.core.config.setting.IntegerSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.config.setting.StringSetting;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiConfigBase extends GuiConfig {
	
	public final Config config;
	
	public GuiConfigBase(GuiScreen parentScreen, String id, String configId, String title, Config config) {
		super(parentScreen, getElementsFor(config, id), configId, false, false, title);
		this.config = config;
	}
	public GuiConfigBase(GuiScreen parentScreen, String id, String title, Config config) {
		this(parentScreen, id, id, title, config);
	}
	public GuiConfigBase(GuiScreen parentScreen, String id, Config config) {
		this(parentScreen, id, id, config);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		FMLCommonHandler.instance().bus().unregister(this);
	}
	
	@SubscribeEvent
	public final void onConfigChangedEvent(OnConfigChangedEvent event) {
		if ((event.modID == modID) &&
		    (event.configID == configID))
			config.onConfigChanged();
	}
	
	/** Gets the config entry for a setting in this GUI. */
	public IConfigEntry getEntry(Setting setting) {
		for (IConfigEntry entry : entryList.listEntries)
			if (setting.fullName.equals(entry.getName()))
				return entry;
		return null;
	}
	
	/** Gets the config entry for a category in this GUI. */
	public CategoryElement.Entry getCategoryEntry(String category) {
		for (IConfigEntry entry : entryList.listEntries)
			if ((entry instanceof CategoryEntry) &&
			    category.equals(entry.getName()))
				return (CategoryElement.Entry)entry;
		return null;
	}
	
	/** Gets all config elements to display on the config screen. */
	public static List<IConfigElement> getElementsFor(Config config, String modId) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		Map<String, List<IConfigElement>> categoryMap = new HashMap<String, List<IConfigElement>>();
		
		// Get all static setting fields from the config class.
		for (Field field : config.getClass().getFields())
			if (Modifier.isStatic(field.getModifiers()) &&
			    (field.getType().isAssignableFrom(Setting.class))) {
				
				Setting setting;
				try { setting = (Setting)field.get(null); }
				catch (Exception ex) { throw new RuntimeException(ex); }
				
				ConfigSetting annotation = field.getAnnotation(ConfigSetting.class);
				String configElementClassName = ((annotation != null) ? annotation.getConfigElementClass() : "");
				boolean reqMinecraftRestart = ((annotation != null) && annotation.requiresMinecraftRestart());
				boolean reqWorldRestart = (reqMinecraftRestart || ((annotation != null) && annotation.requiresWorldRestart()));
				
				IConfigElement configElement;
				// If the setting doesn't have a ConfigSetting annotation or its
				// custom config element class was not set, use the default one.
				if (configElementClassName.isEmpty()) {
					String configEntryClassName = ((annotation != null) ? annotation.getConfigEntryClass() : "");
					Class<? extends IConfigEntry> entryClass = null;
					try {
						if (!configEntryClassName.isEmpty())
							entryClass = (Class<? extends IConfigEntry>)Class.forName(configEntryClassName);
					} catch (Exception ex) { throw new RuntimeException(ex); }
					configElement = new SettingConfigElement(config, setting, modId, reqMinecraftRestart, reqWorldRestart).setConfigEntryClass(entryClass);
				// Otherwise try to instantiate the custom config element.
				} else try {
					configElement = (IConfigElement)Class.forName(configElementClassName).getConstructor(
							Config.class, Setting.class, String.class, boolean.class, boolean.class)
							.newInstance(config, setting, modId, reqMinecraftRestart, reqWorldRestart);
				} catch (Exception ex) { throw new RuntimeException(ex); }
				
				// If the setting's category is not "general", add it to the category map.
				if (!setting.category.equals("general")) {
					List<IConfigElement> categoryList;
					if ((categoryList = categoryMap.get(setting.category)) == null)
						categoryMap.put(setting.category, (categoryList = new ArrayList<IConfigElement>()));
					categoryList.add(configElement);
				// Otherwise just add it to the main config screen.
				} else list.add(configElement);
				
			}
		
		// Create and add category elements to the main config screen.
		for (Map.Entry<String, List<IConfigElement>> entry : categoryMap.entrySet())
			list.add(new CategoryElement(entry.getKey(), "config." + modId.toLowerCase() + ".category." + entry.getKey(), entry.getValue()));
		
		return list;
	}
	
	
	public static class SettingConfigElement<T> extends DummyConfigElement<T> {
		
		public final Config config;
		public final Setting<T> setting;
		
		public SettingConfigElement(Config config, Setting<T> setting, String modId,
		                            boolean reqMinecraftRestart, boolean reqWorldRestart) {
			super(setting.fullName, setting.defaultValue, getGuiType(setting),
			      "config." + modId.toLowerCase() + "." + setting.fullName,
			      getValidValues(setting), null, getMinValue(setting), getMaxValue(setting));
			this.config = config;
			this.setting = setting;
			requiresMcRestart = reqMinecraftRestart;
			requiresWorldRestart = reqWorldRestart;
		}
		
		@Override
		public Object get() { return config.get(setting); }
		
		@Override
		public void set(T value) {
			if (setting instanceof EnumSetting)
				value = (T)Enum.valueOf((Class<Enum>)setting.defaultValue.getClass(), (String)value);
			config.set(setting, value);
		}
		
		
		public static <T> ConfigGuiType getGuiType(Setting<T> setting) {
			if (setting instanceof BooleanSetting) return ConfigGuiType.BOOLEAN;
			else if (setting instanceof IntegerSetting) return ConfigGuiType.INTEGER;
			else if (setting instanceof DoubleSetting) return ConfigGuiType.DOUBLE;
			else if ((setting instanceof StringSetting) ||
			         (setting instanceof EnumSetting)) return ConfigGuiType.STRING;
			else throw new UnsupportedOperationException("Setting type '" + setting.getClass().getSimpleName() + "' not supported.");
		}
		public static <T> String[] getValidValues(Setting<T> setting) {
			if (setting instanceof IntegerSetting) {
				int[] validInts = ((IntegerSetting)setting).validValues;
				if (validInts == null) return null;
				String[] validValues = new String[validInts.length];
				for (int i = 0; i < validValues.length; i++)
					validValues[i] = String.valueOf(validInts[i]);
				return validValues;
			} else if (setting instanceof StringSetting)
				return ((StringSetting)setting).validValues;
			else if (setting instanceof EnumSetting) {
				Object[] values = ((EnumSetting)setting).defaultValue.getClass().getEnumConstants();
				String[] validValues = new String[values.length];
				for (int i = 0; i < validValues.length; i++)
					validValues[i] = values[i].toString();
				return validValues;
			} else return null;
		}
		public static <T> T getMinValue(Setting<T> setting) {
			if (setting instanceof IntegerSetting)
				return (T)(Object)((IntegerSetting)setting).minValue;
			if (setting instanceof DoubleSetting)
				return (T)(Object)((DoubleSetting)setting).minValue;
			else return null;
		}
		public static <T> T getMaxValue(Setting<T> setting) {
			if (setting instanceof IntegerSetting)
				return (T)(Object)((IntegerSetting)setting).maxValue;
			if (setting instanceof DoubleSetting)
				return (T)(Object)((DoubleSetting)setting).maxValue;
			else return null;
		}
		
	}
	
	public static class CategoryElement extends DummyCategoryElement {
		
		public CategoryElement(String name, String langKey, List<IConfigElement> childElements) {
			super(name, langKey, childElements);
		}
		
		@Override
		public Class<? extends IConfigEntry> getConfigEntryClass() { return Entry.class; }
		
		public static class Entry extends CategoryEntry {
			public boolean enabled = true;
			public Entry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
				super(owningScreen, owningEntryList, configElement);
			}
			@Override
			public boolean enabled() { return enabled; }
		}
		
	}
	
}
