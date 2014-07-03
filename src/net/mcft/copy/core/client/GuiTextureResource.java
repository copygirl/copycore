package net.mcft.copy.core.client;

public class GuiTextureResource extends ModResource {
	
	public final int defaultWidth;
	public final int defaultHeight;
	
	public GuiTextureResource(String modid, String location, int defaultWidth, int defaultHeight) {
		super(modid, "textures/gui/" + location + ".png");
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}
	
}
