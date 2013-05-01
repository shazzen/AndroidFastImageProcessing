package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.GroupFilter;

/**
 * This performs an erosion on the red channel of an image, followed by a dilation of the same radius. The radius is set on initialization, with a range of 1+ pixels. This filters out smaller bright regions.
 * @author Chris Batt
 */
public class OpeningFilter extends GroupFilter {
	public OpeningFilter(int radius) {
		ErosionFilter erosion = new ErosionFilter(radius);
		DilationFilter dilation = new DilationFilter(radius);
		erosion.addTarget(dilation);
		dilation.addTarget(this);
		
		registerInitialFilter(erosion);
		registerTerminalFilter(dilation);
	}
}
