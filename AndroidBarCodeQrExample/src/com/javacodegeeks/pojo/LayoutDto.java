package com.javacodegeeks.pojo;

/**
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */
public class LayoutDto extends LinksDto{

	private LayoutModel layout;
	
	public LayoutDto(){
		
	}
	
	public LayoutDto(LayoutModel layout){
		super();
		this.layout = layout;
	}

	/**
	 * @return the layout
	 */
	public LayoutModel getLayout() {
		return layout;
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(LayoutModel layout) {
		this.layout = layout;
	}
}
