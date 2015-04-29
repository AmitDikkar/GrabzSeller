/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample.listeners;

import com.javacodegeeks.adapters.AisleItemsPromotionsAdapter;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.rest.RestManager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class OnAisleSelectedOnPromotions implements OnItemSelectedListener {

	Context context;
	String outletId;
	AisleItemsPromotionsAdapter adapter;

	public OnAisleSelectedOnPromotions(Context context, String outletId, AisleItemsPromotionsAdapter adapter){
		this.context = context;
		this.outletId = outletId;
		this.adapter = adapter;
	}
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String aisleName = parent.getItemAtPosition(position).toString();
		Toast.makeText(context, "Aisle selected : " + aisleName, Toast.LENGTH_SHORT).show();
		GetPromotionalItemsTask task = new GetPromotionalItemsTask();
		task.execute(aisleName);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	public class GetPromotionalItemsTask extends AsyncTask<String, Void, AisleItemDto[]>{

		@Override
		protected AisleItemDto[] doInBackground(String... params) {
			String aisleName = params[0];
			RestManager manager = new RestManager();
			AisleItemDto[] aisleItemDtos = manager.getPromotionalItems(outletId, aisleName);
			return aisleItemDtos;
		}
		
		@Override
		protected void onPostExecute(AisleItemDto[] aisleItemDtos) {
			if(aisleItemDtos != null){
				Toast.makeText(context, "# promotional items:" + aisleItemDtos.length, Toast.LENGTH_SHORT).show();
				adapter.clear();
				adapter.addAll(aisleItemDtos);
				adapter.notifyDataSetChanged();
			}
			else{
				Toast.makeText(context, "Error while retriving promotional items.", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
