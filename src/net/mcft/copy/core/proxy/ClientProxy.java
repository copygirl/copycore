package net.mcft.copy.core.proxy;

import net.mcft.copy.backpacks.block.tileentity.TileEntityBackpack;
import net.mcft.copy.core.handler.ModelReloader;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void init() {
		
		super.init();
		
		ModelReloader.register();
		
	}
	
}
