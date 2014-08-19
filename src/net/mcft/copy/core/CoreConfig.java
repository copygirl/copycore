package net.mcft.copy.core;

import java.io.File;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.SyncedSetting;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.config.setting.IntegerSetting;
import net.mcft.copy.core.config.setting.Setting;
import net.mcft.copy.core.tweak.TweakAutoReplace;
import net.mcft.copy.core.tweak.TweakDoubleDoorInteraction;

public class CoreConfig extends Config {
	
	@SyncedSetting
	public static Setting tweakAutoReplace =
			new BooleanSetting("tweaks.autoReplace", TweakAutoReplace.instance.isEnabled()).setComment(
					"When enabled, used up items and stacks will get replaced by ones above in the same column.");
	
	@SyncedSetting
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
	
}
