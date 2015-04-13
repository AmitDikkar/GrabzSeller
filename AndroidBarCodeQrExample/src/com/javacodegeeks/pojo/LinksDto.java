/**
 * 
 */
package com.javacodegeeks.pojo;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Sina Nikkhah, Amit Dikkar, Shaji Thiyarathodi, Priyanka Deo
 *
 */
public class LinksDto {

	private List<LinkDto> links = new ArrayList<LinkDto>();
	
	public List<LinkDto> getLinks() {
		return links;
	}

	public void setLinks(List<LinkDto> links) {
		this.links = links;
	}

	public void addLink(LinkDto link) {
		links.add(link);
	    }
	
}
