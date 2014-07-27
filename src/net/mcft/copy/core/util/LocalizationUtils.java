package net.mcft.copy.core.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.resources.I18n;

public final class LocalizationUtils {
	
	private LocalizationUtils() {  }
	
	public static String translate(String key, Object... params) {
		return I18n.format(key, params);
	}
	public static String[] translateMultiline(String key, Object... params) {
		return translate(key, params).split("\\\\n");
	}
	
	public static String translateTooltip(String modId, String key, Object... params) {
		key = "tooltip." + modId.toLowerCase() + "." + key;
		return translate(key, params);
	}
	public static String[] translateTooltipMultiline(String modId, String key, Object... params) {
		return translateTooltip(modId, key, params).split("\\\\n");
	}
	public static void translateTooltipMultiline(List lines, String modId, String key, Object... params) {
		lines.addAll(Arrays.asList(translateTooltipMultiline(modId, key, params)));
	}
	
}
