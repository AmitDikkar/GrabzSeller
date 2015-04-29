/**
 * 
 */
package com.javacodegeeks.pojo;


/**
 * @author Amit
 *
 */
public class LayoutUpdateRequest {

	String action;
	
	String itemId;
	
	double promotionalPrice;
	
	String promotionName;

	public LayoutUpdateRequest(){
		
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the promotionName
	 */
	public String getPromotionName() {
		return promotionName;
	}
	/**
	 * @param promotionName the promotionName to set
	 */
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	/**
	 * @return the promotionalPrice
	 */
	public double getPromotionalPrice() {
		return promotionalPrice;
	}
	/**
	 * @param promotionalPrice the promotionalPrice to set
	 */
	public void setPromotionalPrice(double promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}
}
