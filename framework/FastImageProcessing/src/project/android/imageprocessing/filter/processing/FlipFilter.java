package project.android.imageprocessing.filter.processing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import project.android.imageprocessing.filter.BasicFilter;

/**
 * Flips the image in the given orientation.
 * flipDirection: The direction to flip the image
 * @author Chris Batt
 */
public class FlipFilter extends BasicFilter {
	public static final int FLIP_VERTICAL = 0;
	public static final int FLIP_HORIZONTAL = 1;
	public static final int FLIP_BOTH = 2;
	
	public FlipFilter(int flipDirection) {
		textureVertices = new FloatBuffer[4];
		
		float minX = 0f;
		float maxX = 1f;
		float minY = 0f;
		float maxY = 1f;
		switch(flipDirection) {
			case FLIP_VERTICAL: minX = 1f; maxX = 0f; break;
			case FLIP_HORIZONTAL: minY = 1f; maxY = 0f; break;
			case FLIP_BOTH: minX = 1f; maxX = 0f; minY = 1f; maxY = 0f; break;
		}
		
		float[] texData0 = new float[] {
			minX, minY,
			maxX, minY,
			minX, maxY,
			maxX, maxY,
		};
		textureVertices[0] = ByteBuffer.allocateDirect(texData0.length * 4).order(ByteOrder. nativeOrder()).asFloatBuffer();
		textureVertices[0].put(texData0).position(0);
		
		float[] texData1 = new float[] {
			minY, maxX,
			minY, minX,
	        maxY, maxX,
	        maxY, minX,
		};
		textureVertices[1] = ByteBuffer.allocateDirect(texData1.length * 4).order(ByteOrder. nativeOrder()).asFloatBuffer();
		textureVertices[1].put(texData1).position(0);
			
		float[] texData2 = new float[] {
			maxX, maxY,
	        minX, maxY,
	        maxX, minY,
	        minX, minY,
		};
		textureVertices[2] = ByteBuffer.allocateDirect(texData2.length * 4).order(ByteOrder. nativeOrder()).asFloatBuffer();
		textureVertices[2].put(texData2).position(0);
		
		float[] texData3 = new float[] {
			maxY, minX,
			maxY, maxX,
			minY, minX,
			minY, maxX,
		};
		textureVertices[3] = ByteBuffer.allocateDirect(texData3.length * 4).order(ByteOrder. nativeOrder()).asFloatBuffer();
		textureVertices[3].put(texData3).position(0);
		
		for(int i = 0; i < 4; i++) {
			
		}
	}

}
