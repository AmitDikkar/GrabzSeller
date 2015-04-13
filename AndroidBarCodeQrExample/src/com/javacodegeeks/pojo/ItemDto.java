/**
 * 
 */
package com.javacodegeeks.pojo;

import com.javacodegeeks.pojo.ItemModel;;

/**
 * @author Amit
 *
 */
public class ItemDto extends LinksDto{

	private ItemModel item;

	public ItemDto(){
		
	}
	/**
	 * @param item
	 */
	public ItemDto(ItemModel item) {
		super();
		this.item = item;
	}

	/**
	 * @return the item
	 */
	public ItemModel getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(ItemModel item) {
		this.item = item;
	}
}
