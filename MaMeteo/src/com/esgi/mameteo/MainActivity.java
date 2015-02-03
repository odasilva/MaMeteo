package com.esgi.mameteo;

import java.util.ArrayList;
import java.util.List;

import modele.Weather_Data;

import BDD.WeatherBDD;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {

	private Spinner spinner;
	private List countries;
	private List<Weather_Data> weather_datas;
	private ArrayAdapter adapter;
	private Intent intent;
	private WeatherBDD weatherBdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
//		weatherBdd = new WeatherBDD(this);
//		weatherBdd.open();
//
//		weatherBdd.insertWeather(new Weather_Data("Paris,FR"));
//		weatherBdd.insertWeather(new Weather_Data("London,EN"));
//		weatherBdd.insertWeather(new Weather_Data("Madrid,FR"));
//		weatherBdd.insertWeather(new Weather_Data("Lisbon,PT"));
//		
//		
//		spinner = (Spinner) findViewById(R.id.spinner);
//		
//		weather_datas = weatherBdd.getWeathers();
//		for(int i = 0; i < weather_datas.size(); i++){
//			countries.add(weather_datas.get(i).getCountrie());
//		}
//
//		adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, countries);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner.setAdapter(adapter);

	}
	
	public void onClickConsult(View v){
		intent = new Intent(this, MaMeteoActivity.class);
		intent.putExtra("Countrie", spinner.getSelectedItem().toString());
		startActivity(intent);
	}
}
