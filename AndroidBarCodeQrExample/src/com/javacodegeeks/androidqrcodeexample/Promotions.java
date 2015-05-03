/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.javacodegeeks.adapters.AisleItemsPromotionsAdapter;
import com.javacodegeeks.androidqrcodeexample.AndroidBarcodeQrExample.PostScannedItemTask;
import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleSelectedOnPromotions;
import com.javacodegeeks.androidqrcodeexample.listeners.PromotionDialogNegativeBtnListner;
import com.javacodegeeks.androidqrcodeexample.listeners.PromotionDialogPositiveBtnListner;
import com.javacodegeeks.pojo.AisleItemDto;
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
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class Promotions extends Activity {

	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	Spinner spinnerPromotionsAisleNames;
	ArrayAdapter<String> spinnerAdapter;
	ListView listViewPromotions;
	public static AisleItemsPromotionsAdapter aisleItemsPromotionsAdapter;
	String outletId;
	String dialogResult;
	private static final String PREFS_NAME = "MyPrefsFile";
	
	public static List<AisleItemDto> aisleItemsOnPromotion = new ArrayList<AisleItemDto>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_promotions);
	    setTitle("Manage Promotions");
	    
		//retrieve outletId from the shared preferences.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		this.outletId = settings.getString("outletId", null);

	    this.listViewPromotions = (ListView) findViewById(R.id.idListViewPromotions);
	    //this.aisleItemsPromotionsAdapter = new AisleItemsPromotionsAdapter(this, R.layout.listview_item_row, aisleItemsOnPromotion);
	    this.aisleItemsPromotionsAdapter = new AisleItemsPromotionsAdapter(this, R.layout.listview_item_row, aisleItemsOnPromotion);
	    this.listViewPromotions.setAdapter(aisleItemsPromotionsAdapter);

	    this.spinnerPromotionsAisleNames = (Spinner) findViewById(R.id.spinner_promotions_aisleNames);
	    this.spinnerPromotionsAisleNames.setOnItemSelectedListener(new OnAisleSelectedOnPromotions(this, outletId, aisleItemsPromotionsAdapter));
	    
	    populateAisleNames(outletId);
	}

	//scan QR on click event
	public void scanQR(View v){
		Toast.makeText(this, "scannin qr", Toast.LENGTH_SHORT).show();
	}

	//scan QR on click event
	public void scanBar(View v){
		Toast.makeText(this, "scannin barcode", Toast.LENGTH_SHORT).show();
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(Promotions.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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

	public void showPromotionsFormDialog(AisleItemDto aisleItemDto, Context parentContext){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Add To Promotion.");
        b.setCancelable(false);
        
		final TextView textView = new TextView(this);
		String itemName = aisleItemDto.getAisleItem().getName();
		textView.setText(itemName);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		
		final EditText input = new EditText(this);
		input.setHint("Enter Price...");
		input.setId(1);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setGravity(Gravity.CENTER_HORIZONTAL);
		input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
		
		LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(textView);
        lay.addView(input);
        b.setView(lay);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton)
			{
			}
		});
        
		b.setNegativeButton("CANCEL",  new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton)
			{
			}
		});
		
		AlertDialog alertDialog = b.create();
		alertDialog.show();
		
		//attach onClick listener to OK button
		Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//		okButton.setOnClickListener(new PromotionDialogPositiveBtnListner(alertDialog, this, aisleItemDto));
		
		//attach onClick listener to Cancel button
		Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		cancelButton.setOnClickListener(new PromotionDialogNegativeBtnListner(alertDialog, this, aisleItemDto));
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String upcCode = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				String selectedAisle = String.valueOf(this.spinnerPromotionsAisleNames.getSelectedItem());
				String outletId = this.outletId;

				Toast.makeText(this, "UPC code: " + upcCode + " Formate: " + format, Toast.LENGTH_SHORT).show();

				PostScannedPtomotionalItemTask task = new PostScannedPtomotionalItemTask(this);
				task.execute(outletId, selectedAisle, upcCode);
				try {
					AisleItemDto aisleItemDto = task.get();
					if(aisleItemDto != null){
						showPromotionsFormDialog(aisleItemDto, this);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void populateAisleNames(String outletId) {
		GetAisleNamesTask task = new GetAisleNamesTask(this);
		task.execute(outletId);
	}
	
	public class PostScannedPtomotionalItemTask extends AsyncTask<String, Void, AisleItemDto>{

		Context appContext;
		public PostScannedPtomotionalItemTask(Context context){
			this.appContext = context;
		}
		@Override
		protected AisleItemDto doInBackground(String... params) {
			String outletId = params[0];
			String selectedAisle = params[1];
			String upcCode = params[2];
			String url = "http://grabztestenv.elasticbeanstalk.com//seller/outlets/"+outletId+"/aisles/"+ selectedAisle+"/items/"+upcCode;
			Log.i("PostScannedItemTask - doInBackground", "url received: " + url);
			return sendPostRequest(url);
		}
		
		@Override
		protected void onPostExecute(AisleItemDto aisleItemDto) {
			if(aisleItemDto == null){
				Toast toast = Toast.makeText(this.appContext, "Sorry, we couldn't find this item in our database. Please contact Team Grabz.", Toast.LENGTH_LONG);
				toast.show();
				return;
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
				HttpStatus responseCode = responseEntity.getStatusCode();
				Log.i("PostScannedItemTask - sendPostRequest", "Request Executed with status code : " + responseCode + " and item received is:" + aisleItemDto.getAisleItem().getName());
				return aisleItemDto;
			}
			catch(Exception e){
				return null;
			}
		}
	}
	
	public class GetAisleNamesTask extends AsyncTask<String, Void, String[]>{

		private Context context;

		public GetAisleNamesTask(Context context){
			this.context = context;
		}
		
		@Override
		protected String[] doInBackground(String... params) {
			String outletId = params[0];
			RestManager manager = new RestManager();
			String[] aisleNames = manager.getAisleNames(outletId);
			return aisleNames;
		}

		@Override
		protected void onPostExecute(String[] aisleNames) {
			if(aisleNames != null){
				spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, aisleNames);
				// Specify the layout to use when the list of choices appears
				spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// Apply the adapter to the spinner
				spinnerPromotionsAisleNames.setAdapter(spinnerAdapter);
			}
			else{
				Toast.makeText(context, "Error occurred while retrieving Aisle names.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
