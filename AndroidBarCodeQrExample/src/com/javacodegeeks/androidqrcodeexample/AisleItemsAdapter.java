/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleItemDeleteClickListener;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.LinkDto;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Amit
 *
 */
public class AisleItemsAdapter extends ArrayAdapter<AisleItemDto> {

	Context context;
	int layoutResourceId;   
	//AisleItemDto data[] = null;
	List<AisleItemDto> data = new ArrayList<AisleItemDto>();
	public AisleItemsAdapter(Context context, int layoutResourceId, AisleItemDto[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		
		//conver array to the list.. makes thisngs more easy.
		for(AisleItemDto aisleItemDto : data){
			this.data.add(aisleItemDto);
		}
	}

	static class AisleItemHolder{
		TextView txtTitle;
	}
	
	@Override
	public View  getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AisleItemHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new AisleItemHolder();
            //holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            row.setTag(holder);
        }
        else
        {
            holder = (AisleItemHolder)row.getTag();
        }
       
        AisleItemDto aisleItemDto = data.get(position);
        holder.txtTitle.setText(aisleItemDto.getAisleItem().getName());
        //holder.imgIcon.setImageResource(weather.icon);
        
        ImageButton deleteButton = (ImageButton) row.findViewById(R.id.btnDeleteAisleItem);
        String deleteUrl = null;
        String getUrl = null;
        
        for (LinkDto link : aisleItemDto.getLinks()){
        	if(link.getRel().equals("delete-item")){
        		deleteUrl = "http://grabztestenv.elasticbeanstalk.com" + link.getHref();
        	}
        	else if(link.getRel().equals("view-item")){
        		getUrl = "http://grabztestenv.elasticbeanstalk.com" + link.getHref();
        	}
        }
        deleteButton.setOnClickListener(new OnAisleItemDeleteClickListener(deleteUrl+"?action=delete", context, this, position));
        return row;
	}
	
	public void removeItem(int position){
		Log.i("Refresh List view", "Removing item at " + position);
		//data[position] = new AisleItemDto();
		//data = ArrayUtils.removeElement(data, position);
		//data.remove(position);
		//this.notifyDataSetChanged();
		Log.i("Refresh List view", "Refreshing the list");
	}
	
	@Override
	public void clear() {
		super.clear();
		data = null;
	}
}
