package com.esgi.mameteo;

import java.util.ArrayList;
import java.util.List;

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
	private ArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		spinner = (Spinner) findViewById(R.id.spinner);
		countries = new ArrayList();	
		
		countries.add("Paris,FR");
		countries.add("London,EN");
		countries.add("Madrid,ES");
		countries.add("Lisbon,PT");

		adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, countries);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}
	
	public void onClickConsult(View v){
		Intent intent = new Intent(this, MaMeteoActivity.class);
		intent.putExtra("Countrie", spinner.getSelectedItem().toString());
		startActivity(intent);
	}
}
