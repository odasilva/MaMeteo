package com.esgi.mameteo;

import java.util.ArrayList;

import BDD.WeatherBDD;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FavoriteActivity extends Activity{

	private WeatherBDD weatherBDD;
	private ListView listview;
	private ArrayList<String> favorites;
	private ArrayAdapter adapter;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		
		listview  = (ListView) findViewById(R.id.listview);
		
		weatherBDD = new WeatherBDD(this);
		weatherBDD.open();
		favorites = weatherBDD.getFavorite();
		weatherBDD.close();
		
		adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, favorites);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
                          
            String itemValue = (String) listview.getItemAtPosition(position);
                
            intent = new Intent(listview.getContext(), MaMeteoActivity.class);
     		intent.putExtra("Countrie", itemValue);
     		startActivity(intent);
     		
            }

       }); 
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			 
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				new AlertDialog.Builder(listview.getContext())
			    .setTitle(getResources().getString(R.string.titleDialogFavorite))
			    .setMessage(getResources().getString(R.string.messageDialogFavorite))
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	
			        	String itemValue = (String) listview.getItemAtPosition(position);
			        	weatherBDD.open();
			        	weatherBDD.setFavoriteWeatherWithCountries(itemValue, 0);
			        	weatherBDD.close();
			        	
			        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastFavorite), Toast.LENGTH_SHORT).show();
			        	
			        	finish();
			    		startActivity(getIntent());
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {}
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
				
				return false;
			}

       }); 
	}
	
	
	
}
