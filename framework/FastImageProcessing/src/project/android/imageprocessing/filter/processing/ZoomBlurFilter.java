package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.BasicFilter;
import android.graphics.PointF;
import android.opengl.GLES20;

/**
 * Applies a motion blur around a point
 * blurSize: A multiplier for the blur size, ranging from 0.0 on up
 * blurCenter: The normalized center of the blur.
 * @author Chris Batt
 */
public class ZoomBlurFilter extends BasicFilter {
	private static final String UNIFORM_BLUR_SIZE = "u_BlurSize";
	private static final String UNIFORM_BLUR_LOCATION = "u_BlurLocation";
	
	float blurSize;
	PointF blurLocation;
	private int blurSizeHandle;
	private int blurLocationHandle;
	
	public ZoomBlurFilter(float blurSize, PointF blurLocation) {
		this.blurSize = blurSize;
		this.blurLocation = blurLocation;
	}
	
	@Override
	protected String getFragmentShader() {
		return
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n"
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_BLUR_SIZE+";\n"
				+"uniform vec2 "+UNIFORM_BLUR_LOCATION+";\n"
						
				
		  		+"void main(){\n"
		  		+"   vec2 samplingOffset = 1.0/100.0 * ("+UNIFORM_BLUR_LOCATION+" - "+VARYING_TEXCOORD+") * "+UNIFORM_BLUR_SIZE+";\n"
		  		+"   vec4 fragColour = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+") * 0.18;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" +  samplingOffset) * 0.15;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" -  samplingOffset) * 0.15;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" +  (2.0 * samplingOffset)) * 0.12;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" -  (2.0 * samplingOffset)) * 0.12;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" +  (3.0 * samplingOffset)) * 0.09;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" -  (3.0 * samplingOffset)) * 0.09;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" +  (4.0 * samplingOffset)) * 0.05;\n"
		  		+"   fragColour += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" -  (4.0 * samplingOffset)) * 0.05;\n"
		  		+"   gl_FragColor = fragColour;\n"
		  		+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		blurSizeHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_BLUR_SIZE);
		blurLocationHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_BLUR_LOCATION);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(blurSizeHandle, blurSize);
		GLES20.glUniform2f(blurLocationHandle, blurLocation.x, blurLocation.y);
	}
}
