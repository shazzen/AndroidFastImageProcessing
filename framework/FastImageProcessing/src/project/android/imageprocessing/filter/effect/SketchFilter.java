package project.android.imageprocessing.filter.effect;

import project.android.imageprocessing.filter.processing.SobelEdgeDetectionFilter;

/**
 * Converts video to look like a sketch. This is just the Sobel edge detection filter with the colors inverted
 * @author Chris Batt
 */
public class SketchFilter extends SobelEdgeDetectionFilter {
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"
						
				
		  		+"void main(){\n"
		  		+"   vec2 up = vec2(0.0, "+UNIFORM_TEXELHEIGHT+");\n"
		  		+"   vec2 right = vec2("+UNIFORM_TEXELWIDTH+", 0.0);\n"
		  		+"   float bottomLeftIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - up - right).r;\n"
		  		+"   float topRightIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + up + right).r;\n"
			    +"   float topLeftIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + up - right).r;\n"
			    +"   float bottomRightIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - up + right).r;\n"
			    +"   float leftIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - right).r;\n"
			    +"   float rightIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + right).r;\n"
			    +"   float bottomIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - up).r;\n"
			    +"   float topIntensity = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + up).r;\n"
			    +"   float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n"
			    +"   float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n"
			     
			    +"   float mag = 1.0 - length(vec2(h, v));\n"
			     
			    +"   gl_FragColor = vec4(vec3(mag), 1.0);\n"
		  		+"}\n";
	}
}
