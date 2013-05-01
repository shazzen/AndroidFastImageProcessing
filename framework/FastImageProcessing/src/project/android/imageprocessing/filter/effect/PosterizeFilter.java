package project.android.imageprocessing.filter.effect;

import project.android.imageprocessing.filter.BasicFilter;
import android.opengl.GLES20;

/**
 * This reduces the color dynamic range into the number of steps specified, leading to a cartoon-like simple shading of the image.
 * colorLevels: The number of color levels to reduce the image space to. This ranges from 1 to 256
 * @author Chris Batt
 */
public class PosterizeFilter extends BasicFilter {
	private static final String UNIFORM_QUANTIZATION = "u_Quantization";
	
	private int quantizationLevelsHandle;
	private float quantizationLevels;
	
	public PosterizeFilter(float quantizationLevels) {
		this.quantizationLevels = quantizationLevels;
	}
		
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_QUANTIZATION+";\n"
						
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+");\n"
			    +"   vec3 posterizedImageColor = floor((color.rgb * "+UNIFORM_QUANTIZATION+") + 0.5) / "+UNIFORM_QUANTIZATION+";\n"
			    +"   gl_FragColor = vec4(posterizedImageColor, color.a);\n"
		  		+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		quantizationLevelsHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_QUANTIZATION);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform1f(quantizationLevelsHandle, quantizationLevels);
	}
}
