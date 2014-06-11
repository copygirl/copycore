package net.mcft.copy.core;

import java.io.File;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.tweak.TweakAutoReplace;
import net.mcft.copy.core.tweak.TweakSetting;

public class CoreConfig extends Config {
	
	public CoreConfig(File file) {
		super(file);
		
		new TweakSetting(this, TweakAutoReplace.instance).setComment(
				"When enabled, used up items and stacks will get replaced by ones above in the same column.");
		
	}
	
}
