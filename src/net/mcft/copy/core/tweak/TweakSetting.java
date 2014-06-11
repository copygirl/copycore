package net.mcft.copy.core.tweak;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.config.setting.BooleanSetting;
import net.mcft.copy.core.util.NameUtils;

public class TweakSetting extends BooleanSetting {
	
	private final Tweak tweak;
	
	public TweakSetting(Config config, Tweak tweak, String name) {
		super(config, name, tweak.isEnabled());
		this.tweak = tweak;
	}
	public TweakSetting(Config config, Tweak tweak) {
		this(config, tweak, "tweaks." + NameUtils.getName(tweak));
	}
	
	@Override
	public TweakSetting setComment(String comment) {
		super.setComment(comment);
		return this;
	}
	@Override
	public TweakSetting setSynced() {
		super.setSynced();
		return this;
	}
	
	@Override
	public void setSyncedValue(Boolean value) {
		super.setSyncedValue(value);
		tweak.setEnabled(value);
	}
	
}
