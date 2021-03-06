package net.mcft.copy.core.handler;

import java.util.LinkedList;
import java.util.List;

import net.mcft.copy.core.copycore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelReloader implements IResourceManagerReloadListener {
	
	private static final ModelReloader instance = new ModelReloader();
	
	private List<IReloadableModel> modelsToReload = new LinkedList<IReloadableModel>();
	
	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		copycore.log.info("Reloading {} models.", modelsToReload.size());
		for (IReloadableModel model : modelsToReload)
			model.reload();
	}
	
	public static void register() {
		IResourceManager manager = Minecraft.getMinecraft().getResourceManager();
		if (manager instanceof IReloadableResourceManager)
			((IReloadableResourceManager)manager).registerReloadListener(instance);
		else copycore.log.warn("Resource manager is not reloadable?");
	}
	
	/** Registers a model to be reloaded when resource manager has
	 *  been reloaded, for example when resource packs have changed. <br>
	 *  The reload method of the model is called afterwards */
	public static void registerModel(IReloadableModel model) {
		instance.modelsToReload.add(model);
	}
	
	public interface IReloadableModel {
		
		/** Reloads this model. Called after the resource manager has been reloaded */
		public void reload();
		
	}
	
}
