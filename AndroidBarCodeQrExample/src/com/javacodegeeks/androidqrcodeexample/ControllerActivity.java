/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class ControllerActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_controller);
	    // TODO Auto-generated method stub
	}

	//on click listener for Manage Aisle Items button.
	public void manageAisleItems(View v){
		Toast.makeText(this, "Manage Aisle Items button click", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(getApplicationContext(), AndroidBarcodeQrExample.class));
	}
	
	//on click listener for Aisle Management
	public void btnAisleManagement(View v){
		Toast.makeText(this, "Aisle Management button clicked.", Toast.LENGTH_SHORT).show();
		startActivity(new Intent(getApplicationContext(), ManageAisles.class));
	}
	
	//on click listener for Promotions.
	public void btnPromotions(View v){
		Toast.makeText(this, "Promotions button clicked.", Toast.LENGTH_SHORT).show();
	}
}
