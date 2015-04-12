package com.javacodegeeks.androidqrcodeexample;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AndroidBarcodeQrExample extends Activity {
	/** Called when the activity is first created. */

	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		populateSpinner();
	}

	private void populateSpinner() {
		GetAisleItemsTask task = new GetAisleItemsTask(this);
		task.execute("http://grabztestenv.elasticbeanstalk.com//seller/outlets/b97153fc14/aisles/Aisle:Right/items/");
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
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				 /*(new GetAisleItemsTask())
	                .execute("http://grabztestenv.elasticbeanstalk.com//seller/outlets/b97153fc14/aisles/Aisle:Right/items/");*/
				Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}
	
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
	         if (aisleItems.length != 0 && this.responseCode == HttpStatus.OK) {
	             populateSpinner(aisleItems);
	         }
	     }

		private void populateSpinner(String[] aisleItems) {
			Spinner spinner = (Spinner) findViewById(R.id.spinner_aisleNames);
			// Create an ArrayAdapter using the string array and a default spinner layout
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.appContext, android.R.layout.simple_spinner_item, aisleItems);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);
		}
	}

	
}