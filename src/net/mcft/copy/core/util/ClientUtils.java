package net.mcft.copy.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class ClientUtils {
	
	private static Timer mcTimer;
	
	private ClientUtils() {  }
	
	public static EntityPlayer getLocalPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public static World getLocalWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
	@SideOnly(Side.CLIENT)
	public static Timer getTimer() {
		return ((mcTimer == null) ? (mcTimer = ReflectionHelper.getPrivateValue(
				Minecraft.class, Minecraft.getMinecraft(), "field_71428_T", "timer")) : mcTimer);
	}
	
	@SideOnly(Side.CLIENT)
	public static float getPartialTicks() {
		return getTimer().renderPartialTicks;
	}
	
}
