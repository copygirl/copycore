package net.mcft.copy.core.tweak;

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
		int x = event.x, y = event.y, z = event.z;
		if ((event.action != Action.RIGHT_CLICK_BLOCK) ||
			!(event.world.getBlock(x, y, z) == door) ||
		    event.entityPlayer.isSneaking()) return;
		
		int direction = door.func_150013_e(event.world, x, y, z);
		boolean open = door.func_150015_f(event.world, x, y, z);
		boolean mirror = ((door.func_150012_g(event.world, x, y, z) & 16) != 0);
		
		switch (direction) {
			case 0: z -= (mirror ? 1 : -1); break;
			case 1: x += (mirror ? 1 : -1); break;
			case 2: z += (mirror ? 1 : -1); break;
			case 3: x -= (mirror ? 1 : -1); break;
		}
		
		if ((event.world.getBlock(x, y, z) == door) &&
		    (door.func_150013_e(event.world, x, y, z) == direction) &&
		    (door.func_150015_f(event.world, x, y, z) == open) &&
		    (((door.func_150012_g(event.world, x, y, z) & 16) != 0) != mirror))
			door.onBlockActivated(event.world, x, y, z, event.entityPlayer, event.face, 0, 0, 0);
	}
	
}
