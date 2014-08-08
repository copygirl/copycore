package net.mcft.copy.core.tweak;

import net.mcft.copy.core.misc.BlockLocation;
import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TweakDoubleDoorInteraction extends Tweak {
	
	public static final TweakDoubleDoorInteraction instance = new TweakDoubleDoorInteraction();
	
	private TweakDoubleDoorInteraction() {  }
	
	@Override
	protected void enable() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void disable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	@SubscribeEvent(priority=EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEvent event) {
		BlockDoor door = (BlockDoor)Blocks.wooden_door;
		BlockLocation loc = BlockLocation.get(event.world, event.x, event.y, event.z);
		
		if ((event.action != Action.RIGHT_CLICK_BLOCK) ||
		    event.entityPlayer.isSneaking() ||
		    !(loc.getBlock() == door)) return;
		
		int direction = getDoorOrientation(door, loc);
		boolean isOpen = isDoorOpen(door, loc);
		boolean isMirrored = isDoorMirrored(door, loc);
		
		int i = (isMirrored ? -1 : 1);
		switch (direction) {
			case 0: loc = loc.relative(0, 0,  i); break;
			case 1: loc = loc.relative(-i, 0, 0); break;
			case 2: loc = loc.relative(0, 0, -i); break;
			case 3: loc = loc.relative( i, 0, 0); break;
		}
		
		if ((loc.getBlock() == door) &&
		    (getDoorOrientation(door, loc) == direction) &&
		    (isDoorOpen(door, loc) == isOpen) &&
		    (isDoorMirrored(door, loc) != isMirrored))
			door.onBlockActivated(loc.world, loc.x, loc.y, loc.z, event.entityPlayer, event.face, 0, 0, 0);
	}
	
	private int getDoorOrientation(BlockDoor door, BlockLocation loc) {
		return door.func_150013_e(loc.blockAccess, loc.x, loc.y, loc.z);
	}
	private boolean isDoorOpen(BlockDoor door, BlockLocation loc) {
		return door.func_150015_f(loc.blockAccess, loc.x, loc.y, loc.z);
	}
	private boolean isDoorMirrored(BlockDoor door, BlockLocation loc) {
		return ((door.func_150012_g(loc.blockAccess, loc.x, loc.y, loc.z) & 16) != 0);
	}
	
}
