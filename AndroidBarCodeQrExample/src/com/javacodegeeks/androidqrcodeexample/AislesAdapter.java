/**
 * 
 */
package com.javacodegeeks.androidqrcodeexample;

import java.util.List;

import com.javacodegeeks.androidqrcodeexample.listeners.OnAisleDeleteClickListener;
import com.javacodegeeks.pojo.AisleNameDto;
import com.javacodegeeks.pojo.LinkDto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Amit
 *
 */
public class AislesAdapter extends ArrayAdapter<AisleNameDto>{

	private int layoutResourceId;
	private Context context;
	private List<AisleNameDto> data;

	public AislesAdapter(Context context, int layoutResourceId, List<AisleNameDto> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	static class AisleHolder{
		TextView txtTitle;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		AisleHolder aisleHolder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			aisleHolder = new AisleHolder();
			//holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
			aisleHolder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

			row.setTag(aisleHolder);
		}
		else
		{
			aisleHolder = (AisleHolder)row.getTag();
		}

		AisleNameDto aisleNameDto = data.get(position);
		aisleHolder.txtTitle.setText(aisleNameDto.getAisleName());
		String deleteUrl = null;
		for(LinkDto linkDto : aisleNameDto.getLinks()){
			if(linkDto.getMethod().equals(new String("DELETE"))){
				deleteUrl = linkDto.getHref();
			}
		}
		
		//holder.imgIcon.setImageResource(weather.icon);
		ImageButton deleteButton = (ImageButton) row.findViewById(R.id.btnDeleteAisleItem);
		deleteButton.setOnClickListener(new OnAisleDeleteClickListener(deleteUrl, context, position));

		return row;
	}
}
