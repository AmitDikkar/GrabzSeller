/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import com.javacodegeeks.androidqrcodeexample.R.string;

import android.app.Activity;
import android.os.Bundle;
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
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_manage_aisles);
	    
	    this.editTextAddAisle = (EditText) findViewById(R.id.idEditTextAddAisle);
	    
	    this.listViewAisles = (ListView)findViewById(R.id.idListViewAislesOnManageAisles);
		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        this.listViewAisles.addHeaderView(header);
	}

	//on click listener for add aisle button.
	public void addAisle(View v){
		String textEntered = this.editTextAddAisle.getText().toString();
		if(textEntered.equals("") || textEntered == null){
			Toast.makeText(this, R.string.manage_aisles_aisle_number_missing_message, Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, "Text Entered:" + textEntered, Toast.LENGTH_SHORT).show();
		}
	}
}
