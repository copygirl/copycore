package net.mcft.copy.core.inventory.slot;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.mcft.copy.core.misc.Enumerator;
import net.mcft.copy.core.misc.IEnumerable;
import net.minecraft.inventory.IInventory;

public class SlotGroup implements IEnumerable<SlotBase> {
	
	public List<SlotBase> slots = new ArrayList<SlotBase>();
	
	public SlotGroup() {  }
	public SlotGroup(Class<? extends SlotBase> slotClass,
	                 IInventory inventory, int startIndex,
	                 int x, int y, int columns, int rows) {
		try {
			Constructor<? extends SlotBase> constructor =
					slotClass.getConstructor(IInventory.class, int.class, int.class, int.class);
			for (int j = 0; j < rows; j++)
				for (int i = 0; i < columns; i++) {
					int index = startIndex + i + j * columns;
					int xx = x + i * 18;
					int yy = y + j * 18;
					add(constructor.newInstance(inventory, index, xx, yy));
				}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	/** Adds a slot to this slot group. */
	public void add(SlotBase slot) {
		slots.add(slot);
	}
	
	// IEnumerable implementation
	
	@Override
	public Enumerator<SlotBase> iterator() { return Enumerator.of(slots); }
	
}
