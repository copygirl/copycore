package net.mcft.copy.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class ClientUtils {
	
	private ClientUtils() {  }
	
	public EntityPlayer getLocalPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	public World getLocalWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
}
