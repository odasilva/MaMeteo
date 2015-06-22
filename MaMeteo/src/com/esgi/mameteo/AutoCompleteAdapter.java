package com.esgi.mameteo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

	private LayoutInflater inflater;
	
	
	public AutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final TextView tv;
		if (convertView != null) {
			tv = (TextView) convertView;
		} else {
			tv = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		}
 
		tv.setText(getItem(position));
		return tv;
	}
	
	
	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(final CharSequence constraint) {
				List<String> citiesList = null;
				if (constraint != null) {
					try {
						//TODO populate citiesList with datas from openweathermap
						
					} catch (IOException e) {
					}
				}
				if (citiesList == null) {
					citiesList = new ArrayList<String>();
				}
 
				final FilterResults filterResults = new FilterResults();
				filterResults.values = citiesList;
				filterResults.count = citiesList.size();
 
				return filterResults;
			}
 
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(final CharSequence contraint, final FilterResults results) {
				clear();
				for (String str: (List<String>) results.values) {
					add(str);
				}
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
 
		/*	@Override
			public CharSequence convertResultToString(final Object resultValue) {
				return resultValue == null ? "" : ((String) resultValue).getAddressLine(0);
			}*/
		};
		return myFilter;
	}

}
