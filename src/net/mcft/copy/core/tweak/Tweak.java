package net.mcft.copy.core.tweak;

public abstract class Tweak {
	
	private boolean enabled = false;
	
	/** Returns if this tweak is enabled. */
	public boolean isEnabled() { return enabled; }
	
	/** Enables this tweak. */
	public void enable() {
		if (enabled) return;
		enableInternal();
		enabled = false;
	}
	
	/** Disables this tweak. */
	public void disable() {
		if (!enabled) return;
		disableInternal();
		enabled = false;
	}
	
	protected abstract void enableInternal();
	
	protected abstract void disableInternal();
	
}
