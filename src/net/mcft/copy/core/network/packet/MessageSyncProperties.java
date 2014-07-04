package net.mcft.copy.core.network.packet;

import java.io.IOException;
import java.util.List;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.entity.EntityPropertiesBase;
import net.mcft.copy.core.entity.EntityProperty;
import net.mcft.copy.core.network.AbstractMessage;
import net.mcft.copy.core.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.IExtendedEntityProperties;

/** Synchronizes SyncedEntityProperties with players who
 *  first see an entity or whenever the properties update. */
public class MessageSyncProperties extends AbstractMessage {
	
	public int entityId;
	public String identifier;
	public List<EntityProperty> propertyList;
	public PacketBuffer propertyBuffer;
	
	public MessageSyncProperties() {  }
	public MessageSyncProperties(EntityPropertiesBase properties, List<EntityProperty> propertyList) {
		entityId = properties.getEntity().getEntityId();
		identifier = EntityUtils.getIdentifier(properties.getClass());
		this.propertyList = propertyList;
	}
	
	@Override
	public void write(PacketBuffer buffer) throws IOException {
		buffer.writeInt(entityId);
		buffer.writeStringToBuffer(identifier);
		
		int index = buffer.writerIndex();
		buffer.writeInt(0);
		
		buffer.writeByte(propertyList.size());
		for (EntityProperty property : propertyList) {
			buffer.writeByte(property.id);
			property.write(buffer);
		}
		
		// Retroactively set the length of the part of
		// the buffer that contains the properties list.
		buffer.setInt(index, buffer.writerIndex() - index - 4);
	}
	
	@Override
	public void read(PacketBuffer buffer) throws IOException {
		entityId = buffer.readInt();
		identifier = buffer.readStringFromBuffer(128);
		propertyBuffer = new PacketBuffer(buffer.readBytes(buffer.readInt()));
	}
	
	@Override
	public void handle(EntityPlayer player) {
		Entity entity = player.worldObj.getEntityByID(entityId);
		if (entity == null) {
			copycore.log.warn("Couldn't find entity to sync to for properties '{}'.", identifier);
			return;
		}
		IExtendedEntityProperties properties = entity.getExtendedProperties(identifier);
		if (!(properties instanceof EntityPropertiesBase)) {
			copycore.log.warn("No valid syncable properties found for '{}'.", identifier);
			return;
		}
		EntityPropertiesBase syncProperties = (EntityPropertiesBase)properties;
		int amount = propertyBuffer.readByte();
		for (int i = 0; i < amount; i++)
			syncProperties.getPropertyById(propertyBuffer.readByte()).read(propertyBuffer);
	}
	
}
