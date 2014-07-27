package net.mcft.copy.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class WorldUtils {
	
	private WorldUtils() {  }
	
	/** Spawns an ItemStack in the world. */
	public static EntityItem spawnItem(World world, double x, double y, double z, ItemStack stack) {
		if ((stack == null) || (stack.stackSize <= 0)) return null;
		EntityItem item = new EntityItem(world, x, y, z, stack);
		world.spawnEntityInWorld(item);
		return item;
	}
	/** Spawns an ItemStack in the world with random motion. */
	public static EntityItem spawnItemWithMotion(World world, double x, double y, double z, ItemStack stack) {
		EntityItem item = spawnItem(world, x, y, z, stack);
		if (item == null) return null;
		item.motionX = RandomUtils.getGaussian() * 0.05F;
		item.motionY = RandomUtils.getGaussian() * 0.05F + 0.2F;
		item.motionZ = RandomUtils.getGaussian() * 0.05F;
		return item;
	}
	
	/** Spawn an ItemStack dropping from a destroyed block. */
	public static EntityItem dropStackFromBlock(World world, int x, int y, int z, ItemStack stack) {
		float itemX = x + RandomUtils.getFloat(0.1F, 0.9F);
		float itemY = y + RandomUtils.getFloat(0.1F, 0.9F);
		float itemZ = z + RandomUtils.getFloat(0.1F, 0.9F);
		return spawnItemWithMotion(world, itemX, itemY, itemZ, stack);
	}
	/** Spawn an ItemStack dropping from a destroyed block. */
	public static EntityItem dropStackFromBlock(TileEntity te, ItemStack stack) {
		return dropStackFromBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord, stack);
	}
	
	/** Spawns an ItemStack as if it was dropped from an entity on death. */
	public static EntityItem dropStackFromEntity(Entity entity, ItemStack stack, float speed) {
		EntityPlayer player = ((entity instanceof EntityPlayer) ? (EntityPlayer)entity : null);
		EntityItem item;
		if (player == null) {
			double y = entity.posY + entity.getEyeHeight() - 0.3;
			item = spawnItem(entity.worldObj, entity.posX, y, entity.posZ, stack);
			if (item == null) return null;
			item.delayBeforeCanPickup = 40;
			float f1 = RandomUtils.getFloat(0.5F);
			float f2 = RandomUtils.getFloat((float)Math.PI * 2.0F);
			item.motionX = -Math.sin(f2) * f1;
			item.motionY = 0.2;
			item.motionZ = Math.cos(f2) * f1;
			return item;
		} else item = player.dropPlayerItemWithRandomChoice(stack, true);
		if (item != null) {
			item.motionX *= speed / 4;
			item.motionZ *= speed / 4;
		}
		return item;
	}
	/** Spawns multiple ItemStacks as if they were dropped from an entity on death. */
	public static void dropStacksFromEntity(Entity entity, Iterable<ItemStack> stacks, float speed) {
		for (ItemStack stack : stacks)
			dropStackFromEntity(entity, stack, speed);
	}
	
}
