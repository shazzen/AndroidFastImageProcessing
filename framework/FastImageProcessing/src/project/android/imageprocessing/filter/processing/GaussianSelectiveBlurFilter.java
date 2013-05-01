package project.android.imageprocessing.filter.processing;

import android.graphics.PointF;

/**
 * A Gaussian blur that preserves focus within a circular region
 * blurSize: A multiplier for the size of the blur, ranging from 0.0 on up
 * excludeCircleRadius: The radius of the circular area being excluded from the blur
 * excludeCirclePoint: The center of the circular area being excluded from the blur
 * excludeBlurSize: The size of the area between the blurred portion and the clear circle
 * aspectRatio: The aspect ratio of the image, used to adjust the circularity of the in-focus region.
 * @author Chris Batt
 */
public class GaussianSelectiveBlurFilter extends GaussianBlurPositionFilter {
		
	public GaussianSelectiveBlurFilter(float blurSize, float aspectRatio, PointF excludedCirclePoint, float excludedCircleRadius, float excludedBlurSize) {
		super(blurSize, aspectRatio, excludedCirclePoint, excludedCircleRadius, excludedBlurSize);
	}

	@Override
	protected String getFragmentShader() {
		return
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n"
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_BLUR_SIZE+";\n"
				+"uniform float "+UNIFORM_ASPECT_RATIO+";\n"
				+"uniform vec2 "+UNIFORM_EXCLUDE_CIRCLE_POINT+";\n"
				+"uniform float "+UNIFORM_EXCLUDE_CIRCLE_RADIUS+";\n"
						
				
		  		+"void main(){\n"
				+"   vec4 sharpImageColor = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+");\n"
				+"   vec4 blurredImageColor = texture2D("+UNIFORM_TEXTUREBASE+1+", "+VARYING_TEXCOORD+");\n"
				+"   vec2 texCoordAfterAspect = vec2("+VARYING_TEXCOORD+".x, "+VARYING_TEXCOORD+".y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+");\n"
		  		+"   float distanceFromCenter = distance("+UNIFORM_EXCLUDE_CIRCLE_POINT+", texCoordAfterAspect);\n"
				+"   gl_FragColor = mix(sharpImageColor, blurredImageColor, smoothstep("+UNIFORM_EXCLUDE_CIRCLE_RADIUS+" - "+UNIFORM_BLUR_SIZE+", "+UNIFORM_EXCLUDE_CIRCLE_RADIUS+", distanceFromCenter));\n"
		  		+"}\n";
	}
}
