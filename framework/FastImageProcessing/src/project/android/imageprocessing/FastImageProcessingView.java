package project.android.imageprocessing;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Fast image processing view extension of GLSurfaceView.  This class is required for all fast image processing
 * activities that use this framework.  With the help of the {@link FastImageProcessingPipeline}, this class provides
 * the OpenGL context required for image processing.  Set of the framework is as follows:
 * <p>
 * Add FastImageProcessingView to activity layout. <p>
 * Add FastImageProcessingPipeline to FastImageProcessingView <p>
 * Create a input point and attach filter chains to the input and attach endpoints to the filter chains using addTarget. <p>
 * <code>	image = new ImageResourceInput(view, this, R.drawable.picture); <p>
 *			filter = new GreyScaleFilter(); <p>
 *			screen = new ScreenEndpoint(pipeline); <p>
 *			image.addTarget(filter); <p>
 *			filter.addTarget(screen); <p>
 * </code><p>
 * Pass the input point into the FastImageProcessingPipeline as the root renderer and start the pipeline. <p>
 * <code>	
		pipeline.addRootRenderer(image); <p>
		pipeline.startRendering(); <p>
 * </code><p>
 * @author Chris Batt
 */
public class FastImageProcessingView extends GLSurfaceView {
	
	/**
	 * Creates a new view which can be used for fast image processing.
	 * @param context The activity context that this view belongs to.
	 */
	public FastImageProcessingView(Context context) {
		super(context);	
		setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);
        setEGLContextClientVersion(2);
	}
	
	/**
	 * Sets the FastImageProcessingPipeline that will do the rendering for this view.
	 * @param pipeline The FastImageProcessingPipeline that will do the rendering for this view.
	 */
	public void setPipeline(FastImageProcessingPipeline pipeline) {
		setRenderer(pipeline);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

}
