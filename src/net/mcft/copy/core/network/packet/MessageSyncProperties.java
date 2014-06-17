package net.mcft.copy.core.network.packet;

import java.io.IOException;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.misc.SyncedEntityProperties;
import net.mcft.copy.core.network.AbstractMessage;
import net.mcft.copy.core.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.IExtendedEntityProperties;

/** Synchronizes SyncedEntityProperties with players who
 *  first see an entity or whenever the properties update. */
public class MessageSyncProperties extends AbstractMessage {
	
	public int entityId;
	public String identifier;
	public NBTTagCompound data;
	
	public MessageSyncProperties() {  }
	public MessageSyncProperties(SyncedEntityProperties properties) {
		entityId = properties.getEntity().getEntityId();
		identifier = EntityUtils.getIdentifier(properties.getClass());
		data = new NBTTagCompound();
		properties.write(data);
		properties.writeToSyncPacket(data);
	}
	
	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeInt(entityId);
		buffer.writeStringToBuffer(identifier);
		buffer.writeNBTTagCompoundToBuffer(data);
	}
	
	@Override
	public void read(PacketBuffer buffer) throws IOException {
		entityId = buffer.readInt();
		identifier = buffer.readStringFromBuffer(128);
		data = buffer.readNBTTagCompoundFromBuffer();
	}
	
	@Override
	public void handle(EntityPlayer player) {
		Entity entity = player.worldObj.getEntityByID(entityId);
		if (entity == null) {
			copycore.log.warn("Couldn't find entity to sync to for properties '{}'.", identifier);
			return;
		}
		IExtendedEntityProperties properties = entity.getExtendedProperties(identifier);
		if (!(properties instanceof SyncedEntityProperties)) {
			copycore.log.warn("No valid syncable properties found for '{}'.", identifier);
			return;
		}
		SyncedEntityProperties syncProperties = (SyncedEntityProperties)properties;
		syncProperties.read(data);
		syncProperties.readFromSyncPacket(data);
	}
	
}