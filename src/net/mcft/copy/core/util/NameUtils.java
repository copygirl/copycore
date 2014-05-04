package net.mcft.copy.core.util;

import java.util.Locale;

public final class NameUtils {
	
	private NameUtils() {  }

	/** Gets the name of a game object from its class name. <br>
	 *  For example: <code>BlockCraftingStation</code> => <code>craftingStation</code> */
	public static String getName(Object object) {
		return getName(object.getClass());
	}
	/** Gets the name from this class' name. <br>
	 *  For example: <code>BlockCraftingStation</code> => <code>craftingStation</code> */
	public static String getName(Class clazz) {
		String fullName = clazz.getSimpleName();
		int begin = 1;
		while (Character.isLowerCase(fullName.charAt(begin)))
			begin++;
		return (fullName.substring(begin, begin + 1).toLowerCase(Locale.ENGLISH) +
		        fullName.substring(begin + 1));
	}
	
	/** Gets the "game name" of a game object from its class name. <br>
	 *  For example: <code>BlockCraftingStation</code> => <code>crafting_station</code> */
	public static String getGameItemName(Object object) {
		return getName(object.getClass());
	}
	/** Gets the "game name" from this class' name. <br>
	 *  For example: <code>BlockCraftingStation</code> => <code>crafting_station</code> */
	public static String getGameItemName(Class clazz) {
		String fullName = clazz.getSimpleName();
		StringBuilder str = new StringBuilder(fullName.length());
		int i = 1;
		for (; Character.isLowerCase(fullName.charAt(i)); i++) {  }
		str.append(Character.toLowerCase(fullName.charAt(i)));
		for (i++; i < fullName.length(); i++) {
			char chr = fullName.charAt(i);
			if (Character.isUpperCase(chr))
				str.append('_').append(Character.toLowerCase(chr));
			else str.append(chr);
		}
		return str.toString();
	}
	
}
