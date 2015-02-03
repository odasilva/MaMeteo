package com.esgi.mameteo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		List exemple = new ArrayList();		
		exemple.add("Assinie");
		exemple.add("Bassam");
		exemple.add("Abidjan");

		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, exemple);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

	}
}
