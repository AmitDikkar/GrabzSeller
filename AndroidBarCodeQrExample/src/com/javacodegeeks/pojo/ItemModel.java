package com.javacodegeeks.pojo;

/**
 * This class represents documents of "Items" collection. Items collection has all the items.
 * Document Structure:
 * <pre>
 * {
    "_id": {
        "$oid": "550a1c22d97366042b679c7e"
    },
    "itemId": "29141",
    "category": "Bread & Bakery",
    "name": "Safeway SELECT Salted Breadsticks - Each",
    "description": "No Description",
    "imageUrl": "http://smapistorage.blob.core.windows.net/thumbimages/noimageavailable100x100.gif",
    "StoreId": "e1eb38866b",
    "color": "Red",
    "size": "12"
}
 * </pre>
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */

public class ItemModel {

	/**
	 * Auto generated Id by MongoDb. No need to set this explicitly.
	 * **/

	private String _id;
	
	/**
	 * Item id received from SupermarketApi. If it is not retrieved from Supermarket API,
	 * it is the upc code of the item. 
	 * **/
	private String itemId;
	
	/**
	 * Name of the item.
	 */
	private String name;
	
	/**
	 * Brand of the item.
	 */
	private String brand;
	
	/**
	 * Small description of the item.
	 */
	private String description;
	
	/**
	 * Color of the item.
	 */
	private String color;
	
	/**
	 * Size of the item.
	 */
	private String size;
	
	/**
	 * Category of the item. 
	 * **/
	private String category;
	
	/**
	 * Image url of the item. 
	 * **/
	private String imageUrl;
	
	/**
	 * @param _id
	 * @param itemId
	 * @param name
	 * @param brand
	 * @param description
	 * @param color
	 * @param size
	 * @param category
	 * @param imageUrl
	 */
	public ItemModel(String _id, String itemId, String name, String brand,
			String description, String color, String size, String category,
			String imageUrl) {
		super();
		this._id = _id;
		this.itemId = itemId;
		this.name = name;
		this.brand = brand;
		this.description = description;
		this.color = color;
		this.size = size;
		this.category = category;
		this.imageUrl = imageUrl;
	}

	public ItemModel(){
		
	}

	/**
	 * Use this constructor to create an object from "ItemDomain".
	 * @param itemDomain
	 *//*
	public ItemModel(ItemDomain itemDomain){
		this.name = itemDomain.getName();
		this.brand = itemDomain.getBrand();
		this.color = itemDomain.getColor();
		this.description = itemDomain.getDescription();
		this.size = itemDomain.getSize();
	}*/
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
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
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
}
