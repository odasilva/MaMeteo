package com.esgi.mameteo;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchResultsActivity extends Activity{
	
	private ListView resultsList;
	private ArrayList<String> foundedCities;
	private ArrayAdapter adapter;
	//private String cityName;
	//private   String apiSearchUrl = "http://api.openweathermap.org/data/2.5/find?q=" +cityName +"&type=like&mode=xml";
	//private   String apiSearchUrl = "http://api.openweathermap.org/data/2.5/find?";
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
 
        // get the action bar
        ActionBar actionBar = getActionBar();
 
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        
        handleIntent(getIntent());
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
       // setIntent(intent);
        handleIntent(intent);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	try {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v("MAMETEO",query);
            parseResponse( new CitySearchTask().execute(query).get());
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,foundedCities);
            resultsList = (ListView) findViewById(R.id.resultsList);
            resultsList.setAdapter(adapter);
            resultsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String selected = (String)adapter.getItem(position);
					Intent intent = new Intent(SearchResultsActivity.this, MaMeteoActivity.class);
					intent.putExtra("Countrie",selected);	
					startActivity(intent);
				}
			});
			} catch (InterruptedException e) {}
			catch (ExecutionException e) {}
         
        }
 
    }
    
    private void parseResponse(String result){
    	try{
    		foundedCities = new ArrayList<String>();
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	InputSource is = new InputSource(new StringReader(result));
        	Document doc = builder.parse(is);
        	//get all retrievde cities
        	NodeList citiesXml = doc.getElementsByTagName("city");
        	//insert citiesName in an ArrayList
        	for(int i = 0; i < citiesXml.getLength(); i ++)
        		foundedCities.add(citiesXml.item(i).getAttributes().getNamedItem("name").getNodeValue());
        	}catch(IOException e){}
        	catch (ParserConfigurationException e) {}
    		catch (SAXException e) {}
    	
    }
    
    private class CitySearchTask extends AsyncTask<String, Void,String >{

    	@Override
    	protected String doInBackground(String... params) {
    		String searchQuery = params[0];
    	    HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet( "http://api.openweathermap.org/data/2.5/find?q=" +searchQuery +"&type=like&mode=xml"));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
    	}

    	  @Override
    	    protected void onPostExecute(String result) {
    	        super.onPostExecute(result);
    	        try{
    	        }catch(Exception e){
    	        	
    	        }
    	    }
    	
    }

}



