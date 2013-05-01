package project.android.imageprocessing.filter.colour;

import project.android.imageprocessing.filter.BasicFilter;
import android.opengl.GLES20;

/**
 * A colour matrix filter implementation of BasicFilter.  
 * This filter takes in a 4x4 matrix that will be applied to each pixel in the input image. 
 * It does not take into account the alpha values which will always be the same as the input 
 * alpha values.  The percentage of the resulting RGB value that should be factor into the output texture can
 * be controlled by the intensity. 
 * @author Chris Batt
 */
public class ColourMatrixFilter extends BasicFilter {
	private static final String UNIFORM_COLORMATRIX = "u_ColorMatrix";
	private static final String UNIFORM_INTENSITY = "u_Intensity";
	private float[] colorMatrix;
	private float intensity;
	private int colorMatrixHandle;
	private int intensityHandle;
	
	/**
	 * Creates a ColourMatrixFilter with the given matrix as the multiplicand and the given intensity as 
	 * the percentage of the colorMatrix product that should be factor into the output.
	 * @param colorMatrix
	 * The 4x4 matrix that will be multiplied by a rgba vector of each pixel.
	 * @param intensity
	 * The percentage of the of the colorMatrix and pixel product that should factor into the output.
	 */
	public ColourMatrixFilter(float[] colorMatrix, float intensity) {
		this.colorMatrix = colorMatrix;
		if(intensity < 0) {
			intensity = 0;
		}
		if(intensity > 1) {
			intensity = 1;
		}
		this.intensity = intensity;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_INTENSITY+";\n"
				+"uniform mat4 "+UNIFORM_COLORMATRIX+";\n"
				
		  		+"void main(){\n"
		  		+"   vec4 color = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+"   vec4 matrixResult = vec4(color.rgb, 1.0) * "+UNIFORM_COLORMATRIX+";\n"
		  		+"   vec4 colorResult = "+UNIFORM_INTENSITY+" * matrixResult + (1.0 - "+UNIFORM_INTENSITY+") * color;\n"
		  		+"   gl_FragColor = vec4(colorResult.rgb, color.a);\n"
		  		+"}\n";
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		colorMatrixHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_COLORMATRIX);
		intensityHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_INTENSITY);
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniformMatrix4fv(colorMatrixHandle, 1, false, colorMatrix, 0);
		GLES20.glUniform1f(intensityHandle, intensity);
	}
}
