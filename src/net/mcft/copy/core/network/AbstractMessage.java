package net.mcft.copy.core.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.mcft.copy.core.util.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractMessage<T extends AbstractMessage> implements IMessage, IMessageHandler<T, IMessage> {
	
	public abstract void write(PacketBuffer buffer) throws IOException;
	
	public abstract void read(PacketBuffer buffer) throws IOException;
	
	public abstract void handle(EntityPlayer player);
	
	// IMessage implementation
	
	@Override
	public final void fromBytes(ByteBuf buffer) {
		try { read(new PacketBuffer(buffer)); }
		catch (IOException ex) { throw new RuntimeException(ex); }
	}
	
	@Override
	public final void toBytes(ByteBuf buffer) {
		try { write(new PacketBuffer(buffer)); }
		catch (IOException ex) { throw new RuntimeException(ex); }
	}
	
	// IMessageHandler implementation
	
	@Override
	public final IMessage onMessage(T message, MessageContext context) {
		message.handle(context.side.isServer() ? context.getServerHandler().playerEntity
		                                       : ClientUtils.getLocalPlayer());
		return null;
	}
	
}
