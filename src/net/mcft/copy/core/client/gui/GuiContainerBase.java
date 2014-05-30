package net.mcft.copy.core.client.gui;

import net.mcft.copy.core.client.GuiTextureResource;
import net.mcft.copy.core.container.ContainerBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiContainerBase extends GuiContainer {
	
	public static final GuiTextureResource generic9by6 = new GuiTextureResource("generic_9x6", 256, 256);
	
	public static GuiTextureResource currentTexture;
	
	public String title;
	public Padding padding = new Padding(7, 16, 7, 7);
	
	public GuiContainerBase(ContainerBase container) {
		super(container);
		this.title = container.getTitleLocalized();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		guiLeft = getScreenX();
		guiTop = getScreenY();
		xSize = getWidth();
		ySize = getHeight();
	}
	
	/** Returns the total width of the GUI. */
	public int getWidth() { return (9 * 18 + padding.left + padding.right); }
	/** Returns the total height of the GUI. */
	public int getHeight() { return (4 * 18 + 90 + padding.top + padding.bottom); }
	
	/** Returns the total width of the screen. */
	public int getScreenWidth() { return width; }
	/** Returns the total height of the screen. */
	public int getScreenHeight() { return height; }
	
	/** Returns the X position at which the GUI should be drawn. */
	public int getScreenX() { return (getScreenWidth() - getWidth()) / 2; }
	/** Returns the Y position at which the GUI should be drawn. */
	public int getScreenY() { return (getScreenHeight() - getHeight()) / 2; }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(title, 8, 6, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 93, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture(generic9by6);
		drawQuad(guiLeft, guiTop, 0, 0, xSize, ySize - 97);
		drawQuad(guiLeft, guiTop + ySize - 97, 0, 124, xSize, 97);
	}
	
	public void bindTexture(GuiTextureResource texture) {
		mc.getTextureManager().bindTexture(texture);
		currentTexture = texture;
	}
	
	public void drawQuad(int x, int y, int u, int v, int w, int h)
    {
		float scaleX = 1.0F / currentTexture.defaultWidth;
		float scaleY = 1.0F / currentTexture.defaultHeight;
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.addVertexWithUV(x,     y + h, zLevel,  u      * scaleX, (v + h) * scaleY);
		tess.addVertexWithUV(x + w, y + h, zLevel, (u + w) * scaleX, (v + h) * scaleY);
		tess.addVertexWithUV(x + w, y,     zLevel, (u + w) * scaleX,  v      * scaleY);
		tess.addVertexWithUV(x,     y,     zLevel,  u      * scaleX,  v      * scaleY);
		tess.draw();
	}
	
}
