/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.List;

import com.javacodegeeks.adapters.AisleItemsPromotionsAdapter;
import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleSelectedOnPromotions;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class Promotions extends Activity {

	Spinner spinnerPromotionsAisleNames;
	ArrayAdapter<String> spinnerAdapter;
	ListView listViewPromotions;
	AisleItemsPromotionsAdapter aisleItemsPromotionsAdapter;
	
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
		String outletId = settings.getString("outletId", null);

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
	}
	
	private void populateAisleNames(String outletId) {
		GetAisleNamesTask task = new GetAisleNamesTask(this);
		task.execute(outletId);
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
