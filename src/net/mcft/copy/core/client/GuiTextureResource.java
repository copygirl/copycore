package net.mcft.copy.core.client;

public class GuiTextureResource extends ModResourceLocation {
	
	public final int defaultWidth;
	public final int defaultHeight;
	
	public GuiTextureResource(String location, int defaultWidth, int defaultHeight) {
		super("textures/gui/" + location + ".png");
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}
	
}
