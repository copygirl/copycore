package net.mcft.copy.core.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mcft.copy.core.copycore;
import net.mcft.copy.core.network.packet.MessageSyncProperties;
import net.mcft.copy.core.util.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class EntityPropertiesBase implements IExtendedEntityProperties {
	
	private static final List<String> syncedProperties = new ArrayList<String>();
	private static final Set<Class> checkedProperties = new HashSet<Class>();
	
	private Entity entity;
	
	private final List<EntityProperty> properties = new ArrayList<EntityProperty>();
	private final EntityProperty[] propertyIdMap = new EntityProperty[128];
	
	public EntityPropertiesBase() {
		// See if these properties need to be synced.
		if (!checkedProperties.contains(getClass()) && isSynced())
			syncedProperties.add(EntityUtils.getIdentifier(getClass()));
	}
	
	/** Returns the entity associated with the synced properties. */
	public Entity getEntity() { return entity; }
	
	/** Returns if the properties need to be synchronized with clients. */
	public boolean isSynced() { return false; }
	
	/** Returns the entity property associated with this id. */
	public EntityProperty getPropertyById(int id) { return propertyIdMap[id]; }
	
	@Override
	public void init(Entity entity, World world) {
		this.entity = entity;
	}
	
	/** Adds an entity property to the properties list. The order in which
	 *  items are added matters, as it's the order they will get loaded. */
	public void add(EntityProperty property) {
		property.id = properties.size();
		properties.add(property);
		propertyIdMap[property.id] = property;
	}
	
	/** Only called for properties that need to be synchronized. */
	public void update() {
		if (getEntity().worldObj.isRemote) return;
		MessageSyncProperties message;
		if ((message = getSynced(false, true)) != null)
			copycore.channel.sendToAllTracking(message, getEntity(), false);
		if (getEntity() instanceof EntityPlayer)
			if ((message = getSynced(true, true)) != null)
				copycore.channel.sendTo(message, (EntityPlayer)getEntity());
	}
	
	/** Returns a message with properties to be synchronized, or null if there's none.
	 *  If owner is true, includes properties that should only be sent to the owner of the entity.
	 *  If onlyChanged is true, only includes properties that have changed since the last call. */
	private MessageSyncProperties getSynced(boolean owner, boolean onlyChanged) {
		List<EntityProperty> synced = null;
		for (EntityProperty property : properties)
			if (property.isSynced() &&
			    (owner || property.isSyncedToAll() &&
			    (!onlyChanged || property.hasChanged())))
				((synced == null) ? (synced = new ArrayList<EntityProperty>()) : synced).add(property);
		return ((synced != null) ? new MessageSyncProperties(this, synced) : null);
	}
	
	/** Synchronize settings when a player starts tracking an entity. */
	public static void syncProperties(EntityPlayer player, Entity entity) {
		EntityPropertiesBase properties;
		MessageSyncProperties message;
		for (String identifier : syncedProperties)
			if ((properties = (EntityPropertiesBase)entity.getExtendedProperties(identifier)) != null)
				if ((message = properties.getSynced((player == entity), false)) != null)
					copycore.channel.sendTo(message, player);
	}
	
	// IExtendedEntityProperties saving / loading
	
	@Override
	public final void saveNBTData(NBTTagCompound compound) {
		String identifier = EntityUtils.getIdentifier(getClass());
		NBTTagCompound nbt = null;
		for (EntityProperty property : properties)
			if (property.isSaved())
				((nbt == null) ? (nbt = new NBTTagCompound()) : nbt).setTag(property.name, property.write());
		if (nbt != null)
			compound.setTag(identifier, nbt);
	}
	
	@Override
	public final void loadNBTData(NBTTagCompound compound) {
		String identifier = EntityUtils.getIdentifier(getClass());
		NBTTagCompound nbt = compound.getCompoundTag(identifier);
		if (nbt == null) return;
		NBTBase tag;
		for (EntityProperty property : properties)
			if ((tag = nbt.getTag(property.name)) != null)
				property.read(tag);
	}
	
}
