package net.mcft.copy.core.client.gui;

import java.util.Set;

import net.mcft.copy.core.CoreConfig;
import net.mcft.copy.core.copycore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CoreGuiFactory implements IModGuiFactory {
	
	public static class CoreConfigGuiScreen extends GuiConfig {
		
		public CoreConfigGuiScreen(GuiScreen parentScreen) {
			super(parentScreen, GuiConfigHelper.getElementsFor(CoreConfig.class, copycore.MOD_ID),
			      copycore.MOD_ID, false, false, copycore.MOD_ID);
		}
		
	}
	
	@Override
	public void initialize(Minecraft minecraftInstance) {  }
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() { return CoreConfigGuiScreen.class; }
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) { return null; }
	
}
