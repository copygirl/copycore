package net.mcft.copy.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
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
import net.minecraft.world.WorldServer;
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
		} catch (Exception ex) {
			copycore.log.error("Error decoding packet '{}':", packet.getClass().getSimpleName());
			ex.printStackTrace();
			return;
		}
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
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
		channel.writeAndFlush(channel).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	/** Sends a packet to a player. */
	public void sendToPlayer(EntityPlayer player, AbstractPacket packet) {
		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channel.writeAndFlush(channel).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	/** Sends a packet to everyone near a certain position in the world. */
	public void sendToEveryoneNear(World world, double x, double y, double z,
	                               double distance, AbstractPacket packet) {
		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, distance));
		channel.writeAndFlush(channel).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	/** Sends a packet to everyone near a certain position in the world except for one player. */
	public void sendToEveryoneNear(World world, double x, double y, double z, double distance,
	                               EntityPlayer except, AbstractPacket packet) {
		for (EntityPlayer player : (List<EntityPlayer>)world.playerEntities) {
			if (player == except) continue;
			double dx = x - player.posX;
			double dy = y - player.posY;
			double dz = z - player.posZ;
            if ((dx * dx + dy * dy + dz * dz) < (distance * distance))
            	sendToPlayer(player, packet);
		}
	}
	
	/** Sends a packet to everyone tracking this entity. */
	public void sendToEveryoneTracking(Entity entity, AbstractPacket packet) {
		((WorldServer)entity.worldObj).getEntityTracker().func_151247_a(
				entity, channels.get(Side.SERVER).generatePacketFrom(packet));
	}
	
}
