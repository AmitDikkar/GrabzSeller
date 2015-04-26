/**
 * 
 */
package com.javacodegeeks.rest;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Amit
 *
 */
public class RestManager {

	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
		restTemplate.getMessageConverters().add(mapper);
		return restTemplate;
	}
	
	public HttpEntity<?> getRequestEntity(){
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		return requestEntity;
	}
}
