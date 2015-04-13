package com.javacodegeeks.androidqrcodeexample;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SellerLoginActivity extends Activity {

	EditText editTextOutletId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_login);
		this.editTextOutletId = (EditText) findViewById(R.id.idEditTextOutletId);
	}
	
	//on click listener for authenticate button.
	public void authenticateSeller(View v){
		String outletIdText = this.editTextOutletId.getText().toString();
		if(outletIdText == null || outletIdText.equals("")){
			Toast tost = Toast.makeText(getApplicationContext(), "Please enter outlet Id.", Toast.LENGTH_SHORT);
			tost.show();
		}
		else{
			Toast tost = Toast.makeText(getApplicationContext(), "Text Entered is:" + outletIdText, Toast.LENGTH_SHORT);
			tost.show();
			
		}
	}
	
	
}
