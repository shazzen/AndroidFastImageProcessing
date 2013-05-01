package project.android.imageprocessing.filter.colour;

import project.android.imageprocessing.filter.BasicFilter;
import android.opengl.GLES20;

/**
 * A gamma adjustment filter extension of BasicFilter.
 * This filter adjusts the gamma value of the input image.
 * Values for gamma should be in [0, 3] for normal use; however, all values are excepted.
 * This filter does not adjust alpha values.
 * @author Chris Batt
 */
public class GammaFilter extends BasicFilter {
	private static final String UNIFORM_GAMMA = "u_Gamma";
	private float gamma;
	private int gammaHandle;
	
	/**
	 * Creates a ImageGammaFilter with the given gamma adjustment value.
	 * @param gamma
	 * The gamma adjustment value.
	 */
	public GammaFilter(float gamma) {
		this.gamma = gamma;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_GAMMA+";\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
				+"   gl_FragColor = vec4(pow(color.rgb, vec3("+UNIFORM_GAMMA+")), color.a);\n"
				+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		gammaHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_GAMMA);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(gammaHandle, gamma);
	}
}
