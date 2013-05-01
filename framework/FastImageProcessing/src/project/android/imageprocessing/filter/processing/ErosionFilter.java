package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.TwoPassMultiPixelFilter;

/**
 * This performs an image erosion operation, where the minimum intensity of the red channel in a rectangular neighborhood is used for the intensity of this pixel. The radius of the rectangular area to sample over is specified on initialization, with a range of 1+ pixels. This is intended for use with grayscale images, and it expands dark regions.
 * @author Chris Batt
 */
public class ErosionFilter extends TwoPassMultiPixelFilter  {
	private int erosionRadius;
	
	public ErosionFilter(int erosionRadius) {
		this.erosionRadius = erosionRadius;
	}
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"
				+"const int dilationSize = "+(erosionRadius*2+1)+";\n"
				+"const int dilationRadius = "+erosionRadius+";\n"
				
		  		+"void main(){\n"
		  		+"   vec2 step = vec2("+UNIFORM_TEXELWIDTH+", "+UNIFORM_TEXELHEIGHT+");\n"
		  		+"   float stepIntensity[dilationSize];\n"
		  		+"   for(int i = 0; i < dilationSize; i++) {\n"
		  		+"     stepIntensity[i] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + step * float(i - dilationRadius)).r;\n"
		  		+"   }\n"
		  		
		  		+"   float minValue = 1.0;\n"
		  		+"   for(int i = 0; i < dilationSize; i++) {\n"
		  		+"     minValue = min(minValue, stepIntensity[i]);\n"
		  		+"   }\n"
		  		+"   gl_FragColor = vec4(vec3(minValue), 1.0);\n"
		  		+"}\n";
	}
}
