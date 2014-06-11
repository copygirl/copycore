package net.mcft.copy.core.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Color {
	
	public static final Color WHITE = fromRGB(0xFFFFFF);
	public static final Color BLACK = fromRGB(0x000000);
	public static final Color TRANSPARENT = fromRGBA(0x00000000);
	
	public final int red, green, blue, alpha;
	
	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	
	/** Returns a new color from the r, g, b and a values, all in ranges 0 - 255. */
	public static Color fromRGBA(int r, int g, int b, int a) {
		return new Color(r, g, b, a);
	}
	/** Returns a new color from a hex value in the format 0xRRGGBBAA. */
	public static Color fromRGBA(int hex) {
		return fromRGBA(hex >> 24, (hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF);
	}
	/** Returns a new color from a hex value in the format 0xAARRGGBB. */
	public static Color fromARGB(int hex) {
		return fromRGBA((hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF, hex >> 24);
	}
	
	/** Returns a new color from the r, g and b, all in ranges 0 - 255. */
	public static Color fromRGB(int r, int g, int b) {
		return fromRGBA(r, g, b, 255);
	}
	/** Returns a new color from a hex value in the format 0xRRGGBB. */
	public static Color fromRGB(int hex) {
		return fromRGB(hex >> 16, (hex >> 8) & 0xFF, hex & 0xFF);
	}
	
	
	/** Returns the color as a hex value in the format 0xRRGGBBAA. */
	public int toHexRGBA() {
		return ((alpha << 24) | (red << 16) | (green << 8) | blue);
	}
	/** Returns the color as a hex value in the format 0xAARRGGBB. */
	public int toHexARGB() {
		return ((red << 24) | (green << 16) | (blue << 8) | alpha);
	}
	/** Returns the color as a hex value in the format 0xRRGGBB. */
	public int toHexRGB() {
		return ((red << 16) | (green << 8) | blue);
	}
	
	
	/** Sets the active GL color to this color. */
	@SideOnly(Side.CLIENT)
	public void setActiveGLColor() {
		GL11.glColor4f(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
	}
	
	// Equals, hashCode and toString
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Color)) return false;
		Color color = (Color)obj;
		return (color.red == red) && (color.green == green) &&
		       (color.blue == blue) && (color.alpha == alpha);
	}
	
	@Override
	public int hashCode() { return toHexARGB(); }
	
	@Override
	public String toString() {
		return String.format("[RGB=%s,%s,%s; A=%s]", red, green, blue, alpha);
	}
	
}
