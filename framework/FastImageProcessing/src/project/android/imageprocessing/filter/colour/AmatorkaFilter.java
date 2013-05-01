package project.android.imageprocessing.filter.colour;

import project.android.fastimageprocessing.R;
import android.content.Context;

/**
 *  A photo filter based on a Photoshop action by Amatorka: http://amatorka.deviantart.com/art/Amatorka-Action-2-121069631 . 
 * @author Chris Batt
 */
public class AmatorkaFilter extends LookupFilter {
	public AmatorkaFilter(Context context) {
		super(context, R.drawable.lookup_amatorka);
	}
}
