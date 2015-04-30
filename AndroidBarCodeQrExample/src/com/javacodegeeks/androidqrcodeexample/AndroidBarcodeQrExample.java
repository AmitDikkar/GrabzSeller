package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

import com.javacodegeeks.adapters.AisleItemsAdapter;
import com.javacodegeeks.pojo.AisleItem;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.ItemDto;
import com.javacodegeeks.rest.RestManager;

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
	
	ListView listViewAisleItems;
	public static List<AisleItemDto> aisleItemDtos = new ArrayList<AisleItemDto>();
	public static AisleItemsAdapter aisleItemsAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowHomeEnabled(true);
		setTitle("Item Scanner");
		setContentView(R.layout.activity_main);
		this.spinner = (Spinner) findViewById(R.id.spinner_aisleNames);
		this.spinner.setOnItemSelectedListener(this);
		
		//retrieve outletId from the shared preferences.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		this.outletId = settings.getString("outletId", null);
		
		//populate aisle names for this outlet.
		populateSpinner();
		
		//Create the list view to display items of the aisle.
		this.listViewAisleItems = (ListView)findViewById(R.id.idListViewAisleItems);
//		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
//        this.listViewAisleItems.addHeaderView(header);
        				
        //setting List view adapter for Aisle items list.
		this.aisleItemsAdapter = new AisleItemsAdapter(this, R.layout.listview_item_row, aisleItemDtos);
        this.listViewAisleItems.setAdapter(aisleItemsAdapter);
	}

	private void populateSpinner() {
		GetAisleNamesTask task = new GetAisleNamesTask(this);
		String url = String.format("http://grabztestenv.elasticbeanstalk.com/seller/outlets/%s/aisles/names", this.outletId);
		task.execute(url);
	}

	//onClick listner for scan barcode.
	public void scanBar(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	//on click listener for QR code scanning.
	public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AndroidBarcodeQrExample.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}
	
/*	//on click listener for logout.
	public void logout(View v){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("outletId", null);
		// Commit the edits!
		editor.commit();
		finish();
	}*/

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
        displayAisleItems(selectedAisleItem);
    }
	
	private void displayAisleItems(String selectedAisleItem) {
		GetItemsTask task = new GetItemsTask(this, this.listViewAisleItems);
		task.execute(this.outletId, selectedAisleItem);
	}

	/**
	 * To send POST request to add scanned item in database.
	 * @author Amit
	 *
	 */
	public class PostScannedItemTask extends AsyncTask<String, Void, AisleItemDto>{

		HttpStatus responseCode;
		Context appContext;
		
		public PostScannedItemTask(Context appContext) {
			this.appContext = appContext;
		}

		@Override
		protected AisleItemDto doInBackground(String... params) {
			String outletId = params[0];
			String selectedAisle = params[1];
			String upcCode = params[2];
			String url = "http://grabztestenv.elasticbeanstalk.com//seller/outlets/"+outletId+"/aisles/"+ selectedAisle+"/items/"+upcCode;
			Log.i("PostScannedItemTask - doInBackground", "url received: " + url);
			AisleItemDto aisleItemDto = sendPostRequest(url);
			return aisleItemDto;
		}

		@Override
		protected void onPostExecute(AisleItemDto aisleItemDto) {
			if(aisleItemDto == null){
				Toast toast = Toast.makeText(this.appContext, "Sorry, we couldn't find this item in our database. Please contact Team Grabz.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}
			else{
				Toast toast = Toast.makeText(this.appContext, "Item \"" + aisleItemDto.getAisleItem().getName() + "\" has been added.", Toast.LENGTH_LONG);
				toast.show();
				aisleItemDtos.add(aisleItemDto);
				aisleItemsAdapter.notifyDataSetChanged();
			}
	     }
		
		private AisleItemDto sendPostRequest(String url) {
			try{
				RestManager manager = new RestManager();
				HttpEntity<?> requestEntity = manager.getRequestEntity();
				RestTemplate restTemplate = manager.getRestTemplate();
				
				ResponseEntity<AisleItemDto> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AisleItemDto.class);
				Log.i("Status Code", "Status code" + responseEntity.getStatusCode());
				AisleItemDto aisleItemDto = responseEntity.getBody();
				responseCode = responseEntity.getStatusCode();
				Log.i("PostScannedItemTask - sendPostRequest", "Request Executed with status code : " + responseCode + " and item received is:" + aisleItemDto.getAisleItem().getName());
				return aisleItemDto;
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
	public class GetAisleNamesTask extends AsyncTask<String, Void, String[]>{

		HttpStatus responseCode;
		Context appContext;
		public GetAisleNamesTask(Context appContext){
			this.appContext = appContext;
		}
		
		@Override
		protected String[] doInBackground(String... params) {
			String url = params[0];
			RestManager manager = new RestManager();
			HttpEntity<?> requestEntity = manager.getRequestEntity();
			RestTemplate restTemplate = manager.getRestTemplate();
			ResponseEntity<String[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String[].class);
			String[] aisleNames = responseEntity.getBody();

			this.responseCode = responseEntity.getStatusCode();
			Log.d("GET Task on", url+" " + responseCode.toString());
			return aisleNames;
		}

		 @Override
	     protected void onPostExecute(String[] aisleItems) {
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
		ListView listViewAisleItems;
		
		public GetItemsTask(Context appContext, ListView listViewAisleItems){
			this.appContext = appContext;
			this.listViewAisleItems = listViewAisleItems;
		}
		
		@Override
		protected AisleItemDto[] doInBackground(String... params) {
			String outletId = params[0];
			String aisleNumber = params[1];
			String url = "http://grabztestenv.elasticbeanstalk.com/seller/outlets/"+outletId+"/aisles/" + aisleNumber +"/items";
			try{
				RestManager manager = new RestManager();
				HttpEntity<?> requestEntity = manager.getRequestEntity();
				RestTemplate restTemplate = manager.getRestTemplate();
				
				//TODO: find a way to receive List in the response.
				ResponseEntity<AisleItemDto[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AisleItemDto[].class);
				AisleItemDto[] aisleItems = responseEntity.getBody();
				this.responseCode = responseEntity.getStatusCode();
				Log.d("GET Task on", url+" " + responseCode.toString());
				return aisleItems;
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
				Log.i("GET-items-task", "Got aisle Items: " + aisleItemDtos.length);
				aisleItemsAdapter.clear();
				aisleItemsAdapter.addAll(aisleItemDtos);
				aisleItemsAdapter.notifyDataSetChanged();
		        Log.i("CUSTOM_LIST_VIEW", "Items after populating: " + this.listViewAisleItems.getAdapter().getCount());
			}
		}
	}
}