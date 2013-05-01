package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.MultiPixelRenderer;

/**
 * Currently only used as part of the CannyEdgeDetectionFilter
 * @author Chris Batt
 */
public class WeakPixelInclusionFilter extends MultiPixelRenderer {
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
		  		+"   float centerIntensity = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+").r;\n"
				
		  		+"   float pixelIntensitySum = bottomLeftIntensity + topRightIntensity + topLeftIntensity + bottomRightIntensity + leftIntensity + rightIntensity + bottomIntensity + topIntensity + centerIntensity;\n"
				+"   float sumTest = step(1.5, pixelIntensitySum);\n"
				+"   float pixelTest = step(0.01, centerIntensity);\n"
				+"   gl_FragColor = vec4(vec3(sumTest * pixelTest), 1.0);\n"
				+"}\n";
	}
}
