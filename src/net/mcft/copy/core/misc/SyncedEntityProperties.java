package net.mcft.copy.core.misc;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.network.packet.PacketSyncProperties;
import net.mcft.copy.core.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class SyncedEntityProperties implements IExtendedEntityProperties {
	
	// TODO: Sync properties when players start to track it.
	
	private Entity entity;
	
	/** Returns the entity associated with the synced properties. */
	public Entity getEntity() { return entity; }
	
	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}
	
	/** Synchronizes these properties to all players tracking the entity. */ 
	public void sync() {
		if (!requiresSyncing()) return;
		copycore.channelHandler.sendToEveryoneTracking(
				entity, new PacketSyncProperties(this));
	}
	/** Synchronizes these properties to a specific player. */ 
	public void sync(EntityPlayer player) {
		if (!requiresSyncing()) return;
		copycore.channelHandler.sendToPlayer(
				player, new PacketSyncProperties(this));
	}
	
	// Saving, loading and syncing
	
	/** Returns if these properties should be written to the entity. */
	public boolean isWrittenToEntity() { return false; }
	/** Returns if these properties should be synced to clients. */
	public boolean requiresSyncing() { return false; }
	
	/** Handles writing data to both the entity and sync packet. */
	public void write(NBTTagCompound compound) {  }
	/** Handles read data from both the save and sync packet. */
	public void read(NBTTagCompound compound) {  }
	
	/** Handles writing data to the entity. */
	public void writeToEntity(NBTTagCompound compound) {  }
	/** Handles read data from the entity. */
	public void readFromEntity(NBTTagCompound compound) {  }
	
	/** Handles writing data to the sync packet, to be sent to clients. */
	public void writeToSyncPacket(NBTTagCompound compound) {  }
	/** Handles read data from the sync packet, sent from the server. */
	public void readFromSyncPacket(NBTTagCompound compound) {  }
	
	// IExtendedEntityProperties saving / loading
	
	@Override
	public final void saveNBTData(NBTTagCompound compound) {
		if (!isWrittenToEntity()) return;
		String identifier = EntityUtils.getIdentifier(getClass());
		NBTTagCompound properties = new NBTTagCompound();
		write(properties);
		writeToEntity(properties);
		compound.setTag(identifier, properties);
	}
	
	@Override
	public final void loadNBTData(NBTTagCompound compound) {
		String identifier = EntityUtils.getIdentifier(getClass());
		NBTTagCompound properties = compound.getCompoundTag(identifier);
		if (properties == null) return;
		read(properties);
		readFromEntity(properties);
	}
	
}
