package com.esgi.mameteo;

import org.json.JSONException;

import modele.Weather;
import BDD.WeatherBDD;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MaMeteoActivity extends Activity {

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	
	private TextView hum;
	private ImageView imgView;
	
	private String city;

	private WeatherBDD weatherBdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma_meteo);
		
		Intent intent = getIntent();
		city = intent.getStringExtra("Countrie");
		
		cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);
		imgView = (ImageView) findViewById(R.id.condIcon);

		weatherBdd = new WeatherBDD(this);
		
		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{city});
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	public void onClickFav(View v){
		String toast = "";
		
		weatherBdd.open();
		if(weatherBdd.isFavoriteWithCountires(city)){
//			weatherBdd.setFavoriteWeatherWithCountries(city, 0);
//			toast = city+" removed from favorites";
		}else{
//			weatherBdd.setFavoriteWeatherWithCountries(city, 1);
//			toast = city+" added to favorites";
		}
		weatherBdd.close();
    	Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
		
	}
	
	
	public void onClickRefresh(View v){
		finish();
		startActivity(getIntent());
	}
	
	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
		
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONMeteoParser.getWeather(data);
				
				// Let's retrieve the icon
				weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;
		
	}
		
		
		
		
	@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);
			
			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
				imgView.setImageBitmap(img);
			}
			
			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "c");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg());
				
		}






	
  }
}
