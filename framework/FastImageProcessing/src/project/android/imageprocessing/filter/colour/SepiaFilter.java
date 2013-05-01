package project.android.imageprocessing.filter.colour;


/**
 * Simple sepia tone filter
 * intensity: The degree to which the sepia tone replaces the normal image color (0.0 - 1.0)
 * @author Chris Batt
 */
public class SepiaFilter extends ColourMatrixFilter {

	public SepiaFilter() {
		super(new float[] {	0.3588f, 0.7044f, 0.1368f, 0.0f,
		        			0.2990f, 0.5870f, 0.1140f, 0.0f,
		        			0.2392f, 0.4696f, 0.0912f ,0.0f,
		        			0,0,0,1.0f}, 1.0f);
	}

}
