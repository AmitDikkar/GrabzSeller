/**
 * 
 */
package com.javacodegeeks.pojo;

/**
 * @author Amit
 *
 */
public class AisleNameDto extends LinksDto{

	String aisleName;
	
	public AisleNameDto(){
		
	}

	public AisleNameDto(String aisleName) {
		this.aisleName = aisleName;
	}

	/**
	 * @return the aisleName
	 */
	public String getAisleName() {
		return aisleName;
	}

	/**
	 * @param aisleName the aisleName to set
	 */
	public void setAisleName(String aisleName) {
		this.aisleName = aisleName;
	}
	
	
}
