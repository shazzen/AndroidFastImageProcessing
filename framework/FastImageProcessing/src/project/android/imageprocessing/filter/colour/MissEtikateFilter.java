package project.android.imageprocessing.filter.colour;

import project.android.fastimageprocessing.R;
import android.content.Context;


/**
 * A photo filter based on a Photoshop action by Miss Etikate: http://miss-etikate.deviantart.com/art/Photoshop-Action-15-120151961.
 * @author Chris Batt
 */
public class MissEtikateFilter extends LookupFilter {
	
	public MissEtikateFilter(Context context) {
		super(context, R.drawable.lookup_miss_etikate);
	}

}
