package BDD;

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
	
	public MaMeteoSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_WEATHER + ";");
		onCreate(db);
	}

}
