package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.MultiPixelRenderer;
import android.opengl.GLES20;

/**
 * Applies a sharpen filter to the image
 * @author Chris Batt
 */
public class SharpenFilter extends MultiPixelRenderer {
	private static final String UNIFORM_SHARPEN_AMOUNT = "u_SharpenAmount";
	
	private float sharpenAmount;
	private int sharpenAmountHandle;
	
	public SharpenFilter(float sharpenAmount) {
		this.sharpenAmount = sharpenAmount;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"
				+"uniform float "+UNIFORM_SHARPEN_AMOUNT+";\n"
						
				
		  		+"void main(){\n"
				+"   vec2 singleStepOffset = vec2("+UNIFORM_TEXELWIDTH+", "+UNIFORM_TEXELHEIGHT+");\n"
				+"   vec4 sum = vec4(0,0,0,0);\n"
				+"   vec2 up = vec2(0.0, "+UNIFORM_TEXELHEIGHT+");\n"
				+"   vec2 left = vec2("+UNIFORM_TEXELWIDTH+", 0.0);\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+") * (1.0 + 4.0 * "+UNIFORM_SHARPEN_AMOUNT+");\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + up) * -"+UNIFORM_SHARPEN_AMOUNT+";\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - up) * -"+UNIFORM_SHARPEN_AMOUNT+";\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + left) * -"+UNIFORM_SHARPEN_AMOUNT+";\n"
				+"   sum += texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - left) * -"+UNIFORM_SHARPEN_AMOUNT+";\n"
		  		+"   gl_FragColor = sum;\n"
		  		+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		sharpenAmountHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_SHARPEN_AMOUNT);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(sharpenAmountHandle, sharpenAmount);
	}
}
