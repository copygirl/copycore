package net.mcft.copy.core.base;

import net.mcft.copy.core.util.NameUtils;
import net.mcft.copy.core.util.RegistryUtils;
import net.mcft.copy.core.util.RegistryUtils.IRegistrable;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBase extends Item implements IRegistrable {
	
	private String name;
	
	public ItemBase() {
		setTextureName(RegistryUtils.getActiveModId() + ":" + getName());
		setMaxStackSize(1);
	}

	/** Called only once, then used as a return value in getName(). */
	protected String getNameInternal() {
		return NameUtils.getGameItemName(this);
	}
	
	// IRegistrable implementation
	
	@Override
	public String getName() {
		return ((name == null) ? getNameInternal() : name);
	}
	
	/** Registers the item in the GameRegistry, as well as sets the item name. */
	@Override
	public <T extends IRegistrable> T register() {
		setUnlocalizedName(getName());
		GameRegistry.registerItem(this, getName());
		return (T)this;
	}
	
}
