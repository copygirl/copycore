package net.mcft.copy.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;
import java.util.List;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.util.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@ChannelHandler.Sharable
public class ChannelHandler extends FMLIndexedMessageToMessageCodec<AbstractPacket> {
	
	private final EnumMap<Side, FMLEmbeddedChannel> channels;
	
	public ChannelHandler(String name) {
		channels = NetworkRegistry.INSTANCE.newChannel(name, this);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext context, AbstractPacket packet, ByteBuf target) throws Exception {
		packet.encode(context, new PacketBuffer(target));
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf source, AbstractPacket packet) {
		try {
			packet.decode(context, new PacketBuffer(source));
		} catch (Throwable t) {
			copycore.getLogger().error("Error decoding packet '%s': %s",
			                           packet.getClass().getSimpleName(), t);
			return;
		}
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			INetHandler netHandler = context.channel().attr(NetworkRegistry.NET_HANDLER).get();
			EntityPlayer player = ((NetHandlerPlayServer)netHandler).playerEntity;
			packet.handleServerSide(player);
		} else packet.handleClientSide(ClientUtils.getLocalPlayer());
	}
	
	// Sending packets
	
	/** Sends a packet to the server. */
	public void sendToServer(AbstractPacket packet) {
		FMLEmbeddedChannel channel = channels.get(Side.CLIENT);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channel.writeAndFlush(channel);
	}
	
	/** Sends a packet to a player. */
	public void sendToPlayer(EntityPlayer player, AbstractPacket packet) {
		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channel.writeAndFlush(packet);
	}
	
	/** Sends a packet to everyone near a certain position in the world. */
	public void sendToEveryoneNear(World world, double x, double y, double z,
	                               double distance, AbstractPacket packet) {
		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, distance));
		channel.writeAndFlush(packet);
	}
	
	/** Sends a packet to everyone near this entity. */
	public void sendToEveryoneNear(Entity entity, AbstractPacket packet) {
		// TODO: In the best case, there should be a sendToEveryoneTracking.
		// At the moment this is only possible using vanilla packets or through
		// private fields and lots of custom code.
		sendToEveryoneNear(entity.worldObj, entity.posX, entity.posY, entity.posZ, 128, packet);
	}
	
	/** Sends a packet to everyone near a certain position in the world except for one player. */
	public void sendToEveryoneNear(World world, double x, double y, double z, double distance,
	                               EntityPlayer except, AbstractPacket packet) {
		for (EntityPlayer player : (List<EntityPlayer>)world.playerEntities) {
			double dx = x - player.posX;
			double dy = y - player.posY;
			double dz = z - player.posZ;
            if ((dx * dx + dy * dy + dz * dz) < (distance * distance))
            	sendToPlayer(player, packet);
            	
		}
	}
	
}
