package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//import com.javacodegeeks.androidqrcodeexample.Promotions.PostScannedPtomotionalItemTask;
import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleItemDeleteClickListener;
import com.javacodegeeks.androidqrcodeexample.listeners.PromotionDialogNegativeBtnListner;
import com.javacodegeeks.androidqrcodeexample.listeners.PromotionDialogPositiveBtnListner;
import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleItemDeleteClickListener.DeleteAisleItemTask;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.DeleteAisleItemResponse;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddItems extends Activity implements OnItemSelectedListener{
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
	String outletId;
	private static final String PREFS_NAME = "MyPrefsFile";
	private static Context parentCtx;
	Spinner spinner;
	private boolean promo;
	public static String selectedAisleItem;
	ListView listViewAisleItems;
	public static List<AisleItemDto> aisleItemDtos = new ArrayList<AisleItemDto>();
	public static AisleAdapter aisleItemsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_items);
		parentCtx = this;
		this.spinner = (Spinner) findViewById(R.id.spinner_aisleNamesNew);
		this.spinner.setOnItemSelectedListener(this);
		//retrieve outletId from the shared preferences.
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		this.outletId = settings.getString("outletId", null);
		
		//populate aisle names for this outlet.
		populateSpinner();
		//Create the list view to display items of the aisle.
		this.listViewAisleItems = (ListView)findViewById(R.id.idListViewAisleItemsNew);
		this.aisleItemsAdapter = new AisleAdapter(this, aisleItemDtos);
        this.listViewAisleItems.setAdapter(aisleItemsAdapter);
        
        CheckBox promoCB = (CheckBox) findViewById(R.id.pormoCB);
        promo = promoCB.isChecked();
        promoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	Button scanBtn = (Button) findViewById(R.id.scanner2New);
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            	promo = isChecked;
            	if (promo){
            		(new GetPromotionalItemsTask()).execute(selectedAisleItem);
            		scanBtn.setText("Scan Promotional Item");
            	}
            	else{
            		GetItemsTask task = new GetItemsTask(parentCtx);
            		task.execute(outletId, selectedAisleItem);
            		scanBtn.setText("Scan Item");
            	}
            }
        });

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
			showDialog(AddItems.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	//on click listener for QR code scanning.
	public void scanQR() {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(AddItems.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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
				showPromotionsFormDialog(this, upcCode, selectedAisle, outletId, promo);
			}
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_items, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
		case R.id.logout:
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("outletId", null);
			// Commit the edits!
			editor.commit();
			finish();
			return true;
		case R.id.manage_ailse:
			startActivity(new Intent(getApplicationContext(), ManageAisles.class));
			return true;
		case R.id.scan_qr:
			scanQR();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
            long id) {
        selectedAisleItem = parent.getItemAtPosition(position).toString();
        displayAisleItems(selectedAisleItem);
    }
    private void displayAisleItems(String selectedAisleItem) {
    	if (promo){
    		(new GetPromotionalItemsTask()).execute(selectedAisleItem);
    	}
    	else{
    		GetItemsTask task = new GetItemsTask(this);
    		task.execute(this.outletId, selectedAisleItem);
    	}
	}

    /**
	 * To send POST request to add scanned item in database.
	 * @author Amit
	 *
	 */

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
	
	
	public class GetItemsTask extends AsyncTask<String, Void, AisleItemDto[]>{

		Context appContext;
		
		private HttpStatus responseCode;
		ListView listViewAisleItems;
		
		public GetItemsTask(Context appContext){
			this.appContext = appContext;
//			this.listViewAisleItems = listViewAisleItems;
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
//		        Log.i("CUSTOM_LIST_VIEW", "Items after populating: " + this.listViewAisleItems.getAdapter().getCount());
			}
		}
	}
	public static class AisleAdapter extends ArrayAdapter<AisleItemDto> {
        public AisleAdapter(Context ctx, List<AisleItemDto> itemList) {
            super(ctx, R.layout.listview_item_row, itemList);
        }

        public View getView(final int position, View convertedView, final ViewGroup parent) {
        	View v = convertedView;
            LayoutInflater inflater = (LayoutInflater) parentCtx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview_item_row, parent, false);
            final AisleItemDto aisleItemDto = aisleItemDtos.get(position);
            TextView tv = (TextView) v.findViewById(R.id.txtTitle);
            tv.setText(aisleItemDto.getAisleItem().getName());
            if (aisleItemDto.getAisleItem().getOnPromotion())
            	tv.setTextColor(Color.RED);
            ImageButton btn = (ImageButton) v.findViewById(R.id.btnDeleteAisleItem);
            
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	String deleteUrl = null;
                    String getUrl = null;
                    
                    for (LinkDto link : aisleItemDto.getLinks()){
                    	if(link.getRel().equals("delete-item")){
                    		deleteUrl = "http://grabztestenv.elasticbeanstalk.com" + link.getHref();
                    	}
                    	else if(link.getRel().equals("view-item")){
                    		getUrl = "http://grabztestenv.elasticbeanstalk.com" + link.getHref();
                    	}
                    }
                	(new DeleteAisleItemTask()).execute(deleteUrl,String.valueOf(position));
                }
            });
            return v;
        }
	}
	
	private static class DeleteAisleItemTask extends AsyncTask<String, Void, DeleteAisleItemResponse>{
		int position;
		@Override
		protected DeleteAisleItemResponse doInBackground(String... params) {
			String url = params[0];
			position = Integer.parseInt(params[1]);
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
				Toast.makeText(parentCtx, result.getMessage(), Toast.LENGTH_SHORT).show();;
				
				//since our operation is succesful we will remove it from the list.
				aisleItemDtos.remove(position);
				aisleItemsAdapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(parentCtx, "Sorry, Item can not be deleted at this time.", Toast.LENGTH_LONG).show();;
			}
		}
	}
        
	public void showPromotionsFormDialog(Context parentContext, String upcCode, String selectedAisle, String outletId, boolean onPromo){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Add To Promotion.");
        b.setCancelable(false);
        
	/*	final TextView textView = new TextView(this);
		String itemName = aisleItemDto.getAisleItem().getName();
		textView.setText(itemName);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);*/
		
		final EditText price = new EditText(this);
		price.setHint("Enter Regular Price...");
		price.setId(1);
		price.setInputType(InputType.TYPE_CLASS_NUMBER);
		price.setGravity(Gravity.CENTER_HORIZONTAL);
		price.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
		
		LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        //lay.addView(textView);
        lay.addView(price);
        
        double promoPriceOut = 0;
		
			final EditText promoPrice = new EditText(this);
			promoPrice.setHint("Enter Promotion Price...");
			promoPrice.setId(2);
			promoPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
			promoPrice.setGravity(Gravity.CENTER_HORIZONTAL);
			promoPrice.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
			
			//lay.addView(promoPrice);
		//	promoPriceOut = Double.valueOf(promoPrice.getText().toString()); 
		
			if(onPromo){
				lay.addView(promoPrice);
			}
		
		b.setView(lay);
	/*	LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        //lay.addView(textView);
        lay.addView(price);
        b.setView(lay);*/

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
		
		//double regPrice = Double.valueOf(price.getText().toString());
		
		//attach onClick listener to OK button
		Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		okButton.setOnClickListener(new PromotionDialogPositiveBtnListner(alertDialog, this, upcCode, selectedAisle, outletId, onPromo));
		
		//attach onClick listener to Cancel button
		Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		//cancelButton.setOnClickListener(new PromotionDialogNegativeBtnListner(alertDialog, this, aisleItemDto));
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
	
	public class GetPromotionalItemsTask extends AsyncTask<String, Void, AisleItemDto[]>{

		@Override
		protected AisleItemDto[] doInBackground(String... params) {
			String aisleName = params[0];
			RestManager manager = new RestManager();
			AisleItemDto[] aisleItemDtos = manager.getPromotionalItems(outletId, aisleName);
			return aisleItemDtos;
		}
		
		@Override
		protected void onPostExecute(AisleItemDto[] aisleItemDtos) {
			if(aisleItemDtos != null){
				aisleItemsAdapter.clear();
				aisleItemsAdapter.addAll(aisleItemDtos);
				aisleItemsAdapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(parentCtx, "Error while retriving promotional items.", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
