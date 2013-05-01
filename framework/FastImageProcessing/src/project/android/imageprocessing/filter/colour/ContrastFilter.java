package project.android.imageprocessing.filter.colour;

import project.android.imageprocessing.filter.BasicFilter;
import android.opengl.GLES20;

/**
 * A contrast filter extension of BasicFilter.
 * This filter increases or decrease the contrast of the image based on a given value.
 * For normal use of this filter, the contrast value should be in [0, 4].
 * @author Chris Batt
 */
public class ContrastFilter extends BasicFilter {
	static final String UNIFORM_CONTRAST = "u_Contrast";
	private float contrast;
	private int contrastHandle;
	
	/**
	 * Creates an ImageContrastFilter that will adjust contrast by a given amount.
	 * @param contrast
	 * The amount of contrast.
	 */
	public ContrastFilter(float contrast) {
		this.contrast = contrast;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_CONTRAST+";\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"	
				+"   gl_FragColor = vec4(((color.rgb - vec3(0.5)) * "+UNIFORM_CONTRAST+" + vec3(0.5)), color.a);\n"
		  		+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		contrastHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_CONTRAST);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(contrastHandle, contrast);
	}
}
