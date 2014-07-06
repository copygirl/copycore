package net.mcft.copy.core.client;

import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTextureResource extends ModResource {
	
	public final int defaultWidth;
	public final int defaultHeight;
	
	public GuiTextureResource(String modid, String location, int defaultWidth, int defaultHeight) {
		super(modid, "textures/gui/" + location + ".png");
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
	}
	
	/** Draws part of the texture to the screen. */
	public void drawQuad(int x, int y, int u, int v, int w, int h, float zLevel) {
		float scaleX = 1.0F / defaultWidth;
		float scaleY = 1.0F / defaultHeight;
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x,     y + h, zLevel,  u      * scaleX, (v + h) * scaleY);
		tess.addVertexWithUV(x + w, y + h, zLevel, (u + w) * scaleX, (v + h) * scaleY);
		tess.addVertexWithUV(x + w, y,     zLevel, (u + w) * scaleX,  v      * scaleY);
		tess.addVertexWithUV(x,     y,     zLevel,  u      * scaleX,  v      * scaleY);
		tess.draw();
	}
	
	/** Draws part of the texture to the screen. */
	public void drawQuad(int x, int y, int u, int v, int w, int h) {
		drawQuad(x, y, u, v, w, h, 0);
	}
	
}
