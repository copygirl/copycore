package net.mcft.copy.core.network.packet;

import java.io.IOException;
import java.util.Set;

import net.mcft.copy.core.config.Config;
import net.mcft.copy.core.network.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

/** Synchronizes all config settings with clients that join the server. */
public class MessageSyncSettings extends AbstractMessage {
	
	public NBTTagCompound data;
	
	public MessageSyncSettings() {  }
	public MessageSyncSettings(Iterable<Config> configs) {
		data = new NBTTagCompound();
		for (Config config : configs)
			data.setTag(config.id, config.write(new NBTTagCompound()));
	}
	
	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeNBTTagCompoundToBuffer(data);
	}
	
	@Override
	public void read(PacketBuffer buffer) throws IOException {
		data = buffer.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public void handle(EntityPlayer player) {
		Config config;
		for (String id : (Set<String>)data.func_150296_c())
			if ((config = Config.getConfigById(id)) != null)
				config.read(data.getCompoundTag(id));
	}
	
}
