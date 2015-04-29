/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import org.springframework.http.HttpStatus;

import com.javacodegeeks.androidqrcodeexample.ManageAisles;
import com.javacodegeeks.pojo.AisleNameDto;
import com.javacodegeeks.pojo.DeleteAisleItemResponse;
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
public class OnAisleDeleteClickListener implements OnClickListener {

	private String url;
	private Context context;
	int position;
	
	public OnAisleDeleteClickListener(String url, Context context, int position){
		this.url = url;
		this.context = context;
		this.position = position;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		DeleteAisleTask task = new DeleteAisleTask();
		task.execute(url);
		Toast.makeText(context, "delete clicked for position: " + this.position, Toast.LENGTH_SHORT).show();
	}
	
	public class DeleteAisleTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			RestManager manager = new RestManager();
			HttpStatus statusCode = manager.deleteAisle(url);
			if(statusCode.equals(HttpStatus.OK)){
				return "success";
			}
			else {
				return "failed";
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("success")){
				ManageAisles.aisleNameDtos.remove(position);
				ManageAisles.aislesAdapter.notifyDataSetChanged();
				Toast.makeText(context, "Aisle Has been deleted Succesfully", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(context, "Aisle Has been deleted Succesfully", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
