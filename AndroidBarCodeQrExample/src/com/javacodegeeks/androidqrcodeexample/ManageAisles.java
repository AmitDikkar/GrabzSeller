/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.List;



import com.javacodegeeks.pojo.AisleNameDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class ManageAisles extends Activity {

	EditText editTextAddAisle;
	ListView listViewAisles;

	public static List<AisleNameDto> aisleNameDtos = new ArrayList<AisleNameDto>();
	final String PREFS_NAME = "MyPrefsFile";
	public static AislesAdapter aislesAdapter;
	AisleItemsAdapter aisleItemsAdapter;
	
	String outletId;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_manage_aisles);
	    
	    this.editTextAddAisle = (EditText) findViewById(R.id.idEditTextAddAisle);
	 
	    //retrieve outletId from the shared preferences.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        this.outletId = settings.getString("outletId", null);

        listViewAisles = (ListView)findViewById(R.id.idListViewManageAisles);
        aislesAdapter = new AislesAdapter(this, R.layout.listview_item_row, aisleNameDtos);
        listViewAisles.setAdapter(aislesAdapter);
        
        populateAislesList(outletId);
	}

	//on click listener for add aisle button.
	public void addAisle(View v){
		String textEntered = this.editTextAddAisle.getText().toString();
		if(textEntered.equals("") || textEntered == null){
			Toast.makeText(this, R.string.manage_aisles_aisle_number_missing_message, Toast.LENGTH_SHORT).show();
			populateAislesList(outletId);
		}
		else{
			//remove leading and preceding spaces.
			String newAisleName = textEntered.trim();
			Toast.makeText(this, "Text Entered:" + newAisleName, Toast.LENGTH_SHORT).show();
			PostNewAisleTask task = new PostNewAisleTask(this);
			task.execute(this.outletId, newAisleName);
			this.editTextAddAisle.setText("");
		}
	}

	private void populateAislesList(String outletId) {
		GetAislesTask task = new GetAislesTask(this);
		task.execute(outletId);
	}

	public class PostNewAisleTask extends AsyncTask<String, Void, AisleNameDto>{

		private Context context;
		public PostNewAisleTask(Context context){
			this.context = context;
		}
		
		@Override
		protected AisleNameDto doInBackground(String... params) {
			String outletId = params[0];
			String aisleName = params[1];
			RestManager manager = new RestManager();
			AisleNameDto aisleNameDto = manager.postNewAisle(outletId, aisleName);
			return aisleNameDto;
		}
		
		@Override
		protected void onPostExecute(AisleNameDto aisleNameDto) {
			if(aisleNameDto != null){
				Toast.makeText(context, "Posted Aisle new", Toast.LENGTH_SHORT).show();
				aisleNameDtos.add(aisleNameDto);
				aislesAdapter.notifyDataSetChanged();
				Toast.makeText(context, "Posted Aisle new - new Size: " + aislesAdapter.getCount() , Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(context, "Error: Posted Aisle new", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class GetAislesTask extends AsyncTask<String, Void, AisleNameDto[]>{

		private Context context;
		
		public GetAislesTask(Context context){
			this.context = context;
		}

		@Override
		protected AisleNameDto[] doInBackground(String... params) {
			String outletId = params[0];
			RestManager restManager = new RestManager();
			return restManager.getAisles(outletId);
		}

		@Override
		protected void onPostExecute(AisleNameDto[] aisleNames) {
			if(aisleNames != null){
				Log.i("GETAisles-Onpostexecute", "aisleNames size: " + aisleNames.length);
				aislesAdapter.clear();
		        aislesAdapter.addAll(aisleNames);
				aislesAdapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(this.context, "Unexpected error ocurred while receiving Aisles.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
