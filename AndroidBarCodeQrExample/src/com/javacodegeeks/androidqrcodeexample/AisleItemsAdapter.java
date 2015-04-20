/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import com.javacodegeeks.pojo.AisleItemDto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Amit
 *
 */
public class AisleItemsAdapter extends ArrayAdapter<AisleItemDto> {

	Context context;
	int layoutResourceId;   
	AisleItemDto data[] = null;
	
	public AisleItemsAdapter(Context context, int layoutResourceId, AisleItemDto[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	static class AisleItemHolder{
		TextView txtTitle;
	}
	
	@Override
	public View  getView(int position, View convertView, ViewGroup parent) {
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
       
        AisleItemDto aisleItemDto = data[position];
        holder.txtTitle.setText(aisleItemDto.getAisleItem().getName());
        //holder.imgIcon.setImageResource(weather.icon);
       
        return row;
	}
}
