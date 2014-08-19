package net.mcft.copy.core.tweak;

import net.mcft.copy.core.config.ISettingChangedHandler;
import net.mcft.copy.core.config.setting.Setting;

public class SimpleTweakChangedHandler implements ISettingChangedHandler<Boolean> {
	
	private final Tweak tweak;
	
	public SimpleTweakChangedHandler(Tweak tweak) { this.tweak = tweak; }
	
	@Override
	public void onChanged(Setting<Boolean> setting, Boolean value) { tweak.setEnabled(value); }
	
}