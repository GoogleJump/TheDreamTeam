package com.googlejump.adventi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SearchActivity extends Activity implements 
GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener {

	    // Used to gather user's current location
		LocationClient mCurrentLocationClient;
		// Constants used to transfer search details to another activity
		public final static String SEARCH_TERM_MESSAGE = "com.example.adventi.SEARCH_TERM";
		public final static String SEARCH_TYPE = "com.example.adventi.SEARCH_TYPE";
	    public final static String SEARCH_RESULTS_MESSAGE = "com.example.adventi.SEARCH_RESULTS";
	    

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// Show the Up button in the action bar.
		setupActionBar();
		//Set up in order to get user's location
		mCurrentLocationClient = new LocationClient(this, this, this);	

	}
	
	/*
     * Called when the Activity becomes visible.
     */
	@Override
    protected void onStart() {
		super.onStart();
        // Connect the client.
		mCurrentLocationClient.connect();	
		
	}
	
	/*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mCurrentLocationClient.disconnect();
        super.onStop();
    }
    
    /* Thread used to perform a Search - by preference or by price range
	 * 
	 * If search option is by preference, the Yelp API is used
	 * If search option is by price range, the Google Places API is used
	 *  and results are returned based on the price_levels of restaurants.
	 */
	private class MyAsyncTask extends AsyncTask<String,Void,Void>{
		
		String typeOfSearch;
		
		public MyAsyncTask(String typeOfSearch){
			this.typeOfSearch = typeOfSearch;
		}

		@Override
		protected Void doInBackground(String... args) {
			
			String response = "";
			
		    System.out.println("THE USER WANTS TO FIND PLACES TO EAT "+ args[0]);
		  //GET LOCATION NOW
		    Location loc = mCurrentLocationClient.getLastLocation();
		    double latitude = loc.getLatitude();
		    double longitude = loc.getLongitude();
			System.out.println("Location is: " + loc);
			System.out.println("Latitude is: " + latitude);
			System.out.println("Longitude: " + longitude);

			
			if(typeOfSearch.equals("yelp")){
				String consumerKey = "JmCEPESnmAdI--_rPRtojg";
			    String consumerSecret = "Ss8vkoOTJTkwTW-TosiiDUIlgMg";
			    String token = "uDoa1Gkuapkc6YIcUn_fv7uN8PHGM7rb";
			    String tokenSecret = "CKzZFC8figPjQI3NQYI0P7MwOjc";
			    
			    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
				response = yelp.search(args[0], latitude, longitude);
				
			}
			else{
				
				//Build URL for Google API request
				StringBuilder urlString = new StringBuilder();
		        urlString.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		        urlString.append("location=").append(latitude + "," + longitude);
		        urlString.append("&sensor=").append("true");
		        //urlString.append("&name=").append(params[1]);
		        urlString.append("&rankby=").append("distance");
		        urlString.append("&keyword=").append(args[0]);
		        urlString.append("&minprice=").append("0");
		        urlString.append("&maxprice=").append("3");
		        urlString.append("&key=").append("AIzaSyA63EmMpjRs3wJd_9SU-QsuCw-C7AKA5Qs");
		        
		        HttpURLConnection urlConnection = null;
		        URL url = null;
		        //JSONObject object = null;

		        try
		        {
		            url = new URL(urlString.toString());
		            urlConnection = (HttpURLConnection) url.openConnection();
		            urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		            urlConnection.setDoInput(true);
		            urlConnection.connect();
		            InputStream inStream = null;
		            inStream = urlConnection.getInputStream();
		            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		            String temp = "";
		            while ((temp = bReader.readLine()) != null)
		                response += temp;
		            bReader.close();
		            inStream.close();
		            urlConnection.disconnect();
		            //object = (JSONObject) new JSONTokener(response).nextValue();
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
				
			}//end else
			

	        System.out.println(response);
	        //print something extra if there was a Search by Price
			toPassSearchResults(typeOfSearch, args[0], response);
			 return null;
		} 
		
	}//end private class MyAsyncTask
	
	public void sendPrefMessage(View view) {
        // Do something in response to button
	    EditText editText = (EditText) findViewById(R.id.edit_foodsearch);
    	String message = editText.getText().toString();
        new MyAsyncTask("yelp").execute(message,null, null);
		//Toast.makeText(this, "Yelp request complete", Toast.LENGTH_LONG).show();

    }
	
	public void sendPriceMessage(View view) {
        // Do something in response to button
	
		EditText editText = (EditText) findViewById(R.id.edit_foodsearch);
    	String message = editText.getText().toString();
    	
    	//Initiates Google places request
    	new MyAsyncTask("google").execute(message,null, null);
        Toast.makeText(this, "Google request complete", Toast.LENGTH_LONG).show();
    
    }

	public void toPassSearchResults(String searchType, String searchTerm, String jsonResult){
		Intent intent = new Intent(this, DisplaySearchResultsActivity.class);
    	intent.putExtra(SEARCH_TERM_MESSAGE, searchTerm);
    	intent.putExtra(SEARCH_TYPE, searchType);
    	intent.putExtra(SEARCH_RESULTS_MESSAGE, jsonResult);
    	startActivity(intent);	
	}
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Please turn on location or GPS services.", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
