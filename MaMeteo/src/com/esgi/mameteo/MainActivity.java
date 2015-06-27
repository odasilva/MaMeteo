package com.esgi.mameteo;

import java.util.ArrayList;
import java.util.List;

import modele.Weather_Data;
import BDD.WeatherBDD;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner spinner;
	private List<String> countries;
	private List<Weather_Data> weather_datas;
	private ArrayAdapter adapter;
	private Intent intent;
	private WeatherBDD weatherBdd;
	private MenuInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) findViewById(R.id.buttonFav);
		btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.star));
		
		weatherBdd = new WeatherBDD(this);
		weatherBdd.open();
		
		spinner = (Spinner) findViewById(R.id.spinner);
		countries = new ArrayList<String>();
		weather_datas = weatherBdd.getWeathers();
		weatherBdd.close();
		for(int i = 0; i < weather_datas.size(); i++){
			countries.add(weather_datas.get(i).getCountrie());
		}

		adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, countries);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		weatherBdd = new WeatherBDD(this);
		weatherBdd.open();
		
		spinner = (Spinner) findViewById(R.id.spinner);
		countries = new ArrayList<String>();
		weather_datas = weatherBdd.getWeathers();
		weatherBdd.close();
		for(int i = 0; i < weather_datas.size(); i++){
			countries.add(weather_datas.get(i).getCountrie());
		}

		adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, countries);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	public void onClickConsult(View v){
		intent = new Intent(this, MaMeteoActivity.class);
		intent.putExtra("Countrie", spinner.getSelectedItem().toString());	
		startActivity(intent);
	}
	
	public void onClickFavorite(View v){
		intent = new Intent(this, FavoriteActivity.class);
		startActivity(intent);
	}

	
	 public boolean onCreateOptionsMenu(Menu menu) {
		 
	        inflater = getMenuInflater();
	        inflater.inflate(R.layout.menu, menu);
	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	        searchView.setSearchableInfo(searchManager
	                .getSearchableInfo(getComponentName()));
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
									weatherBdd.open();
									long error = weatherBdd.insertWeather(new Weather_Data(city));
									weatherBdd.close();
									
									if(error != 0)
										 Toast.makeText(MainActivity.this,city+" "+getResources().getString(R.string.succes), Toast.LENGTH_LONG).show();
									else
										 Toast.makeText(MainActivity.this,getResources().getString(R.string.fail_add_city), Toast.LENGTH_LONG).show();
									
									
									weatherBdd = new WeatherBDD(MainActivity.this);
									weatherBdd.open();
									
									spinner = (Spinner) findViewById(R.id.spinner);
									countries = new ArrayList<String>();
									weather_datas = weatherBdd.getWeathers();
									weatherBdd.close();
									for(int i = 0; i < weather_datas.size(); i++){
										countries.add(weather_datas.get(i).getCountrie());
									}

									adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item, countries);
									adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
									spinner.setAdapter(adapter);
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
		                Toast.makeText(MainActivity.this,getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.version)+" : "+ pInfo.versionName, Toast.LENGTH_LONG).show();
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
	                return true;
	            case R.id.search:
	            	
	            	return true;
	           case R.id.leaveItem:
	               finish();
	               return true;
	         }
	         return false;
	     }
}
