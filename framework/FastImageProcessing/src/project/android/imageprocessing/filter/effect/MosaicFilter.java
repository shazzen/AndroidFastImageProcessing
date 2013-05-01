package project.android.imageprocessing.filter.effect;

import project.android.imageprocessing.filter.colour.LookupFilter;
import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;

/**
 * This filter takes an input tileset, the tiles must ascend in luminance. It looks at the input image and replaces each display tile with an input tile according to the luminance of that tile. The idea was to replicate the ASCII video filters seen in other apps, but the tileset can be anything.
 * inputTileSize: the size of the tiles in the input image
 * numTiles: the number of tiles in the input image (must be square?)
 * displayTileSize: the size of the tiles that will be displayed in the mosaic output
 * colorOn: whether or not to use the input colour or the input tile colour.
 * @author Chris Batt
 */
public class MosaicFilter extends LookupFilter {
	protected static final String UNIFORM_INPUT_SIZE = "u_InputSize";
	protected static final String UNIFORM_DISPLAY_SIZE = "u_DisplaySize";
	protected static final String UNIFORM_NUM_TILES = "u_NumTiles";
	protected static final String UNIFORM_COLOR = "u_Color";
	
	private int inputTileSizeHandle;
	private int displayTileSizeHandle;
	private int numOfTilesHandle;
	private int colorHandle;
	private PointF inputTileSize;
	private PointF displayTileSize;
	private int numOfTiles;
	private boolean color;
	
	public MosaicFilter(Context context, int id, PointF inputTileSize, PointF displayTileSize, int numOfTiles, boolean color) {
		super(context, id);
		this.inputTileSize = inputTileSize;
		this.displayTileSize = displayTileSize;
		this.numOfTiles = numOfTiles;
		this.color = color;
	}
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"
				+"uniform vec2 "+UNIFORM_INPUT_SIZE+";\n"	
				+"uniform vec2 "+UNIFORM_DISPLAY_SIZE+";\n"	
				+"uniform int "+UNIFORM_NUM_TILES+";\n"	
				+"uniform int "+UNIFORM_COLOR+";\n"	
				
		  		+"void main(){\n"
		  		+"	vec2 xy = "+VARYING_TEXCOORD+";\n"	
		  		+" 	xy = xy - mod(xy, "+UNIFORM_DISPLAY_SIZE+");\n"	
	  		    +" 	vec4 lumcoeff = vec4(0.299,0.587,0.114,0.0);\n"	
	  		    +" 	vec4 inputColor = texture2D("+UNIFORM_TEXTURE0+", xy);\n"	
	  		    +" 	float lum = dot(inputColor,lumcoeff);\n"	
	  		    +" 	lum = 1.0 - lum;\n"	
	  		    +" 	float stepsize = 1.0 / float("+UNIFORM_NUM_TILES+");\n"	
	  		    +" 	float lumStep = (lum - mod(lum, stepsize)) / stepsize;\n"	
	  		    +" 	float rowStep = 1.0 / "+UNIFORM_INPUT_SIZE+".x;\n"	
	  		    +" 	float x = mod(lumStep, rowStep);\n"	
	  		    +" 	float y = floor(lumStep / rowStep);\n"	
	  		    +" 	vec2 startCoord = vec2(float(x) *  "+UNIFORM_INPUT_SIZE+".x, float(y) * "+UNIFORM_INPUT_SIZE+".y);\n"	
	  		    +" 	vec2 finalCoord = startCoord + (("+VARYING_TEXCOORD+" - xy) * ("+UNIFORM_INPUT_SIZE+" / "+UNIFORM_DISPLAY_SIZE+"));\n"	
	  		    +" 	vec4 color = texture2D("+UNIFORM_TEXTUREBASE+1+", finalCoord);\n"	   
	  		    +" 	if ("+UNIFORM_COLOR+" == 1) {\n"	
	  		    +" 		color = color * inputColor;\n"	
	  		    +"	}\n"	
	  		    +"	gl_FragColor = color;\n"	 
		  		+ "}\n";		
	}
	
	@Override
	protected void initShaderHandles() {
		super.initShaderHandles();
		inputTileSizeHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_INPUT_SIZE);
		displayTileSizeHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_DISPLAY_SIZE);
		numOfTilesHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_NUM_TILES);
		colorHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_COLOR);
	}
	
	@Override
	protected void passShaderValues() {
		super.passShaderValues();
		GLES20.glUniform2f(inputTileSizeHandle, inputTileSize.x, inputTileSize.y);
		GLES20.glUniform2f(displayTileSizeHandle, displayTileSize.x, displayTileSize.y);
		GLES20.glUniform1i(numOfTilesHandle, numOfTiles);
		if(color) {
			GLES20.glUniform1i(colorHandle, 1);
		} else {
			GLES20.glUniform1i(colorHandle, 0);
		}
	}

}
