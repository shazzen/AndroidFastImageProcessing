package project.android.imageprocessing.filter.effect;

import project.android.imageprocessing.filter.BasicFilter;

/**
 * Kuwahara image abstraction, drawn from the work of Kyprianidis, et. al. in their publication "Anisotropic Kuwahara Filtering on the GPU" within the GPU Pro collection. This produces an oil-painting-like image, but it is extremely computationally expensive, so it can take seconds to render a frame on an iPad 2. This might be best used for still images.
 * radius: In integer specifying the number of pixels out from the center pixel to test when applying the filter. A higher value creates a more abstracted image, but at the cost of much greater processing time.
 * This may not work on some devices.  Use {@link KuwaharaRadius3Filter} if it does not.
 * @author Chris Batt
 */
public class KuwaharaFilter extends BasicFilter {
	protected static final String UNIFORM_RADIUS = "u_Radius";
	
	private int radius;
	
	public KuwaharaFilter(int radius) {
		this.radius = radius;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				"precision highp float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"const int "+UNIFORM_RADIUS+" = "+radius+";\n"	
				+"const vec2 src_size = vec2 (1.0 / 768.0, 1.0 / 1024.0);\n"
				
		  		+"void main(){\n"
		  		+"  vec2 uv = "+VARYING_TEXCOORD+";\n"
		  		+"  float n = float(("+UNIFORM_RADIUS+" + 1) * ("+UNIFORM_RADIUS+" + 1));\n"
				+"  int i; int j;\n"
				+"  vec3 m0 = vec3(0.0); vec3 m1 = vec3(0.0); vec3 m2 = vec3(0.0); vec3 m3 = vec3(0.0);\n"
				+"  vec3 s0 = vec3(0.0); vec3 s1 = vec3(0.0); vec3 s2 = vec3(0.0); vec3 s3 = vec3(0.0);\n"
				+"  vec3 c;\n"
				
				+"	for (j = -"+UNIFORM_RADIUS+"; j <= 0; ++j)  {\n"
				+"		for (i = -"+UNIFORM_RADIUS+"; i <= 0; ++i)  {\n"
		        +"     		c = texture2D("+UNIFORM_TEXTURE0+", uv + vec2(i,j) * src_size).rgb;\n"
		        +"     		m0 += c;\n"
		        +"     		s0 += c * c;\n"
		        +" 		}\n"
		        +"	}\n"

			    +" 	for (j = -"+UNIFORM_RADIUS+"; j <= 0; ++j)  {\n"
			    +" 		for (i = 0; i <= "+UNIFORM_RADIUS+"; ++i)  {\n"
			    +" 			c = texture2D("+UNIFORM_TEXTURE0+", uv + vec2(i,j) * src_size).rgb;\n"
			    +" 			m1 += c;\n"
			    +" 			s1 += c * c;\n"
			    +" 		}\n"
			    +" 	}\n"

			    +" 	for (j = 0; j <= "+UNIFORM_RADIUS+"; ++j)  {\n"
			    +" 		for (i = 0; i <= "+UNIFORM_RADIUS+"; ++i)  {\n"
			    +" 			c = texture2D("+UNIFORM_TEXTURE0+", uv + vec2(i,j) * src_size).rgb;\n"
			    +" 			m2 += c;\n"
			    +" 			s2 += c * c;\n"
			    +" 		}\n"
			    +" 	}\n"

			    +" 	for (j = 0; j <= "+UNIFORM_RADIUS+"; ++j)  {\n"
			    +" 		for (i = -"+UNIFORM_RADIUS+"; i <= 0; ++i)  {\n"
			    +" 			c = texture2D("+UNIFORM_TEXTURE0+", uv + vec2(i,j) * src_size).rgb;\n"
			    +" 			m3 += c;\n"
			    +" 			s3 += c * c;\n"
			    +"     	}\n"
			    +" 	}\n"

			    +" 	float min_sigma2 = 1e+2;\n"
			    +" 	m0 /= n;\n"
			    +" 	s0 = abs(s0 / n - m0 * m0);\n"

			    +" 	float sigma2 = s0.r + s0.g + s0.b;\n"
			    +" 	if (sigma2 < min_sigma2) {\n"
			    +" 		min_sigma2 = sigma2;\n"
			    +" 		gl_FragColor = vec4(m0, 1.0);\n"
			    +" 	}\n"

			    +" 	m1 /= n;\n"
			    +" 	s1 = abs(s1 / n - m1 * m1);\n"

			    +" 	sigma2 = s1.r + s1.g + s1.b;\n"
			    +" 	if (sigma2 < min_sigma2) {\n"
			    +" 		min_sigma2 = sigma2;\n"
			    +" 		gl_FragColor = vec4(m1, 1.0);\n"
			    +" 	}\n"

			    +" 	m2 /= n;\n"
			    +" 	s2 = abs(s2 / n - m2 * m2);\n"

			    +" 	sigma2 = s2.r + s2.g + s2.b;\n"
			    +" 	if (sigma2 < min_sigma2) {\n"
			    +" 		min_sigma2 = sigma2;\n"
			    +" 		gl_FragColor = vec4(m2, 1.0);\n"
			    +" 	}\n"

			    +" 	m3 /= n;\n"
			    +" 	s3 = abs(s3 / n - m3 * m3);\n"

			    +" 	sigma2 = s3.r + s3.g + s3.b;\n"
			    +" 	if (sigma2 < min_sigma2) {\n"
			    +" 		min_sigma2 = sigma2;\n"
			    +" 		gl_FragColor = vec4(m3, 1.0);\n"
			    +" 	}\n"
		  		+"}\n";	
	}
}
