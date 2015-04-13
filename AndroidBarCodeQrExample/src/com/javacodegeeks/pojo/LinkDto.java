package com.javacodegeeks.pojo;

/**
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */
public class LinkDto {

	private String rel = "self";
	private String href = "#";
	private String method = "GET";

	public LinkDto(String rel, String href, String method) {
		this.rel = rel;
		this.href = href;
		this.method = method;
	    }
	
	public LinkDto(){
		
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}
