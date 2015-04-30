/**
 * 
 */
package com.javacodegeeks.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.AisleNameDto;
import com.javacodegeeks.pojo.DeleteAisleItemResponse;
import com.javacodegeeks.pojo.LayoutDto;
import com.javacodegeeks.pojo.LayoutUpdateActions;
import com.javacodegeeks.pojo.LayoutUpdateRequest;
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
		String[] aisleNames = null;
		try{
			//RestManager manager = new RestManager();
			HttpEntity<?> requestEntity = getRequestEntity();
			RestTemplate restTemplate = getRestTemplate();
			String url = String.format("%s/seller/outlets/%s/aisles/names", BASE_URL ,outletId);
			ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String[].class);
			aisleNames = responseEntity.getBody();
			return aisleNames;
		}
		catch (HttpClientErrorException e){
			Log.i("Get-AisleNames", "Failed to get aisle names: " + e.getStatusText());
			return aisleNames;
		}
		catch (Exception e) {
			Log.i("Get-AisleNames", "Failed to get aisle names: " + e.getLocalizedMessage());
			return aisleNames;
		}
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

	public AisleItemDto[] getPromotionalItems(String outletId, String aisleName) {
		AisleItemDto[] aisleItems = null;
		String url = String.format("%s/seller/outlets/%s/aisles/%s/promotions", BASE_URL, outletId, aisleName);
		
		try{
			RestManager manager = new RestManager();
			HttpEntity<?> requestEntity = manager.getRequestEntity();
			RestTemplate restTemplate = manager.getRestTemplate();
		
			ResponseEntity<AisleItemDto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AisleItemDto[].class);
			aisleItems = responseEntity.getBody();
			return aisleItems;
		}
		catch(HttpClientErrorException e){
			Log.i("Delete-Aisle Task:", e.getStatusText());
			return aisleItems;
		}
		catch (Exception e) {
			Log.e("GET-promotional Items", e.getLocalizedMessage());
			return aisleItems;
		}
	}

	public AisleItemDto promotion(String url, boolean shouldSetPromotion, double promotionalPrice, String promotionName) {
		try{
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(new MediaType("application","json"));
			RestTemplate restTemplate = new RestTemplate();
			MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
			restTemplate.getMessageConverters().add(mapper);
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			LayoutUpdateRequest updateRequest = new LayoutUpdateRequest();
			if(!shouldSetPromotion){
				updateRequest.setAction(LayoutUpdateActions.REMOVE_PROMOTION);
			}
			else{
				updateRequest.setAction(LayoutUpdateActions.SET_PROMOTION);
				updateRequest.setPromotionalPrice(promotionalPrice);
				updateRequest.setPromotionName("NO_NAME");
			}
			String fullUrl = BASE_URL + url;
			Log.i("Promotion", "sending PUT on : " + fullUrl);
			HttpEntity<LayoutUpdateRequest> requestEntity = new HttpEntity<LayoutUpdateRequest>(updateRequest,requestHeaders);
			ResponseEntity<AisleItemDto> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity,AisleItemDto.class);
			AisleItemDto aisleItemDto = responseEntity.getBody();
			return aisleItemDto;
		}
		catch(HttpClientErrorException e){
			Log.e("Promotion", "ShouldSetPromotion: " + shouldSetPromotion + " Error: " + e.getStatusText());
			return null;
		}
		catch (Exception e) {
			Log.e("Promotion", "ShouldSetPromotion: " + shouldSetPromotion + " Error: " + e.getLocalizedMessage());
			return null;
		}
	}

	public DeleteAisleItemResponse deleteAisleItem(String url) {

		try{
			url = BASE_URL + url;
			HttpEntity<?> requestEntity = getRequestEntity();
			RestTemplate restTemplate = getRestTemplate();

			Log.i("DELETE_Aisle_item: Put request on:", url);
			ResponseEntity<DeleteAisleItemResponse> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, DeleteAisleItemResponse.class);
			DeleteAisleItemResponse response = responseEntity.getBody();
			return response;
		}
		catch(HttpClientErrorException e){
			Log.e("DELETE Aisle Item", "Error: " + e.getStatusText());
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
