package net.mcft.copy.core;

import java.io.File;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.IntegerSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.tweak.TweakAutoReplace;
import net.mcft.copy.core.tweak.TweakDoubleDoorInteraction;
import net.mcft.copy.core.tweak.TweakPlayerDeathItemDespawnTime;

public class CoreConfig extends Config {
	
	public static Setting tweakAutoReplace =
			new BooleanSetting("tweaks.autoReplace", TweakAutoReplace.instance.isEnabled()).setComment(
					"When enabled, used up items and stacks will get replaced by ones above in the same column.");
	
	public static Setting tweakDoubleDoorInteraction =
			new BooleanSetting("tweaks.doubleDoorInteraction", TweakDoubleDoorInteraction.instance.isEnabled()).setComment(
					"When enabled, interacting with a wooden double door opens both doors at once.");
	
	public static Setting tweakPlayerDeathItemDespawnTime =
			new IntegerSetting("tweaks.playerDeathitemDespawnTime", -1).setValidRange(-1, Integer.MAX_VALUE).setComment(
					"The time in seconds items will stay in the world when dropped from a player death. Use -1 to disable the tweak.");
	
	public CoreConfig(File file) {
		super(file);
		addAllViaReflection();
	}
	
	@Override
	public void update() {
		TweakAutoReplace.instance.setEnabled(copycore.config.<Boolean>get(tweakAutoReplace));
		TweakDoubleDoorInteraction.instance.setEnabled(copycore.config.<Boolean>get(tweakDoubleDoorInteraction));
		
		int despawnTime = this.<Integer>get(tweakPlayerDeathItemDespawnTime);
		TweakPlayerDeathItemDespawnTime.instance.setEnabled(despawnTime >= 0);
		TweakPlayerDeathItemDespawnTime.instance.despawnTime = despawnTime;
	}
	
}
