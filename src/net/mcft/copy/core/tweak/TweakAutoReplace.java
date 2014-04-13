package net.mcft.copy.core.tweak;

import net.mcft.copy.core.util.StackUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/** Auto replaces used-up stacks and broken items in a player's inventory,
 *  if items of the same type exist in the same column above the item. <br>
 *  This tweak only works server-side. */
public final class TweakAutoReplace extends Tweak {
	
	public static final TweakAutoReplace instance = new TweakAutoReplace();
	
	private TweakAutoReplace() {  }
	
	@Override
	public void enableInternal() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void disableInternal() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent
	public void onPlayerDestroyItem(PlayerDestroyItemEvent event) {
		
		if (event.entity.worldObj.isRemote) return;
		
		EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		
		ItemStack currentItem = inv.getCurrentItem();
		if ((currentItem != null) &&
		    (currentItem != event.original)) return;
		
		int row;
		for (row = 2; row >= 0; row--) {
			int slot = inv.currentItem + row * 9 + 9;
			ItemStack stackAbove = inv.getStackInSlot(slot);
			if (!canReplace(stackAbove, event.original)) break;
			int targetSlot = ((slot < 27) ? (slot + 9) : (slot - 27));
			inv.setInventorySlotContents(targetSlot, stackAbove);
			inv.setInventorySlotContents(slot, null);
			player.playerNetServerHandler.sendPacket(
					new S2FPacketSetSlot(0, slot + 9, stackAbove));
		}
		
		if (row < 2)
			player.playerNetServerHandler.sendPacket(
					new S2FPacketSetSlot(0, inv.currentItem + row * 9 + 18, null));
		
	}
	
	/** Returns if the destroyed item can be replaced with this item. */
	private static boolean canReplace(ItemStack replacement, ItemStack destroyed) {
		// TODO: Allow tools of the same type to act as replacement?
		// Also, maybe allow the same item with different NBT data (enchantments)?
		return StackUtils.equals(replacement, destroyed, false, true, !destroyed.getItem().isDamageable());
	}
	
}
