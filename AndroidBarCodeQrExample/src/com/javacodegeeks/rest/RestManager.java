/**
 * 
 */
package com.javacodegeeks.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ietf.jgss.Oid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.javacodegeeks.pojo.AisleItem;
import com.javacodegeeks.pojo.AisleNameDto;
import com.javacodegeeks.pojo.LayoutDto;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.pojo.LinksDto;

/**
 * @author Amit
 *
 */
public class RestManager {

	public static final String BASE_URL = "http://grabztestenv.elasticbeanstalk.com";
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
		//RestManager manager = new RestManager();
		HttpEntity<?> requestEntity = getRequestEntity();
		RestTemplate restTemplate = getRestTemplate();
		String url = String.format("%s/seller/outlets/%s/aisles/", BASE_URL ,outletId);
		ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String[].class);
		String[] aisleNames = responseEntity.getBody();
		return aisleNames;
	}

	public AisleNameDto[] getAisles(String outletId) {
		try{
		HttpEntity<?> requestEntity = getRequestEntity();
		RestTemplate restTemplate = getRestTemplate();
		String url = String.format("%s/seller/outlets/%s/aisles", BASE_URL, outletId);
		ResponseEntity<AisleNameDto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AisleNameDto[].class);
		AisleNameDto[] aisleNameDtos = responseEntity.getBody();
		return aisleNameDtos;
		}
		catch(HttpClientErrorException e){
			Log.e("GetAisleNamesTask", "error:" + e.getStatusCode());
			return null;
		}
		catch (Exception e) {
			Log.e("GetAisleNamesTask", "Unkown Error.");
			return null;
		}
	}

	public AisleNameDto postNewAisle(String outletId, String aisleName) {
		
		String url = String.format("%s/aisles", BASE_URL);

		try{
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(new MediaType("application","json"));
			RestTemplate restTemplate = new RestTemplate();
			MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
			restTemplate.getMessageConverters().add(mapper);
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			Map<String,String> aisleBody = new HashMap<String, String>();
			aisleBody.put("outletId",outletId);
			aisleBody.put("aisleNum", aisleName);
			HttpEntity<Map> requestEntity = new HttpEntity<Map>(aisleBody,requestHeaders);
			ResponseEntity<LayoutDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,LayoutDto.class);
			LayoutDto layoutDto = responseEntity.getBody();
			String aisleNameNew = layoutDto.getLayout().getAisleNum();
			AisleNameDto aisleNameDto = new AisleNameDto(aisleNameNew);
			String urlPattern = "/seller/outlets/%s/aisles/%s";
			String deleteUrl = String.format(urlPattern, layoutDto.getLayout().getOutletId(), aisleNameNew);
			aisleNameDto.addLink(new LinkDto("delete-aisle", deleteUrl, "DELETE"));
			return aisleNameDto;
		}
		catch(HttpClientErrorException e){
			Log.i("Post New Aisle", "Error: " + e.getStatusText());
			return null;
		}
		catch (Exception e) {
			Log.i("Post New Aisle", "Error: " + e.getMessage());
			return null;
		}
	}

	public HttpStatus deleteAisle(String url) {
		try{
			HttpEntity<?> requestEntity = getRequestEntity();
			RestTemplate restTemplate = getRestTemplate();
			
			ResponseEntity<Void> responseEntity = restTemplate.exchange(BASE_URL + url, HttpMethod.DELETE, requestEntity, Void.class);
			HttpStatus statusCode = responseEntity.getStatusCode();
			return statusCode;
		}
		catch(HttpClientErrorException e){
			Log.i("Delete-Aisle Task:", e.getStatusText());
			return e.getStatusCode();
		}
	}
}
