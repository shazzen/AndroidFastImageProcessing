package project.android.imageprocessing.filter.colour;

import project.android.fastimageprocessing.R;
import project.android.imageprocessing.filter.GroupFilter;
import project.android.imageprocessing.filter.blend.AlphaBlendFilter;
import project.android.imageprocessing.filter.processing.GaussianBlurFilter;
import android.content.Context;

/**
 * Another lookup-based color remapping filter.
 * @author Chris Batt
 */
public class SoftEleganceFilter extends GroupFilter {

	public SoftEleganceFilter(Context context) {
		LookupFilter img1 = new LookupFilter(context, R.drawable.lookup_soft_elegance_1);
		GaussianBlurFilter gaussianBlur = new GaussianBlurFilter(9.7f);
		AlphaBlendFilter alphaBlend = new AlphaBlendFilter(0.14f);
		LookupFilter img2 = new LookupFilter(context, R.drawable.lookup_soft_elegance_2);
		img1.addTarget(gaussianBlur);
		img1.addTarget(alphaBlend);
		gaussianBlur.addTarget(alphaBlend);
		alphaBlend.registerFilterLocation(img1);
		alphaBlend.registerFilterLocation(gaussianBlur);
		alphaBlend.addTarget(img2);
		img2.addTarget(this);
		
		registerInitialFilter(img1);
		registerFilter(gaussianBlur);
		registerFilter(alphaBlend);
		registerTerminalFilter(img2);
	}
}
