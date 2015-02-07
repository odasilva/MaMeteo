package com.esgi.mameteo;

import org.json.JSONException;

import modele.Weather;
import modele.Weather_Data;
import BDD.WeatherBDD;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
	
	private MenuInflater inflater;
	private Intent intent;
	
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

	public void onClickFav(View v){
		String toast = "";
		
		weatherBdd.open();
		if(weatherBdd.isFavoriteWithCountires(city)){
			weatherBdd.setFavoriteWeatherWithCountries(city, 0);
			toast = city+" "+getResources().getString(R.string.toastDelFavorite);
		}else{
			weatherBdd.setFavoriteWeatherWithCountries(city, 1);
			toast = city+" "+getResources().getString(R.string.toastAddFavorite);
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
									weatherBdd.open();
									long error = weatherBdd.insertWeather(new Weather_Data(city));
									weatherBdd.close();
									
									if(error != 0)
										 Toast.makeText(MaMeteoActivity.this,city+" "+getResources().getString(R.string.succes), Toast.LENGTH_LONG).show();
									else
										 Toast.makeText(MaMeteoActivity.this,getResources().getString(R.string.fail_add_city), Toast.LENGTH_LONG).show();
										
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
	                Toast.makeText(MaMeteoActivity.this,getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.version)+" : "+ pInfo.versionName, Toast.LENGTH_LONG).show();
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