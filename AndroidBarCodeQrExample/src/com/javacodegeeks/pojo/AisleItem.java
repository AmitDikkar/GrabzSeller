/**
 * 
 */
package com.javacodegeeks.pojo;

import java.net.UnknownHostException;

/**
 * This class represents structure of "aisleItems" field of document of "layouts" collection.
 * This is the Item present in the Aisle of a particular store.
 * Document Structure:
 * <pre>
 *  {
 		"itemId": "87009",
        "name": "SAGRES FROZEN SARDINES ",
        "imageUrl": "/url/of/the/image",
        "price": "3.98"
    }
 * </pre>
 * @author Amit
 *
 */
public class AisleItem{

	/**
	 * This id represents one of the items of Items collection.
	 * **/
	private String itemId;

	/**
	 * Name of the item. 
	 * **/	
	private String name;

	/**
	 * Price of the Item (of given id) in a specific outlet.
	 * **/
	private float price;

	/**
	 * Url of the image of the item.This is same as url present in 'items' collection for this item.
	 */
	private String imageUrl;
	
	private boolean onPromotion = false;
	
	private float promotionalPrice = 0;
	
	private String promotionName = "NO_NAME"; 
	
	public AisleItem() {
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 * @throws UnknownHostException 
	 */
	public void setItemId(String itemId){
		this.itemId = itemId;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the onPromotion
	 */
	public boolean getOnPromotion() {
		return onPromotion;
	}

	/**
	 * @param onPromotion the onPromotion to set
	 */
	public void setOnPromotion(boolean onPromotion) {
		this.onPromotion = onPromotion;
	}

	/**
	 * @return the promotionalPrice
	 */
	public float getPromotionalPrice() {
		return promotionalPrice;
	}

	/**
	 * @param promotionalPrice the promotionalPrice to set
	 */
	public void setPromotionalPrice(float promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
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
}
