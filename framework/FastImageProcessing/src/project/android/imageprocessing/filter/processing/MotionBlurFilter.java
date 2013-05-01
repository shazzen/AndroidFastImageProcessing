package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.MultiPixelRenderer;

/**
 * Applies a directional motion blur to an image
 * blurSize: A multiplier for the blur size, ranging from 0.0 on up
 * blurAngle: The angular direction of the blur, in degrees.
 * @author Chris Batt
 */
public class MotionBlurFilter extends MultiPixelRenderer {
	
	private float blurSize;
	private float degree;
	
	public MotionBlurFilter(float blurSize, float degree) {
		this.blurSize = blurSize;
		this.degree = degree;
	}

	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"
				
		  		+"void main(){\n"
		  		+"   vec2 step = vec2("+UNIFORM_TEXELWIDTH+", "+UNIFORM_TEXELHEIGHT+");\n"
		  		+"   vec4 fragColour = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+") * 0.18;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + step) * 0.15;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - step) * 0.15;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + step * 2.0) * 0.12;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - step * 2.0) * 0.12;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + step * 3.0) * 0.09;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - step * 3.0) * 0.09;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + step * 4.0) * 0.05;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - step * 4.0) * 0.05;\n"
		  		+"   gl_FragColor = fragColour;\n"
		  		+"}\n";
	}
	
	@Override
	protected void handleSizeChange() {
		super.handleSizeChange();
		texelWidth = (float) (blurSize * Math.cos(degree * Math.PI / 180.0)) / (float)getWidth();
		texelHeight = (float) (blurSize * Math.sin(degree * Math.PI / 180.0)) / (float)getHeight();
	}
}
