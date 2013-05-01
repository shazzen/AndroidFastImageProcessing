package project.android.imageprocessing.filter.colour;

import project.android.imageprocessing.filter.MultiInputFilter;
import project.android.imageprocessing.helper.ImageHelper;
import project.android.imageprocessing.input.GLTextureOutputRenderer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

/**
 * Uses an RGB color lookup image to remap the colors in an image. First, use your favourite photo editing application to apply a filter to lookup.png from AndroidFastImageProcessing/res/drawable. For this to work properly each pixel color must not depend on other pixels (e.g. blur will not work). If you need a more complex filter you can create as many lookup tables as required. Once ready, use your new lookup.png file as the resource input for LookupFilter.
 * @author Chris Batt
 */
public class LookupFilter extends MultiInputFilter {
	private int lookup_texture;
	private Bitmap lookupBitmap;
	
	public LookupFilter(Context context, int id) {
		super(2);
		final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
		lookupBitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(lookup_texture != 0) {
			int[] tex = new int[1];
			tex[0] = lookup_texture;
			GLES20.glDeleteTextures(1, tex, 0);
			lookup_texture = 0;
		}
	}
	
	
	@Override
	protected String getFragmentShader() {
		return 
				 "precision mediump float;\n" 
				+"uniform sampler2D "+UNIFORM_TEXTURE0+";\n" 
				+"uniform sampler2D "+UNIFORM_TEXTUREBASE+1+";\n" 
				+"varying vec2 "+VARYING_TEXCOORD+";\n"	
				
		  		+ "void main(){\n"
		  		+ "  vec4 texColour = texture2D("+UNIFORM_TEXTURE0+","+VARYING_TEXCOORD+");\n"
		  		+ "  float blueColor = texColour.b * 63.0;\n"
		  		+ "  vec2 quad1;\n"
		  		+ "  quad1.y = floor(floor(blueColor) / 8.0);\n"
		  		+ "  quad1.x = floor(blueColor) - (quad1.y * 8.0);\n"
		  		+ "  vec2 quad2;\n"
		  		+ "  quad2.y = floor(ceil(blueColor) / 8.0);\n"
		  		+ "  quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n"
		  		+ "  vec2 texPos1;\n"
		  		+ "  texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * texColour.r);\n"
		  		+ "  texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * texColour.g);\n"
		  		+ "  vec2 texPos2;\n"
		  		+ "  texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * texColour.r);\n"
		  	    + "  texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * texColour.g);\n"
		  		+ "  vec4 newColor1 = texture2D("+UNIFORM_TEXTUREBASE+1+", texPos1);\n"
		  		+ "  vec4 newColor2 = texture2D("+UNIFORM_TEXTUREBASE+1+", texPos2);\n"
		  		+ "  vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n"
		  		+ "  gl_FragColor = vec4(newColor.rgb, texColour.a);\n"
		  		+ "}\n";		
	}
	
	@Override
	public void newTextureReady(int texture, GLTextureOutputRenderer source) {
		if(filterLocations.size() < 2 || !source.equals(filterLocations.get(0))) {
			clearRegisteredFilterLocations();
			registerFilterLocation(source, 0);
			registerFilterLocation(this, 1);
		}
		if(lookup_texture == 0) {
			lookup_texture = ImageHelper.bitmapToTexture(lookupBitmap);
		}
		super.newTextureReady(lookup_texture, this);
		super.newTextureReady(texture, source);
	}
}
