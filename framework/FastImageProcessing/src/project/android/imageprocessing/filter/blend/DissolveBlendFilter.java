package project.android.imageprocessing.filter.blend;

import project.android.imageprocessing.filter.MultiInputFilter;
import android.opengl.GLES20;

/**
 * Applies a dissolve blend of two images
 * mix: The degree with which the second image overrides the first (0.0 - 1.0)
 * @author Chris Batt
 */
public class DissolveBlendFilter extends MultiInputFilter {
	private static final String UNIFORM_MIX_PERCENT = "u_MixPercent";
	
	private float mixPercent;
	private int mixPercentHandle;

	public DissolveBlendFilter(float mixPercent) {
		super(2);
		this.mixPercent = mixPercent;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_MIX_PERCENT+";\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color1 = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+"   vec4 color2 = texture2D("+UNIFORM_TEXTUREBASE+1+","+VARYING_TEXCOORD+");\n"
		  		+"   gl_FragColor = mix(color1, color2, "+UNIFORM_MIX_PERCENT+");\n"
		  		+"}\n";	
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		mixPercentHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_MIX_PERCENT);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(mixPercentHandle, mixPercent);
	}
}
