/**
 * 
 */
package com.javacodegeeks.pojo;

/**
 * @author Amit
 *
 */
public class SellerAutheticationRequest {

	/**
	 * Outlet Id to be authenticated.
	 */
	String outletId;
	
	public SellerAutheticationRequest(){
		
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
}
