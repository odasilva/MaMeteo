package BDD;

import java.util.ArrayList;
import java.util.List;

import modele.Weather_Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherBDD {
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "weather.db";
 
	private static final String TABLE_WEATHER = "table_weather";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_COUNTRIE = "Countrie";
	private static final int NUM_COL_COUNTRIE = 1;
	private static final String COL_FAVORITE = "Favorite";
	private static final int NUM_COL_FAVORITE = 2;
 
	private SQLiteDatabase bdd;
 
	private MaMeteoSQLite maMeteoSQLite;
 
	public WeatherBDD(Context context){
		maMeteoSQLite = new MaMeteoSQLite(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		bdd = maMeteoSQLite.getWritableDatabase();
	}
 
	public void close(){
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertWeather(Weather_Data weather){
		
		ContentValues values = new ContentValues();

		values.put(COL_COUNTRIE, weather.getCountrie());
		values.put(COL_FAVORITE, weather.getFavorite());

		return bdd.insert(TABLE_WEATHER, null, values);
	}
 
	public int updateWeather(int id, Weather_Data weather){

		ContentValues values = new ContentValues();
		
		values.put(COL_COUNTRIE, weather.getCountrie());
		values.put(COL_FAVORITE, weather.getFavorite());
		
		return bdd.update(TABLE_WEATHER, values, COL_ID + " = " +id, null);
	}
	
	public int setFavoriteWeatherWithCountries(String countries, int favorite){

		ContentValues values = new ContentValues();
		
		values.put(COL_COUNTRIE, countries);
		values.put(COL_FAVORITE, favorite);
		
		return bdd.update(TABLE_WEATHER, values, COL_COUNTRIE + " = " +countries, null);
	}
	
	public boolean isFavoriteWithCountires(String countries){
		
	    String query = "SELECT "+COL_ID+" FROM "+TABLE_WEATHER +" WHERE "+COL_COUNTRIE+" = "+countries+";";
	    boolean isFavorite = false;
	    Cursor c = bdd.rawQuery(query, null);
	    
	    c.moveToNext();
	    isFavorite = (1 == getWeatherWithId(c.getInt(0)).getFavorite());
	    c.close();
	    
	    return isFavorite;
	}
 
	public int removeWeatherWithID(int id){

		return bdd.delete(TABLE_WEATHER, COL_ID + " = " +id, null);
	}
	
	public int removeWeatherWithCountries(String countries){

		return bdd.delete(TABLE_WEATHER, COL_COUNTRIE + " = " +countries, null);
	}
 
	public Weather_Data getWeatherWithId(int id){

		Cursor c = bdd.query(TABLE_WEATHER, new String[] {COL_ID, COL_COUNTRIE, COL_FAVORITE}, COL_ID + " LIKE \"" + id +"\"", null, null, null, null);
		return cursorToWeather(c);
	}

	private Weather_Data cursorToWeather(Cursor c){

		if (c.getCount() == 0)
			return null;

		c.moveToFirst();

		Weather_Data weather = new Weather_Data();

		weather.setId(c.getInt(NUM_COL_ID));
		weather.setCountrie(c.getString(NUM_COL_COUNTRIE));
		weather.setFavorite(c.getInt(NUM_COL_FAVORITE));

		c.close();

		return weather;
	}
	
	public List<Weather_Data> getWeathers()
	{
	    List<Weather_Data> weathers = new ArrayList<Weather_Data>();

	    String query = "SELECT "+COL_ID+" FROM "+TABLE_WEATHER;

	    Cursor c = bdd.rawQuery(query, null);
	    
	    while(c.moveToNext()) {
	    	weathers.add(getWeatherWithId(c.getInt(0)));
	    }
	    c.close();
	    
	    return weathers;
	}
	
	
	public ArrayList<String> getFavorite()
	{
		String query = "SELECT "+COL_COUNTRIE+" FROM "+TABLE_WEATHER +" WHERE "+COL_FAVORITE+" = 1";
		ArrayList<String> favorites = new ArrayList<String>();
		
	    Cursor c = bdd.rawQuery(query, null);
	    
	    while(c.moveToNext()) {
	    	favorites.add(getWeatherWithId(c.getInt(0)).getCountrie());
	    }
	    c.close();
	    
	    return favorites;		
	}
}
