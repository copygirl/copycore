package net.mcft.copy.core.client.gui;

import java.util.Set;

import net.mcft.copy.core.CoreConfig;
import net.mcft.copy.core.copycore;
import net.mcft.copy.core.tweak.TweakAutoReplace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CoreGuiFactory implements IModGuiFactory {
	
	public static class CoreConfigGuiScreen extends GuiConfigBase {
		
		public CoreConfigGuiScreen(GuiScreen parentScreen) {
			super(parentScreen, copycore.MOD_ID, copycore.config);
		}
		
		@Override
		public void onConfigChanged() {
			super.onConfigChanged();
			TweakAutoReplace.instance.setEnabled(copycore.config.<Boolean>get(CoreConfig.tweakAutoReplace));
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
