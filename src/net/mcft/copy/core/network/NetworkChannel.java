package net.mcft.copy.core.network;

import java.util.List;

import net.mcft.copy.core.util.RegistryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkChannel extends SimpleNetworkWrapper {
	
	public NetworkChannel(String name) {
		super(name);
	}
	public NetworkChannel() {
		this(RegistryUtils.getActiveModId());
	}
	
	/** Register a message type with this message handler on this side using the ID. */
	public <T extends IMessage & IMessageHandler<T, IMessage>> void register(int id, Side receivingSide, Class<T> messageClass) {
		registerMessage(messageClass, messageClass, id, receivingSide);
	}
	
	public void sendTo(IMessage message, EntityPlayer player) {
		sendTo(message, (EntityPlayerMP)player);
	}
	
	public void sendToAllAround(IMessage message, World world, double x, double y, double z, double distance) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, distance));
	}
	
	public void sendToAllAround(IMessage message, World world, double x, double y, double z,
	                            double distance, EntityPlayer except) {
		for (EntityPlayer player : (List<EntityPlayer>)world.playerEntities) {
			if (player == except) continue;
			double dx = x - player.posX;
			double dy = y - player.posY;
			double dz = z - player.posZ;
            if ((dx * dx + dy * dy + dz * dz) < (distance * distance))
            	sendTo(message, player);
		}
	}
	
	public void sendToAllTracking(IMessage message, Entity entity) {
		((WorldServer)entity.worldObj).getEntityTracker().func_151247_a(entity, getPacketFrom(message));
	}
	
}
