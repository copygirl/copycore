package net.mcft.copy.core.tweak;

public abstract class Tweak {
	
	private boolean enabled = false;
	
	/** Returns if this tweak is enabled. */
	public boolean isEnabled() { return enabled; }
	
	/** Enables or disables this tweak. */
	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		if (enabled) enable();
		else disable();
		this.enabled = enabled;
	}
	
	protected abstract void enable();
	
	protected abstract void disable();
	
}
