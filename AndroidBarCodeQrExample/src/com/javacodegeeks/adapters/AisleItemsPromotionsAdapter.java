/**
 * 
 */
package com.javacodegeeks.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.javacodegeeks.androidqrcodeexample.Promotions;
import com.javacodegeeks.androidqrcodeexample.R;
import com.javacodegeeks.androidqrcodeexample.listeners.OnPromotionDeleteClickListener;
import com.javacodegeeks.pojo.AisleItemDto;
import com.javacodegeeks.pojo.LinkDto;
import com.javacodegeeks.pojo.LinksDto;

/**
 * @author Amit
 *
 */
public class AisleItemsPromotionsAdapter extends ArrayAdapter<AisleItemDto>{
	
	Context context;
	int layoutResourceId;
	
	public AisleItemsPromotionsAdapter(Context context, int layoutResourceId, List<AisleItemDto> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
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
       
        Log.i("AisleItemsPromotionsAdapter", "# Promotional items:" + Promotions.aisleItemsOnPromotion.size());
        
        AisleItemDto aisleItemDto = Promotions.aisleItemsOnPromotion.get(position);
        holder.txtTitle.setText(aisleItemDto.getAisleItem().getName());
        ImageButton deleteButton = (ImageButton) row.findViewById(R.id.btnDeleteAisleItem);
        //holder.imgIcon.setImageResource(weather.icon);
        String putUrl = null;
        for(LinkDto link : aisleItemDto.getLinks()){
        	if(link.getMethod().equals("PUT")){
        		putUrl = link.getHref();
        	}
        }
        deleteButton.setOnClickListener(new OnPromotionDeleteClickListener(putUrl, context,this, position));
        return row;
	}

}
