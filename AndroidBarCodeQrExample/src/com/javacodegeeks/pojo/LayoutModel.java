package com.javacodegeeks.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class represents the "TagId - outletId - AisleNum - Items (with price)" Mapping.
 * This represents the "AisleItemMappings (old Layouts)" collection of Design diagram present in the shared folder.
 * Structure:
 * <pre>
 * {
 *  "_id": {
        "$oid": "550a22d6d973530f89c8e31f"
    },
    "aisleNum": "Aisle:Front Left",
    "tagId": "OUTe1eb38866bANFront Left",
    "outletId": "e1eb38866b",
    "aisleItems": [
        {
            "id": "77637",
            "price": "6"
        },
        {
            "itemId": "77638",
            "name": "Bacardi Frozen Mixers Frozen Concentrate Non-Alcoholic  - 10 Oz Can",
            "price": "6"
        },
        {
            "itemId": "77639",
            "name": "Bacardi Frozen Mixers Frozen Concentrate Non-Alcoholic  - 10 Oz Can",
            "price": "6"
        }
    ]
 }
 </pre>
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */
public class LayoutModel {

	/**
	 * This is auto generated _id field of the MongoDb.
	 * **/
	private String _id;
	
	/**
	 * Items (along with price) that belongs to this particular Aisle.
	 * **/
	private List<AisleItem> aisleItems = new ArrayList<AisleItem>();
	
	/**
	 * "Aisle number" represented by the tagId in this particulat outlet.
	 * */
	private String aisleNum;
	
	/**
	 * Tag Id of the Aisle.
	 * */
	private String tagId;
	
	/**
	 * Id of the outlet in which this aisle is present.
	 * */
	private String outletId;
	

	/**
	 * @param _id
	 * @param aisleItems
	 * @param aisleNum
	 * @param tag_id
	 * @param outletId
	 */
	public LayoutModel(List<AisleItem> aisleItems, String aisleNum, String tagId, String outletId) {
		super();
		this.aisleItems = aisleItems;
		this.aisleNum = aisleNum;
		this.tagId = tagId;
		this.outletId = outletId;
	}

	/**
	 * 
	 */
	public LayoutModel() {
		super();
	}
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
	 * @return the aisleItems
	 */
	public List<AisleItem> getAisleItems() {
		return aisleItems;
	}

	/**
	 * @param aisleItems the aisleItems to set
	 */
	public void setAisleItems(List<AisleItem> aisleItems) {
		this.aisleItems = aisleItems;
	}

	/**
	 * @return the aisleNum
	 */
	public String getAisleNum() {
		return aisleNum;
	}

	/**
	 * @param aisleNum the aisleNum to set
	 */
	public void setAisleNum(String aisleNum) {
		this.aisleNum = aisleNum;
	}

	/**
	 * @return the outletId
	 */
	public String getOutletId() {
		return outletId;
	}

	/**
	 * @param outletId the outletId to set
	 */
	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	/**
	 * @return the tagId
	 */
	public String getTagId() {
		return tagId;
	}

	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
}
