package com.esgi.mameteo;

import java.util.ArrayList;

import modele.Weather_Data;

import BDD.WeatherBDD;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class FavoriteActivity extends Activity{

	private WeatherBDD weatherBDD;
	private ListView listview;
	private ArrayList<String> favorites;
	private ArrayAdapter adapter;
	private Intent intent;
	private MenuInflater inflater;
	
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
	
	
	
	 public boolean onCreateOptionsMenu(Menu menu) {
		 
	        inflater = getMenuInflater();
	        inflater.inflate(R.layout.menu, menu);
	 	 
	        return true;
	     }
	 
	      public boolean onOptionsItemSelected(MenuItem item) {
	      
	    	  switch (item.getItemId()) {
	            case R.id.favoriteItem:
	            	intent = new Intent(this, FavoriteActivity.class);
	            	startActivity(intent);
	               return true;
	            case R.id.addCityItem:
					AlertDialog.Builder AddCityBox;
					final EditText input = new EditText(this);
					AddCityBox = new AlertDialog.Builder(this);
					AddCityBox.setView(input);
					AddCityBox.setTitle(getResources().getString(R.string.txtaddCityItem));
					//AddCityBox.setIcon(R.drawable.ic_launcher);
					AddCityBox.setMessage(getResources().getString(R.string.txtaddCityItem));
					AddCityBox.setPositiveButton(getResources().getString(R.string.save),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									String city = input.getText().toString();
									weatherBDD.open();
									long error = weatherBDD.insertWeather(new Weather_Data(city));
									weatherBDD.close();
									
									if(error != 0)
										 Toast.makeText(FavoriteActivity.this,city+" "+getResources().getString(R.string.succes), Toast.LENGTH_LONG).show();
									else
										 Toast.makeText(FavoriteActivity.this,getResources().getString(R.string.fail_add_city), Toast.LENGTH_LONG).show();
										
								}
							});
					AddCityBox.setNegativeButton(getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {}
							});
					AddCityBox.show();
	                return true;
	            case R.id.aboutItem:
					PackageInfo pInfo;
					try {
						pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		                Toast.makeText(FavoriteActivity.this,getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.version)+" : "+ pInfo.versionName, Toast.LENGTH_LONG).show();
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
	                return true;
	           case R.id.leaveItem:
	               finish();
	               return true;
	         }
	         return false;
	     }	
	
}
