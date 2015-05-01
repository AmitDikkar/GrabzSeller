/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class ControllerActivity extends Activity {

	private static final String PREFS_NAME = "MyPrefsFile";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_controller);
	    setTitle("Welcome!!");
	}

	//on click listener for Manage Aisle Items button.
	public void manageAisleItems(View v){
//		startActivity(new Intent(getApplicationContext(), AndroidBarcodeQrExample.class));
		startActivity(new Intent(getApplicationContext(), AddItems.class));
	}
	
	//on click listener for Aisle Management
	public void btnAisleManagement(View v){
		startActivity(new Intent(getApplicationContext(), ManageAisles.class));
	}
	
	//on click listener for Promotions.
	public void btnPromotions(View v){
		startActivity(new Intent(getApplicationContext(), Promotions.class));
	}
	
	public void btnLogout(View v){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("outletId", null);
		// Commit the edits!
		editor.commit();
		finish();
	}
}
