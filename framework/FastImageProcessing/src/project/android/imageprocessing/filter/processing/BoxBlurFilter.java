package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.TwoPassMultiPixelFilter;

/**
 * A hardware-accelerated 9-hit box blur of an image
 * @author Chris Batt
 */
public class BoxBlurFilter extends TwoPassMultiPixelFilter {
	@Override
	protected String getFragmentShader() {
		return 
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"
						
				
		  		+"void main(){\n"
				+"   vec2 firstOffset = vec2(1.5 * "+UNIFORM_TEXELWIDTH+", 1.5 * "+UNIFORM_TEXELHEIGHT+");\n"
				+"   vec2 secondOffset = vec2(3.5 * "+UNIFORM_TEXELWIDTH+", 3.5 * "+UNIFORM_TEXELHEIGHT+");\n"
				+"   vec4 sum = vec4(0,0,0,0);\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+") * 0.2;"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - firstOffset) * 0.2;"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + firstOffset) * 0.2;"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - secondOffset) * 0.2;"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + secondOffset) * 0.2;"
		  		+"   gl_FragColor = sum;\n"
		  		+"}\n";
	}
}
