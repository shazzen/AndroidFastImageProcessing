package project.android.imageprocessing.filter.effect;

import android.graphics.PointF;

/**
 *Simulates the refraction through a solid glass sphere
 *center: The center about which to apply the distortion
 *radius: The radius of the distortion, ranging from 0.0 to 1.0
 *refractiveIndex: The index of refraction for the sphere
 * @author Chris Batt
 */
public class SphereRefractionFilter extends BulgeDistortionFilter {
	
	public SphereRefractionFilter(PointF center, float radius, float refractiveIndex, float aspectRatio) {
		super(center, radius, refractiveIndex, aspectRatio);
	}

	@Override
	protected String getFragmentShader() {
		return 
				 "precision highp float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform vec2 "+UNIFORM_CENTER+";\n"
				+"uniform float "+UNIFORM_RADIUS+";\n"
				+"uniform float "+UNIFORM_DISTORTION_AMOUNT+";\n"
				+"uniform float "+UNIFORM_ASPECT_RATIO+";\n"
				
		  		+"void main(){\n"
		  		+"	vec2 textureCoordinateToUse = vec2("+VARYING_TEXCOORD+".x, ("+VARYING_TEXCOORD+".y * "+UNIFORM_ASPECT_RATIO+" + 0.5 - 0.5 * "+UNIFORM_ASPECT_RATIO+"));\n"
		  		+"	float distanceFromCenter = distance("+UNIFORM_CENTER+", textureCoordinateToUse);\n"
			    +" 	float checkForPresenceWithinSphere = step(distanceFromCenter, "+UNIFORM_RADIUS+");\n"
			    +" 	distanceFromCenter = distanceFromCenter / "+UNIFORM_RADIUS+";\n"
			    +" 	float normalizedDepth = "+UNIFORM_RADIUS+" * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\n"
			    +" 	vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - "+UNIFORM_CENTER+", normalizedDepth));\n"
			    +" 	vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, "+UNIFORM_DISTORTION_AMOUNT+");\n"
			    +" 	gl_FragColor = texture2D("+UNIFORM_TEXTURE0+", (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;\n"
		  		+"}\n";
	}
}
