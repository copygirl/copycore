package net.mcft.copy.core.container;

import java.util.HashMap;
import java.util.Map;

import net.mcft.copy.core.util.RegistryUtils;

public final class ContainerRegistry {
	
	private static Map<String, Class<? extends ContainerBase>> idToContainerLookup =
			new HashMap<String, Class<? extends ContainerBase>>();
	private static Map<Class<? extends ContainerBase>, String> containerToIdLookup =
			new HashMap<Class<? extends ContainerBase>, String>();
	
	private ContainerRegistry() {  }
	
	public static void registerMapping(Class<? extends ContainerBase> containerClass, String id) {
		idToContainerLookup.put(id, containerClass);
		containerToIdLookup.put(containerClass, id);
	}
	public static void register(Class<? extends ContainerBase> containerClass) {
		String id = RegistryUtils.getActiveModId() + ":" + containerClass.getSimpleName();
		registerMapping(containerClass, id);
	}
	
	public static String getIdFromContainer(Class<? extends ContainerBase> containerClass) {
		return containerToIdLookup.get(containerClass);
	}
	public static String getIdFromContainer(ContainerBase container) {
		return getIdFromContainer(container.getClass());
	}
	
	public static Class<? extends ContainerBase> getContainerFromId(String id) {
		return idToContainerLookup.get(id);
	}
	
}
