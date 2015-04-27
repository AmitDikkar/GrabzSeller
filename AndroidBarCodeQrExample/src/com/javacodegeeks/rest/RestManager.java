/**
 * 
 */
package com.javacodegeeks.rest;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Amit
 *
 */
public class RestManager {

	static final String BASE_URL = "http://grabztestenv.elasticbeanstalk.com";
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
	
	public String[] getAisleNames(String outletId){
		RestManager manager = new RestManager();
		HttpEntity<?> requestEntity = getRequestEntity();
		RestTemplate restTemplate = getRestTemplate();
		String url = String.format("%s/seller/outlets/%s/aisles/", BASE_URL ,outletId);
		ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String[].class);
		String[] aisleNames = responseEntity.getBody();
		return aisleNames;
	}
}
