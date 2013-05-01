package project.android.imageprocessing.filter.processing;

import project.android.imageprocessing.filter.MultiPixelRenderer;

/**
 * Takes the median value of the three color components, over a 3x3 area
 * @author Chris Batt
 */
public class MedianFilter extends MultiPixelRenderer {
	
	@Override
	protected String getFragmentShader() {
		return
				"precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				+"uniform float "+UNIFORM_TEXELWIDTH+";\n"
				+"uniform float "+UNIFORM_TEXELHEIGHT+";\n"	
				
				+"#define s2(a, b)				temp = a; a = min(a, b); b = max(temp, b);\n"
				+"#define mn3(a, b, c)			s2(a, b); s2(a, c);\n"
				+"#define mx3(a, b, c)			s2(b, c); s2(a, c);\n"
				+"#define mnmx3(a, b, c)			mx3(a, b, c); s2(a, b);\n"
				+"#define mnmx4(a, b, c, d)		s2(a, b); s2(c, d); s2(a, c); s2(b, d);\n"
				+"#define mnmx5(a, b, c, d, e)	s2(a, b); s2(c, d); mn3(a, c, e); mx3(b, d, e);\n"
				+"#define mnmx6(a, b, c, d, e, f) s2(a, d); s2(b, e); s2(c, f); mn3(a, b, c); mx3(d, e, f);\n"
		
				+"void main() {\n"
				+"  vec3 v[6];\n"
				+"  vec2 right = vec2(0.0, "+UNIFORM_TEXELWIDTH+");\n"
				+"  vec2 down = vec2(0.0, "+UNIFORM_TEXELHEIGHT+");\n"
				+"  v[0] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - down - right).rgb;\n"
				+"  v[1] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - down).rgb;\n"
				+"  v[2] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - down + right).rgb;\n"
				+"  v[3] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" - right).rgb;\n"
				+"  v[4] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+").rgb;\n"
				+"  v[5] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + right).rgb;\n"
				+"  vec3 temp;\n"
				+"  mnmx6(v[0], v[1], v[2], v[3], v[4], v[5]);\n"
				+"  v[5] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + down - right).rgb;\n"
				+"  mnmx5(v[1], v[2], v[3], v[4], v[5]);\n"
				+"  v[5] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + down).rgb;\n"
				+"  mnmx4(v[2], v[3], v[4], v[5]);\n"
				+"  v[5] = texture2D("+UNIFORM_TEXTURE0+", "+VARYING_TEXCOORD+" + down + right).rgb;\n"
				+"  mnmx3(v[3], v[4], v[5]);\n"
				+"  gl_FragColor = vec4(v[4], 1.0);\n"
				+"}\n";
	}
}
