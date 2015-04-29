/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.javacodegeeks.androidqrcodeexample.AndroidBarcodeQrExample;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.DeleteAisleItemResponse;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.rest.RestManager;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class PromotionDialogNegativeBtnListner implements OnClickListener {

	final Dialog dialog;
	Context context;
	AisleItemDto aisleItemDto;
	
	public PromotionDialogNegativeBtnListner(Dialog dialog, Context context, AisleItemDto aisleItemDto){
		this.dialog = dialog;
		this.context = context;
		this.aisleItemDto = aisleItemDto;
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		String url = null;
		for(LinkDto link : aisleItemDto.getLinks()){
			if(link.getRel().equals("delete-item")){
				url = link.getHref();
			}
		}
		DeleteTempAisleItemTask task = new DeleteTempAisleItemTask();
		task.execute(url);
	}
	
	public class DeleteTempAisleItemTask extends AsyncTask<String, Void, DeleteAisleItemResponse>{

		@Override
		protected DeleteAisleItemResponse doInBackground(String... params) {
			String url = params[0];
			try{
				RestManager manager = new RestManager();
				DeleteAisleItemResponse response = manager.deleteAisleItem(url);
				return response;
			}
			catch (Exception e){
				Log.e("DELETE_Aisle_item: Put request on:", url + " Failed.");
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(DeleteAisleItemResponse result) {
			if(result != null){
				Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(context, "Sorry, Item can not be deleted at this time.", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}
	}

}
