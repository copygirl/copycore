package net.mcft.copy.core.misc.rotatable;

public interface IRotatableBounds extends IRotatable4 {
	
	/** Returns the block's bounding box width. */
	float getBoundsWidth();
	
	/** Returns the block's bounding box height. */
	float getBoundsHeight();
	
	/** Returns the block's bounding box depth. */
	float getBoundsDepth();
	
}
