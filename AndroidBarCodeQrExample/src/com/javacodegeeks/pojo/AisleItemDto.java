/**
 * 
 */
package com.javacodegeeks.pojo;


/**
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */
public class AisleItemDto extends LinksDto {
	
	private AisleItem aisleItem;
	
	/**
	 * 
	 */
	public AisleItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AisleItem getAisleItem() {
		return aisleItem;
	}

	public void setAisleItem(AisleItem aisleItem) {
		this.aisleItem = aisleItem;
	}

	public AisleItemDto (AisleItem aisleItem){
		super();
		this.aisleItem = aisleItem;
	}
}
