package com.javacodegeeks.androidqrcodeexample;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import com.javacodegeeks.pojo.AisleItem;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.ItemDto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AndroidBarcodeQrExample extends Activity implements OnItemSelectedListener {
	/** Called when the activity is first created. */

	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	String outletId;
	private static final String PREFS_NAME = "MyPrefsFile";

	Spinner spinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		this.spinner = (Spinner) findViewById(R.id.spinner_aisleNames);
		
		//retrieve outletId from the shared preferences.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		this.outletId = settings.getString("outletId", null);
		Toast toast = Toast.makeText(this, "Main Activity shared preference value: " + outletId, Toast.LENGTH_SHORT);
		toast.show();
		
		populateSpinner();
		this.spinner.setOnItemSelectedListener(this);
	}

	private void populateSpinner() {
		GetAisleItemsTask task = new GetAisleItemsTask(this);
		//task.execute("http://grabztestenv.elasticbeanstalk.com/seller/outlets/"+this.outletId+"/aisles/");
		String url = String.format("http://grabztestenv.elasticbeanstalk.com/seller/outlets/%s/aisles/", this.outletId);
		task.execute(url);
			
	}

	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {

				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});
		return downloadDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String upcCode = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				
				String selectedAisle = String.valueOf(this.spinner.getSelectedItem());
				String outletId = this.outletId;
				
				//post this item to backend.
				PostScannedItemTask task = new PostScannedItemTask(this);
				task.execute(outletId, selectedAisle, upcCode);
			}
		}
	}
	

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
            long id) {
        String selectedAisleItem = parent.getItemAtPosition(position).toString();
        Toast tost = Toast.makeText(getApplicationContext(), "You have selected" + selectedAisleItem, Toast.LENGTH_SHORT);
        tost.show();
        displayAisleItems(selectedAisleItem);
    }
	
	private void displayAisleItems(String selectedAisleItem) {
		GetItemsTask task = new GetItemsTask(this);
		task.execute(this.outletId);
	}

	/**
	 * To send POST request to add scanned item in database.
	 * @author Amit
	 *
	 */
	public class PostScannedItemTask extends AsyncTask<String, Void, AisleItem>{

		HttpStatus responseCode;
		Context appContext;
		
		public PostScannedItemTask(Context appContext) {
			this.appContext = appContext;
		}

		@Override
		protected AisleItem doInBackground(String... params) {
			String outletId = params[0];
			String selectedAisle = params[1];
			String upcCode = params[2];
			String url = "http://grabztestenv.elasticbeanstalk.com//seller/outlets/"+outletId+"/aisles/"+ selectedAisle+"/items/"+upcCode;
			Log.i("PostScannedItemTask - doInBackground", "url received: " + url);
			AisleItem aisleItem = sendPostRequest(url);
			return aisleItem;
		}

		@Override
		protected void onPostExecute(AisleItem aisleItem) {
			if(aisleItem == null){
				Toast toast = Toast.makeText(this.appContext, "Sorry, we couldn't find this item in our database. Please contact Team Grabz.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}
			else{
				Toast toast = Toast.makeText(this.appContext, "Item" + aisleItem.getName() + " has been added", Toast.LENGTH_LONG);
				toast.show();
			}
	     }
		
		private AisleItem sendPostRequest(String url) {
			try{
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
				HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
				RestTemplate restTemplate = new RestTemplate();
				MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
				restTemplate.getMessageConverters().add(mapper);
				ResponseEntity<AisleItemDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AisleItemDto.class);
				Log.i("Status Code", "Status code" + responseEntity.getStatusCode());
				AisleItemDto aisleItemDto = responseEntity.getBody();
				responseCode = responseEntity.getStatusCode();
				Log.i("PostScannedItemTask - sendPostRequest", "Request Executed with status code : " + responseCode + " and item received is:" + aisleItemDto.getAisleItem().getName());
				return aisleItemDto.getAisleItem();
			}
			catch(Exception e){
				return null;
			}
		}
	}
	
	/**
	 * To send GET request for AisleNames.
	 * @author Amit
	 *
	 */
	public class GetAisleItemsTask extends AsyncTask<String, Void, String[]>{

		HttpStatus responseCode;
		Context appContext;
		public GetAisleItemsTask(Context appContext){
			this.appContext = appContext;
		}
		
		@Override
		protected String[] doInBackground(String... params) {
			String url = params[0];
			// Set the Accept header
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
			RestTemplate restTemplate = new RestTemplate();
			MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
			restTemplate.getMessageConverters().add(mapper);
			//       BasketDto[] baskets = restTemplate.getForObject(url, BasketDto[].class);
			ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String[].class);
			String[] aisleNames = responseEntity.getBody();

			this.responseCode = responseEntity.getStatusCode();
			Log.d("GET Task on", url+" " + responseCode.toString());
			return aisleNames;
		}

		 @Override
	     protected void onPostExecute(String[] aisleItems) {
				Toast toastNew = Toast.makeText(appContext, "Received " + aisleItems.length + " Items", Toast.LENGTH_LONG);
				toastNew.show();
	         if (aisleItems.length != 0 && this.responseCode == HttpStatus.OK) {
	             populateSpinner(aisleItems);
	         }
	     }

		private void populateSpinner(String[] aisleItems) {
			Log.i("PopulateSpinner", "populating spinner. adding " + aisleItems.length + " aisle names.");
			// Create an ArrayAdapter using the string array and a default spinner layout
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.appContext, android.R.layout.simple_spinner_item, aisleItems);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public class GetItemsTask extends AsyncTask<String, Void, AisleItemDto[]>{

		Context appContext;
		
		private HttpStatus responseCode;
		
		public GetItemsTask(Context appContext){
			this.appContext = appContext;
		}
		
		@Override
		protected AisleItemDto[] doInBackground(String... params) {
			String outletId = params[0];
			String url = "http://grabztestenv.elasticbeanstalk.com/seller/outlets/"+outletId+"/aisles/Aisle:11/items";
			try{
				// Set the Accept header
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
				HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
				RestTemplate restTemplate = new RestTemplate();
				MappingJacksonHttpMessageConverter mapper = new MappingJacksonHttpMessageConverter();
				restTemplate.getMessageConverters().add(mapper);
				//       BasketDto[] baskets = restTemplate.getForObject(url, BasketDto[].class);
				ResponseEntity<AisleItemDto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,AisleItemDto[].class);
				AisleItemDto[] aisleNames = responseEntity.getBody();

				this.responseCode = responseEntity.getStatusCode();
				Log.d("GET Task on", url+" " + responseCode.toString());
				return aisleNames;
			}
			catch (Exception e){
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(AisleItemDto[] aisleItemDtos) {
			//display AisleItemDtos on the list view.
			if(aisleItemDtos == null){
				Toast toast = Toast.makeText(appContext, "Error Rtrieving Aisle's items", Toast.LENGTH_SHORT);
				toast.show();
			}
			else{
				Toast toast = Toast.makeText(appContext, "Received " + aisleItemDtos.length + " items", Toast.LENGTH_SHORT);
				toast.show();
				AisleItemsAdapter adapter = new AisleItemsAdapter(appContext,
		                R.layout.listview_item_row, aisleItemDtos);
		       
		        ListView listView1 = (ListView)findViewById(R.id.idListViewAisleItems);
		        
		        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
		        listView1.addHeaderView(header);
		        listView1.setAdapter(adapter);
			}
		}
	}
}