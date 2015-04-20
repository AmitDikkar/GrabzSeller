/**
 * 
 */
package com.javacodegeeks.pojo;

/**
 * @author Amit
 *
 */
public class SellerAuthenticationResponseDto {

	/**
	 * Message to be sent to the requester.
	 */
	String message;
	
	/**
	 * Outlet Id which has been authenticated.
	 */
	String outletId;
	
	/**
	 * Status of the authentication.
	 */
	String status;

	public SellerAuthenticationResponseDto(){
		
	}
	
	public SellerAuthenticationResponseDto(String outletId, String status,
			String message) {
		super();
		this.outletId = outletId;
		this.status = status;
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}
