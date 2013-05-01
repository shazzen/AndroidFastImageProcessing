package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.BasicFilter;
import android.opengl.GLES20;

/**
 * Applies an abitrary transform to the image.  Allows for the image to be center or achored top left while this transform is being applied.
 * @author Chris Batt
 */
public class TransformFilter extends BasicFilter {
	private static final String UNIFORM_TRANSFORM_MATRIX = "u_TransformMatrix";
	private static final String UNIFORM_ORTHO_MATRIX = "u_OrthoMatrix";
	
	private int transformMatrixHandle;
	private int orthoMatrixHandle;
	private float[] transformMatrix;
	private float[] orthoMatrix;
	private boolean ignoreAspect;
	private boolean anchorTopLeft;
	
	public TransformFilter(float[] transformMatrix, boolean ignoreAspect, boolean anchorTopLeft) {
		this.transformMatrix = transformMatrix;
		this.ignoreAspect = ignoreAspect;
		this.anchorTopLeft = anchorTopLeft;
	}
	
	@Override
	protected String getVertexShader() {
		return 
				  "attribute vec4 "+ATTRIBUTE_POSITION+";\n"
				+ "attribute vec2 "+ATTRIBUTE_TEXCOORD+";\n"
				+"uniform mat4 "+UNIFORM_ORTHO_MATRIX+";\n"
				+"uniform mat4 "+UNIFORM_TRANSFORM_MATRIX+";\n"	
				+ "varying vec2 "+VARYING_TEXCOORD+";\n"	
				  
				+ "void main() {\n"	
				+ "  "+VARYING_TEXCOORD+" = "+ATTRIBUTE_TEXCOORD+";\n"
				+ "   gl_Position = "+UNIFORM_TRANSFORM_MATRIX+" * vec4("+ATTRIBUTE_POSITION+".xyz, 1.0) * "+UNIFORM_ORTHO_MATRIX+";\n"		                                            			 
				+ "}\n";
	}
	
	@Override
	protected void handleSizeChange() {
		super.handleSizeChange();
		if(ignoreAspect) {
			if(anchorTopLeft) {
				setRenderVertices(new float[]{
			        0.0f, 0.0f,
			        1.0f, 0.0f,
			        0.0f,  1.0f,
			        1.0f,  1.0f,
			    });
			}
			loadOrthoMatrix(-1, 1, -1, 1, -1, 1);
		} else {
			float normalizedHeight = (float)getHeight() / (float)getWidth();
			if(anchorTopLeft) {
				setRenderVertices(new float[] {
					0.0f, 0.0f,
			        1.0f, 0.0f,
			        0.0f,  normalizedHeight,
			        1.0f,  normalizedHeight,
				});
			} else {
				setRenderVertices(new float[] {
					-1.0f, -normalizedHeight,
			        1.0f, -normalizedHeight,
			        -1.0f,  normalizedHeight,
			        1.0f,  normalizedHeight,
				});
			}
			loadOrthoMatrix(-1, 1, (-1 * (float)getHeight()/(float)getWidth()), (1 * (float)getHeight()/(float)getWidth()), -1, 1);
		}
	}
	
	
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		transformMatrixHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TRANSFORM_MATRIX);
		orthoMatrixHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_ORTHO_MATRIX);
	}
	
	private void loadOrthoMatrix(float left, float right, float bottom, float top, float near, float far) {
		float r_l = right - left;
		float t_b = top - bottom;
		float f_n = far - near;
		float tx = - (right + left) / (right - left);
	    float ty = - (top + bottom) / (top - bottom);
	    float tz = - (far + near) / (far - near);
	    
		float scale = 2.0f;
		if (anchorTopLeft) {
			scale = 4.0f;
			tx=-1.0f;
			ty=-1.0f;
		}

		orthoMatrix = new float[] {
			scale / r_l, 0f, 0f, tx,
			0f, scale / t_b, 0f, ty,
			0f, 0f, scale / f_n, tz,
			0f, 0f, 0f, 1f
		};
	} 
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniformMatrix4fv(transformMatrixHandle, 1, false, transformMatrix, 0);
		GLES20.glUniformMatrix4fv(orthoMatrixHandle, 1, false, orthoMatrix, 0);
	}
}
