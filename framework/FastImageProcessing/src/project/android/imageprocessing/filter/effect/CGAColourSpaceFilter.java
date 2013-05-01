package project.android.imageprocessing.filter.effect;

import project.android.imageprocessing.filter.BasicFilter;

/**
 * Simulates the colorspace of a CGA monitor
 * @author Chris Batt
 */
public class CGAColourSpaceFilter extends BasicFilter {

	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n"  
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				
				+"void main() {\n"
				+"	highp vec2 sampleDivisor = vec2(1.0 / 200.0, 1.0 / 320.0);\n"			    
				+"	highp vec2 samplePos = "+VARYING_TEXCOORD+" - mod("+VARYING_TEXCOORD+", sampleDivisor);\n"
			    +"	highp vec4 color = texture2D("+UNIFORM_TEXTURE0+", samplePos );\n"
			    +"	mediump vec4 colorCyan = vec4(85.0 / 255.0, 1.0, 1.0, 1.0);\n"
			    +"	mediump vec4 colorMagenta = vec4(1.0, 85.0 / 255.0, 1.0, 1.0);\n"
			    +"	mediump vec4 colorWhite = vec4(1.0, 1.0, 1.0, 1.0);\n"
			    +"	mediump vec4 colorBlack = vec4(0.0, 0.0, 0.0, 1.0);\n"
			    
				+"  mediump vec4 endColor;\n"
				+"  highp float blackDistance = distance(color, colorBlack);\n"
				+"  highp float whiteDistance = distance(color, colorWhite);\n"
				+"  highp float magentaDistance = distance(color, colorMagenta);\n"
				+"  highp float cyanDistance = distance(color, colorCyan);\n"
				    
				+"  mediump vec4 finalColor;\n"
				    
				+"  highp float colorDistance = min(magentaDistance, cyanDistance);\n"
				+"	colorDistance = min(colorDistance, whiteDistance);\n"
				+"  colorDistance = min(colorDistance, blackDistance); \n"
				    
				+"  if (colorDistance == blackDistance) {\n"
				+"      finalColor = colorBlack;\n"
				+"  } else if (colorDistance == whiteDistance) {\n"
				+"      finalColor = colorWhite;\n"
				+"  } else if (colorDistance == cyanDistance) {\n"
				+"      finalColor = colorCyan;\n"
				+"  } else {\n"
				+"      finalColor = colorMagenta;\n"
				+"  }\n"
		  		+"	gl_FragColor = finalColor;\n"
		  		+"}\n";
				
}
}
