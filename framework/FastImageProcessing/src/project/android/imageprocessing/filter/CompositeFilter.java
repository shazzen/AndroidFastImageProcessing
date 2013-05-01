package project.android.imageprocessing.filter;

import java.util.ArrayList;
import java.util.List;

import project.android.imageprocessing.input.GLTextureOutputRenderer;

/**
 * An extension of a MultiInputFilter.  This class can be used to create a filter that takes multiple inputs
 * where some of the inputs are other filters internal to this filter.  For example, if a filter requires both
 * an input image and the blur of the input image, a CompositeFilter should be used.  Internal filters need to be 
 * registered so that they can be distinguished from external inputs.  All of the internal filters should be registered
 * with registerInitialFilter(GLTextureOutputRenderer filter), registerTerminalFilter(GLTextureOutputRenderer filter) or
 * registerFilter(GLTextureOutputRenderer filter).  If the internal filter requires the input of the
 * external filter then it should be registered as an initial filter.  If the filter output is used in the fragment shader
 * of the CompositeFilter, it should be registered as an terminal filter.  If it is neither an initial or terminal filter, but
 * is still part of the pipeline that is internal to the CompositeFilter, it should be registered using registerFilter.  
 * If an external input is required as input to an initial filter and is also required as input to the CompositeFilter,
 * it should be registered with registerInputOutputFilter(GLTextureOutputRenderer filter). On top of the
 * registering the internal filters, the filter locations for the {@link MultiInputFilter} should also be registered.
 */
public class CompositeFilter extends MultiInputFilter {
	private List<BasicFilter> initialFilters;
	private List<GLTextureOutputRenderer> terminalFilters;
	private List<GLTextureOutputRenderer> inputOutputFilters;
	private List<GLTextureOutputRenderer> filters;

	/**
	 * Creates a CompositeFilter with the default {@link BasicFilter} shaders that takes in a given number of inputs
	 * @param numOfInputs
	 * The number of inputs that this filter expects
	 */
	public CompositeFilter(int numOfInputs) {
		super(numOfInputs);
		initialFilters = new ArrayList<BasicFilter>();
		terminalFilters = new ArrayList<GLTextureOutputRenderer>();
		inputOutputFilters = new ArrayList<GLTextureOutputRenderer>();
		filters = new ArrayList<GLTextureOutputRenderer>();
	}
	
	/* (non-Javadoc)
	 * @see project.android.imageprocessing.input.GLTextureOutputRenderer#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		for(GLTextureOutputRenderer filter : filters) {
			filter.destroy();
		}
	}
	
	/*
	 * If the source is one of the end points of the input filters then it is the result 
	 * of one of the internal filters. When all internal filters have finished we can
	 * draw the multi-input filter. If the source is not in the list of renderers then it 
	 * must be an external input which should be passed to each of the initial renderers
	 * of this multi-input filter.
	 */
	/* (non-Javadoc)
	 * @see project.android.imageprocessing.filter.BasicFilter#newTextureReady(int, project.android.imageprocessing.input.GLTextureOutputRenderer)
	 */
	@Override
	public void newTextureReady(int texture, GLTextureOutputRenderer source) {
		if(inputOutputFilters.contains(source)) {
			if(!texturesReceived.contains(source)) {
				super.newTextureReady(texture, source);
				for(BasicFilter initialFilter : initialFilters) {
					initialFilter.newTextureReady(texture, source);
				}
			}
		} else if(terminalFilters.contains(source)) {
			super.newTextureReady(texture, source);
		} else {
			for(BasicFilter initialFilter : initialFilters) {
				initialFilter.newTextureReady(texture, source);
			}
		}
	}
	
	protected void registerFilter(GLTextureOutputRenderer filter) {
		if(!filters.contains(filter)) {
			filters.add(filter);
		}
	}
	
	protected void registerInitialFilter(BasicFilter filter) {
		initialFilters.add(filter);
		registerFilter(filter);
	}

	protected void registerInputOutputFilter(GLTextureOutputRenderer filter) {
		inputOutputFilters.add(filter);
	}
	
	protected void registerTerminalFilter(GLTextureOutputRenderer filter) {
		terminalFilters.add(filter);
		registerFilter(filter);
	}
	
	/* (non-Javadoc)
	 * @see project.android.imageprocessing.GLRenderer#setRenderSize(int, int)
	 */
	@Override
	public void setRenderSize(int width, int height) {
		for(GLTextureOutputRenderer filter : filters) {
			filter.setRenderSize(width, height);
		}
		super.setRenderSize(width, height);
	}
	
	
}
