package net.mcft.copy.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class ClientUtils {
	
	private ClientUtils() {  }
	
	public static EntityPlayer getLocalPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public static World getLocalWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
}
