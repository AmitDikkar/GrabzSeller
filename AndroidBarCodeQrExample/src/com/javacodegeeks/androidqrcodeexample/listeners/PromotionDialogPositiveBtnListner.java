/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import com.javacodegeeks.androidqrcodeexample.Promotions;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
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
	AisleItemDto aisleItemDto;
	
	public PromotionDialogPositiveBtnListner(Dialog dialog, Context context, AisleItemDto aisleItemDto) {
		this.dialog = dialog;
		this.context = context;
		this.aisleItemDto = aisleItemDto;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		EditText input = (EditText) dialog.findViewById(1);
		String priceEntered = input.getText().toString().trim();
		
		String putUrl = null;
		for(LinkDto link : aisleItemDto.getLinks()){
			if(link.getRel().equals("set-remove-promotion")){
				putUrl = link.getHref();
			}
		}
		
		SetPromotionTask task = new SetPromotionTask();
		task.execute(putUrl, priceEntered);
	}
	
	public class SetPromotionTask extends AsyncTask<String, Void, AisleItemDto>{

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
	}
}
