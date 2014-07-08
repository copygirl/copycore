package net.mcft.copy.core;

import java.io.File;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.tweak.TweakAutoReplace;

public class CoreConfig extends Config {
	
	public static Setting tweakAutoReplace =
			new BooleanSetting("tweaks.autoReplace", TweakAutoReplace.instance.isEnabled()).setComment(
					"When enabled, used up items and stacks will get replaced by ones above in the same column.");
	
	public CoreConfig(File file) {
		super(file);
		addAllViaReflection();
	}
	
}
