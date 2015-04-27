package com.javacodegeeks.androidqrcodeexample;


import java.util.Collections;
import java.util.HashMap;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.javacodegeeks.pojo.SellerAuthenticationResponseDto;
import com.javacodegeeks.pojo.SellerAuthenticationResponseStatus;
import com.javacodegeeks.pojo.SellerAutheticationRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SellerLoginActivity extends Activity {
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	EditText editTextOutletId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//check whether user has logged in or not.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String outletId = settings.getString("outletId", null);
		if(outletId != null){
			//if logged in, directly start main activity.
			startActivity(new Intent(getApplicationContext(), ControllerActivity.class));
			finish();
		}
		else{
			//if not logged in, make user login.
			setContentView(R.layout.activity_seller_login);
			this.editTextOutletId = (EditText) findViewById(R.id.idEditTextOutletId);
		}
	}
	
	//on click listener for authenticate button.
	public void authenticateSeller(View v){
		
		String outletIdText = this.editTextOutletId.getText().toString();
		Log.d("LoginActivity", "Outlet id before trimming: " + outletIdText + "<<");
		outletIdText = outletIdText.trim();
		Log.d("LoginActivity", "Outlet id after trimming: " + outletIdText +"<<");
		if(outletIdText == null || outletIdText.equals("")){
			Toast tost = Toast.makeText(getApplicationContext(), "Please enter outlet Id.", Toast.LENGTH_SHORT);
			tost.show();
		}
		else{
			PostOutletAuthenticationTask task = new PostOutletAuthenticationTask(getApplicationContext());
			task.execute(outletIdText);
		}
	}
	
	public class PostOutletAuthenticationTask extends AsyncTask<String, Void, SellerAuthenticationResponseDto>{

		private Context appContext;

		public PostOutletAuthenticationTask(Context appContext){
			this.appContext = appContext;
		}
		
		@Override
		protected SellerAuthenticationResponseDto doInBackground(String... params) {
			String outletId = params[0];
			String url = "http://grabztestenv.elasticbeanstalk.com/seller/authenticate";
			SellerAuthenticationResponseDto response = sendPostRequest(url, outletId);
			return response;
		}
		
		private SellerAuthenticationResponseDto sendPostRequest(String url,
				String outletId) {
			try{
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
				RestTemplate restTemplate = new RestTemplate();
				MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
				restTemplate.getMessageConverters().add(mapper);
				SellerAutheticationRequest request = new SellerAutheticationRequest();
				request.setOutletId(outletId);
				HttpEntity<SellerAutheticationRequest> requestEntity = new HttpEntity<SellerAutheticationRequest>(request,requestHeaders);
				ResponseEntity<SellerAuthenticationResponseDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SellerAuthenticationResponseDto.class);
				return responseEntity.getBody();
			}
			catch(Exception e){
				return null;
			}
		}

		@Override
		protected void onPostExecute(SellerAuthenticationResponseDto response){
			if(response == null){
				Toast toast = Toast.makeText(this.appContext, "Sorry we can not authenticate at this time.", Toast.LENGTH_LONG);
				toast.show();
			}
			else if(response.getStatus().equals(SellerAuthenticationResponseStatus.ERROR)){
				Toast toast = Toast.makeText(this.appContext, response.getMessage(), Toast.LENGTH_LONG);
				toast.show();
			}
			else {
				//put outlet id in the shared preference - to use it in next activity.
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("outletId", response.getOutletId());
				editor.commit();
				
				//start our next activity
				startActivity(new Intent(getApplicationContext(), AndroidBarcodeQrExample.class));
				
				//kill login activity, so that user will not be able to go back to this activity 
				//by pressing back button
				finish();
			}
		}
	}
}
