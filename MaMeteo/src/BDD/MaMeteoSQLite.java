package BDD;

import modele.Weather_Data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaMeteoSQLite extends SQLiteOpenHelper{

	private static final String TABLE_WEATHER = "table_weather";
	private static final String COL_ID = "ID";
	private static final String COL_COUNTRIE = "Countrie";
	private static final String COL_FAVORITE = "Favorite";
 
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_WEATHER + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_COUNTRIE + " TEXT NOT NULL, "
			+ COL_FAVORITE + " INTEGER NOT NULL);";
	
	private static final String INSERT_BDD = "INSERT INTO " + TABLE_WEATHER + " ("
			+ COL_COUNTRIE + ", "+ COL_FAVORITE + " ) " +
			" VALUES ('Paris,FR', 0), ('Romainville,FR', 0), ('Montreuil,FR', 0), ('Marseille,FR', 0), ('Lyon,FR', 0)," +
			"('Toulouse,FR', 0),('Nice,FR', 0),('Nantes,FR', 0),('Strasbourg,FR', 0),('Montpellier,FR', 0)," +
			"('Bordeaux,FR', 0),('Lille,FR', 0),('Rennes,FR', 0),('Brest,FR', 0),('Le Havre,FR', 0)," +
			"('Toulon,FR', 0),('Grenoble,FR', 0),('Dijon,FR', 0),('Angers,FR', 0),('Villeurbanne,FR', 0);";
			
							
	public MaMeteoSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
		db.execSQL(INSERT_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_WEATHER + ";");
		onCreate(db);
	}

}
