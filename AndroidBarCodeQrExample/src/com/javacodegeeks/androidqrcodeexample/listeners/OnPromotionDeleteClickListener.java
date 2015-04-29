/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import com.javacodegeeks.adapters.AisleItemsPromotionsAdapter;
import com.javacodegeeks.androidqrcodeexample.Promotions;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.rest.RestManager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class OnPromotionDeleteClickListener implements OnClickListener {

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	String url;
	Context context;
	int position;
	AisleItemsPromotionsAdapter adapter;
	
	public OnPromotionDeleteClickListener (String url, Context context, AisleItemsPromotionsAdapter adapter, int position){
		this.url = url;
		this.context = context;
		this.position = position;
		this.adapter = adapter;
	}
	
	@Override
	public void onClick(View v) {
		PutRemoveAisleItemPromotionTask task = new PutRemoveAisleItemPromotionTask();
		task.execute();
	}

	public class PutRemoveAisleItemPromotionTask extends AsyncTask<String, Void, AisleItemDto>{

		@Override
		protected AisleItemDto doInBackground(String... params) {
			RestManager manager = new RestManager();
			AisleItemDto aisleItemDto = manager.promotion(url, false, 0, null);
			return aisleItemDto;
		}
		
		@Override
		protected void onPostExecute(AisleItemDto aisleItemDto) {
			if(aisleItemDto != null){
				Toast.makeText(context, "Item '" + aisleItemDto.getAisleItem().getName() + "' removed successfully.", Toast.LENGTH_SHORT).show();
				Promotions.aisleItemsOnPromotion.remove(position);
				adapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(context, "Item cannot be removed at this time.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
