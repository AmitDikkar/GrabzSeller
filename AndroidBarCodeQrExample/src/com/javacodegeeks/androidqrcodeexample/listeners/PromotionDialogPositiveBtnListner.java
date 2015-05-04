/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.javacodegeeks.androidqrcodeexample.AddItems;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class PromotionDialogPositiveBtnListner implements OnClickListener {
	final Dialog dialog;
	Context context;
	String upcCode;
	String selectedAisle;
	String outletId;
	boolean onPromo;
	
	public PromotionDialogPositiveBtnListner(Dialog dialog, Context context, String upcCode, String selectedAisle, String outletId, boolean onPromo) {
		this.dialog = dialog;
		this.context = context;
		this.upcCode = upcCode;
		this.selectedAisle = selectedAisle;
		this.outletId = outletId;
		this.onPromo = onPromo;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		EditText input = (EditText) dialog.findViewById(1);
		String regPrice = input.getText().toString().trim();
		
		EditText input2;
		String promoPrice = "0";
		
		if(onPromo){
			input2 = (EditText) dialog.findViewById(2);
			promoPrice = input2.getText().toString().trim();
		}
		
		PostScannedItemTask task = new PostScannedItemTask();
		task.execute(regPrice, promoPrice);
	}
	
	public class PostScannedItemTask extends AsyncTask<String, Void, AisleItemDto>{

		HttpStatus responseCode;
		Context appContext;
		private ProgressDialog dialogProgress;
		
		public PostScannedItemTask() {
			dialogProgress = new ProgressDialog(context);
			dialogProgress.setMessage("Loading");
			dialogProgress.setIndeterminate(true);
			dialogProgress.setCancelable(false);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogProgress.show();
		}
		
		@Override
		protected AisleItemDto doInBackground(String... params) {
		/*	String outletId = params[0];
			String selectedAisle = params[1];
			String upcCode = params[2];
			boolean promo = Boolean.valueOf(params[3]);
			double price = Double.valueOf(params[4]);
			double promoPrice = 0;*/
			
			String regPrice = params[0];
			String promoPrice = params[1];
			
			String url ;
			if (onPromo)
				url = "http://grabztestenv.elasticbeanstalk.com//seller/outlets/"+outletId+"/aisles/"+ selectedAisle+"/items/"+upcCode+"?onPromotion="+onPromo+"&price="+regPrice+"&promoPrice="+promoPrice;
			else 
				url = "http://grabztestenv.elasticbeanstalk.com//seller/outlets/"+outletId+"/aisles/"+ selectedAisle+"/items/"+upcCode+"?onPromotion="+onPromo+"&price="+regPrice;
			
			Log.i("PostScannedItemTask - doInBackground", "url received: " + url);
			AisleItemDto aisleItemDto = sendPostRequest(url);
			return aisleItemDto;
		}

		@Override
		protected void onPostExecute(AisleItemDto aisleItemDto) {
			dialogProgress.dismiss();
			if(aisleItemDto == null){
				Toast toast = Toast.makeText(context, "Sorry, we couldn't find this item in our database. Please contact Team Grabz.", Toast.LENGTH_LONG);
				toast.show();
				return;
			}
			else{
				Toast toast = Toast.makeText(context, "Item \"" + aisleItemDto.getAisleItem().getName() + "\" has been added.", Toast.LENGTH_LONG);
				toast.show();
				AddItems.aisleItemDtos.add(aisleItemDto);
				AddItems.aisleItemsAdapter.notifyDataSetChanged();
			}
			dialog.dismiss();
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
	
	/*public class SetPromotionTask extends AsyncTask<String, Void, AisleItemDto>{

		@Override
		protected AisleItemDto doInBackground(String... params) {
			String url = params[0];
			double newPrice = Double.parseDouble(params[1]);
			
			RestManager manager = new RestManager();
			AisleItemDto newAisleItemDto = manager.promotion(url, true, newPrice, null);
			return newAisleItemDto;
		}
		
		@Override
		protected void onPostExecute(AisleItemDto aisleItemDto) {
			if(aisleItemDto != null){
				Promotions.aisleItemsOnPromotion.add(aisleItemDto);
				Promotions.aisleItemsPromotionsAdapter.notifyDataSetChanged();
				Toast.makeText(context, "Item added to Promotion.", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(context, "Sorry could not add item on promotion.", Toast.LENGTH_SHORT).show();
			}
			dialog.dismiss();
		}
	}*/
}
