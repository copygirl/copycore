package net.mcft.copy.core.network.packet;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Set;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.network.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

/** Synchronizes all config settings
 *  with clients that join the server. */
public class PacketSyncSettings extends AbstractPacket {
	
	public NBTTagCompound data;
	
	public PacketSyncSettings() {  }
	public PacketSyncSettings(Iterable<Config> configs) {
		data = new NBTTagCompound();
		for (Config config : configs)
			data.setTag(config.id, config.write(new NBTTagCompound()));
	}
	
	@Override
	public void encode(ChannelHandlerContext context, PacketBuffer buffer) throws IOException {
		buffer.writeNBTTagCompoundToBuffer(data);
	}
	
	@Override
	public void decode(ChannelHandlerContext context, PacketBuffer buffer) throws IOException {
		data = buffer.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		Config config;
		for (String id : (Set<String>)data.func_150296_c())
			if ((config = Config.getConfigById(id)) != null)
				config.read(data.getCompoundTag(id));
	}
	
}
