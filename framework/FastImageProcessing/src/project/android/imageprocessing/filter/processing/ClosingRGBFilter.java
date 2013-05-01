package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.GroupFilter;

/**
 * This is the same as the {@link ClosingFilter}, except that this acts on all color channels, not just the red channel.
 * @author Chris Batt
 */
public class ClosingRGBFilter extends GroupFilter {
	public ClosingRGBFilter(int radius) {
		DilationRGBFilter dilation = new DilationRGBFilter(radius);
		ErosionRGBFilter erosion = new ErosionRGBFilter(radius);
		dilation.addTarget(erosion);
		erosion.addTarget(this);
		
		registerInitialFilter(dilation);
		registerTerminalFilter(erosion);
	}
}
