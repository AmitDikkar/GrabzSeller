/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.javacodegeeks.androidqrcodeexample.AisleItemsAdapter;
import com.javacodegeeks.androidqrcodeexample.AndroidBarcodeQrExample;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.DeleteAisleItemResponse;
import com.javacodegeeks.rest.RestManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class OnAisleItemDeleteClickListener implements OnClickListener{

	private String url;
	private Context context;
	AisleItemsAdapter adapter;
	private int position;
	
    public OnAisleItemDeleteClickListener(String url, Context context, AisleItemsAdapter aisleItemsAdapter, int position){
          this.url = url;
          this.context = context;
          this.adapter = aisleItemsAdapter;
          this.position = position;
     }
    
	@Override
	public void onClick(View v) {
		Log.i("DELETE_Aisle_item", "Gogin to delete an item.");
		new DeleteAisleItemTask().execute(url);
	}
	
	
public class DeleteAisleItemTask extends AsyncTask<String, Void, DeleteAisleItemResponse>{
	
	@Override
	protected DeleteAisleItemResponse doInBackground(String... params) {
		String url = params[0];
		try{
			RestManager manager = new RestManager();
			HttpEntity<?> requestEntity = manager.getRequestEntity();
			RestTemplate restTemplate = manager.getRestTemplate();

			Log.i("DELETE_Aisle_item: Put request on:", url);
			ResponseEntity<DeleteAisleItemResponse> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, DeleteAisleItemResponse.class);
			DeleteAisleItemResponse response = responseEntity.getBody();
			return response;
		}
		catch (Exception e){
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(DeleteAisleItemResponse result) {
		super.onPostExecute(result);
		if(result != null){
			Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();;
			
			//since our operation is succesful we will remove it from the list.
			AndroidBarcodeQrExample.aisleItemDtos.remove(position);
			adapter.notifyDataSetChanged();
		}
		else{
			Toast.makeText(context, "Sorry, Item can not be deleted at this time.", Toast.LENGTH_LONG).show();;
		}
	}
}
}
