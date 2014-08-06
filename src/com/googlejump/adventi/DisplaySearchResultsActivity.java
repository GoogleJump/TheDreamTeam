package com.googlejump.adventi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.location.Geocoder;
import android.location.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlejump.adventi.SearchActivity.MyAsyncTask;
import com.googlejump.adventi.models.AdventiUser;
import com.googlejump.adventi.models.Destination;
import com.googlejump.adventi.models.AdventiUser.Vehicle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

@SuppressLint("NewApi")
public class DisplaySearchResultsActivity extends ListActivity implements LocationListener{

	/* Used to display the search results */
	private ListView listView;
	/* ArrayList gets populated with restaurant names from API query */
	private ArrayList<String> destinationNamesArr = new ArrayList<String>();
	private ArrayList<String> displayNames = new ArrayList<String>();
	private ArrayList<Destination> destinationResults = new ArrayList<Destination>();
	
	/* Used to attach an array to the listView */
	private ArrayAdapter<String> adapter;
    private final String WS_API_KEY = "dce91a3aab38261fcc802285cea75e1e";

    private double latitude;
	private double longitude;
	AdventiUser currentUser;

	public AtomicInteger variable;
	
	private SearchActivity searcher;

	//map
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_search_results);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
		
		searcher = new SearchActivity();
		
		//show map
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			    .getMap();
			  // map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			  // map.setMapType(GoogleMap.MAP_TYPE_NONE);
			  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			  // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			  // map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

		variable = new AtomicInteger(0);	  

		Intent intent = getIntent();
		String message = intent
				.getStringExtra(SearchActivity.SEARCH_TERM_MESSAGE);
		currentUser = (AdventiUser) intent.getSerializableExtra("currentUser");
		TextView searchByPrefTerm = (TextView) findViewById(R.id.searchbypref_term);
		if(!(currentUser.getMethodOfTransport() == Vehicle.CAR)){
			searchByPrefTerm.setText("Nearby Adventi destinations + accessibility rating: ");
		}
		else{
			searchByPrefTerm.setText("Nearby Adventi destinations: ");
		}

		// Change title of view based on type of search
		String searchType = intent.getStringExtra(SearchActivity.SEARCH_TYPE);
		String jsonString = intent
				.getStringExtra(SearchActivity.SEARCH_RESULTS_MESSAGE);
		// Call for doInBackground() in MyAsyncTask to be executed
		//new MyAsyncTask(searchType).execute(jsonString);
		//searcher.new MyAsyncTask("google").execute("food",null, null);
		new MyAsyncTask3(searchType).execute(jsonString);
		//new MyAsyncTask2().execute();
		/*new WalkscoreRequest(destinationResults, currentUser.getMethodOfTransport());
		if (destinationResults != null) {
			for (int j=0; j < destinationResults.size(); j++) {
				String elem;
				elem = destinationResults.get(j).getName();
				if(destinationResults.get(j).getWalkScore() == null){
					elem += " - ";
					elem += destinationResults.get(j).getWalkScore();
				}
				System.out.println(elem);
				displayNames.add(elem);
			}
		}// end if */
	}

	@Override
	public void onLocationChanged(Location location) {
	   System.out.println("start onLocationChagned");
	   Geocoder coder = new Geocoder(this);
	   List<Address> address;
	   map.clear();
	   
	   latitude = location.getLatitude();
	   longitude = location.getLongitude();
	   
	   MarkerOptions mp = new MarkerOptions();

	   //mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
	   mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

	   mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
	   //mp.position(new LatLng(currentUser.getLatitude(), currentUser.getLongitude()));
	   
	   mp.title("my location");

	   map.addMarker(mp);
	   
	   //wait until all data is loaded
	   int num = 0;
	   while (num == 0) {
			num = variable.get();
	   }
	   placeMarkers(destinationResults);
	   
	   map.animateCamera(CameraUpdateFactory.newLatLngZoom(
	    new LatLng(location.getLatitude(), location.getLongitude()), 14));
	  }
	
	public void changeRefine(View view) {
		MainActivity.refine = true;
		Intent launchEatSearchAct = new Intent(DisplaySearchResultsActivity.this, SearchActivity.class);
		startActivity(launchEatSearchAct);
	}
	
	 public void placeMarkers(ArrayList<Destination> dest) {
		 Geocoder coder = new Geocoder(this);
		 List<Address> address;
		 
		 for (int i = 0; i < dest.size(); i++){
			 MarkerOptions mp = new MarkerOptions();
			 try {
				 address = coder.getFromLocationName((dest.get(i)).getAddress(), 1);
				 Address loc = address.get(0);
				 if (address.size() > 0)
				 {
					 mp.position(new LatLng(loc.getLatitude(), loc.getLongitude()));
					 mp.title((dest.get(i)).getName());
				 }
			 } 
			 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 
			 mp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			 map.addMarker(mp);
			 System.out.println("got here");
			 
		 }  
	 }
	 
	  @Override
	 public void onProviderDisabled(String provider) {
	  // TODO Auto-generated method stub

	  }

	  @Override
	 public void onProviderEnabled(String provider) {
	  // TODO Auto-generated method stub

	  }

	  @Override
	 public void onStatusChanged(String provider, int status, Bundle extras) {
	  // TODO Auto-generated method stub

	  }

	/* This thread takes care of parsing the search results for the info we want */
	private class MyAsyncTask3 extends AsyncTask<String, Void, Void> {
		String typeOfSearch;

		public MyAsyncTask3(String searchType) {
			typeOfSearch = searchType;
		}

		@Override
		protected Void doInBackground(String... args) {

			/* Info we query */
			String name;
			String phoneNumber;
			String address = null;
			String price_level;
			String placeURL;
			
			//String walkscoreResponse = null;
			//String formattedAddress;
			String longitude;
			String latitude;
			Integer walkscore = null;
			String walkDesc = null;
			

			if (typeOfSearch.equals("yelp")) {
				//System.out.println("Yelp API search!");

				// Holds Key Value pairs from a JSON source
				JSONObject jsonObject;
				try {

					/*
					 * From Yelp, we are gathering the the restaurant name,
					 * their phone number, their address, and their url.
					 */

					// Get the root JSONObject
					jsonObject = new JSONObject(args[0]);

					// Get the JSON object named businesses
					JSONArray businessList = jsonObject
							.getJSONArray("businesses");

					boolean isInList = false;
					
					for (int i = 0; i < businessList.length(); i++) {
						for (int p = 0; p < destinationNamesArr.size(); p++) {
							// Check for doubles that are already in the list
							if ((destinationNamesArr.get(p))
									.equals((businessList.getJSONObject(i))
											.getString("name"))) {
								isInList = true;
								break;
							}
						}
						if (!isInList) {
							name = (businessList.getJSONObject(i))
									.getString("name");
							phoneNumber = (businessList.getJSONObject(i))
									.getString("display_phone");
							placeURL = (businessList.getJSONObject(i))
									.getString("url");

							// getting location which is super nester
							JSONObject locationJSONObj = new JSONObject(
									(businessList.getJSONObject(i))
											.getString("location"));
							JSONArray locArray = locationJSONObj
									.getJSONArray("display_address");

							StringBuilder buildLoc;
							if (locArray.get(0) != null) {
								buildLoc = new StringBuilder(
										(String) locArray.get(0));
								for (int index = 1; index < locArray.length(); index++) {
									buildLoc.append(" " + locArray.get(index));
								}
								address = buildLoc.toString();
							}

							destinationNamesArr.add(name);
							destinationResults.add(new Destination(name,
									phoneNumber, address, null, placeURL));
							/*System.out.println("Adding "
									+ destinationNamesArr.size()
									+ "th rest to array: " + name);
							System.out.println("It's phone is: " + phoneNumber);
							System.out.println("It's address is: " + address);*/
						}

					}

					/*for (int i = 0; i < destinationNamesArr.size(); i++) {
						System.out.println(destinationNamesArr.get(i));
					}*/

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					/*System.out
							.println("No results found for your current location.");*/
					e.printStackTrace();
				}
			}// end if
			else {
				// GOOGLE PLACES API USED FOR SEARCH BY PRICE
				// Holds Key Value pairs from a JSON source
				//System.out.println("Google API call!");
				JSONObject jsonObject;
				try {

					// Get the root JSONObject
					jsonObject = new JSONObject(args[0]);

					String returnCode = jsonObject.getString("status");

					/* ERROR CODE HANDLING FOR GOOGLE PLACES API */

					if (returnCode == "ZERO_RESULTS") {
						// display no results found message
						//System.out.println("Zero results for this search.");						
						return null;

					} else if (returnCode == "OVER_QUERY_LIMIT") {
						// display error that Hungry Wallet is experience heavy
						// usage so it's temporarily down
						return null;
					} else if (returnCode == "REQUEST_DENIED"
							|| returnCode == "INVALID_REQUEST") {
						// display some generic error screen
						return null;
					} else {
						// it's ok?

						// Get the JSON object named query
						JSONArray businessList = jsonObject
								.getJSONArray("results");


						for (int i = 0; i < businessList.length(); i++) {
							boolean isInList = false;
							for (int p = 0; p < destinationNamesArr.size(); p++) {
								// Check for doubles that are already in the
								// list
								if ((destinationNamesArr.get(p))
										.equals((businessList.getJSONObject(i))
												.getString("name"))) {
									isInList = true;
									break;
								}
							}
							// If it hasn't been added yet, add it to the list
							if (!isInList) {
								name = (businessList.getJSONObject(i))
										.getString("name");
								address = (businessList.getJSONObject(i))
										.getString("vicinity");
								/*price_level = (businessList.getJSONObject(i))
										.getString("price_level");*/
								price_level = Integer.toString(0);
								latitude = ((businessList.getJSONObject(i))
										.getJSONObject("geometry")).getJSONObject("location").getString("lat");
								longitude = (businessList.getJSONObject(i))
										.getJSONObject("geometry").getJSONObject("location").getString("lng");

														    		
								//used to have adding to arraylist and names array here

								
								
								
						destinationNamesArr.add(name);
						destinationResults.add(new Destination(name,
								null, address, Integer.parseInt(price_level), null, 
							    Double.parseDouble(longitude), Double.parseDouble(latitude), 
							    walkscore, walkDesc));
								
								
								
								
								
								/*System.out.println("Adding "
										+ destinationNamesArr.size()
										+ "the rest to array: " + name + " at "
										+ address);*/
							}
						}

						/*for (int i = 0; i < destinationNamesArr.size(); i++) {
							System.out.println(destinationNamesArr.get(i));
						}*/
						
						System.out.println("At the end of first asynctask - size of destResults: " + destinationResults.size());
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			variable.getAndIncrement();
			// System.out.println("Size of the names array: " +
			// destinationNamesArr.size());
			return null;

		}// end doInBackground

		/*
		 * After the search has occurred and results array is populated, we want
		 * to display the results using a ListView on the screen. We change the
		 * color of the text in the TextViews of the the ListView to be black
		 * because it is white by default
		 */
		protected void onPostExecute(Void result) {
			listView = (ListView) getListView();
			// listView = (ListView) findViewById(R.id.list);

			// set up ArrayAdapter to use the custom layout for each search
			// result
			if(destinationResults.size() == 0){
				((TextView)findViewById(R.id.searchbypref_term)).setText("No results found.\n" +   "Please check your location services settings or search other related terms.");
				((TextView)findViewById(R.id.searchbypref_term)).setGravity(0x01);
				((TextView)findViewById(R.id.searchbypref_term)).setTextSize(25);
				return;
			}
			
			new WalkscoreRequest(destinationResults, currentUser.getMethodOfTransport());
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (destinationResults != null) {
				for (int j=0; j < destinationResults.size(); j++) {
					String elem;
					elem = destinationResults.get(j).getName();
					if(destinationResults.get(j).getWalkScore() != null){
						elem += " - ";
						elem += destinationResults.get(j).getWalkScore();
						System.out.println(elem);
					}
					displayNames.add(elem);
				}
				
				listView.setAdapter(adapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1, displayNames) {

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {

						View view = super
								.getView(position, convertView, parent);
						TextView text = (TextView) view
								.findViewById(android.R.id.text1);
						text.setTextColor(Color.BLACK);

						return view;
					}// end getView function

				}); // end setAdapter call
			}// end if
		}// end onPostExecute function

	}// end internal AsyncTask class

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
		getMenuInflater().inflate(R.menu.display_search_results, menu);
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

	// TODO: REFACTOR THIS METHOD
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Destination restObj = destinationResults.get(position);
		super.onListItemClick(lv, v, position, id);
		/*System.out.println("Name of result clicked was: "
				+ destinationNamesArr.get(position));*/

		// Pop up a dialog with more info
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		LayoutInflater inflater = this.getLayoutInflater();
		View dialogLayout;
		if (restObj.getPriceLevel() == null)
			dialogLayout = inflater
					.inflate(R.layout.dialog_search_result, null);
		else {
			dialogLayout = inflater
					.inflate(R.layout.dialog_google_result, null);
		}
		builder.setView(dialogLayout);
		builder.setTitle(destinationNamesArr.get(position));

		// Yelp
		if (restObj.getPriceLevel() == null) {

			// address
			if (restObj.getAddress() != null) {
				TextView phoneView = (TextView) dialogLayout
						.findViewById(R.id.resultPhone);
				phoneView.setText("Phone: " + restObj.getPhoneNumber());
			} else {
				TextView phoneView = (TextView) dialogLayout
						.findViewById(R.id.resultPhone);
				phoneView.setText("Phone: Not Available");
			}

			// address
			if (restObj.getAddress() != null) {
				TextView addressView = (TextView) dialogLayout
						.findViewById(R.id.resultAddress);
				addressView.setText("Address: " + restObj.getAddress());
			} else {
				TextView addressView = (TextView) dialogLayout
						.findViewById(R.id.resultAddress);
				addressView.setText("Address: Not Available");
			}

			// url
			if (restObj.getUrl() != null) {
				TextView urlView = (TextView) dialogLayout
						.findViewById(R.id.resultUrl);
				urlView.setText("Url: " + restObj.getUrl());
			} else {
				TextView urlView = (TextView) dialogLayout
						.findViewById(R.id.resultUrl);
				urlView.setText("Url: Not Available");
			}

		} else {
			
			builder.setMessage("Price Scale: " + (restObj.getPriceLevel() + 1)
					+ " dollar signs");

			// address
			if (restObj.getAddress() != null) {
				TextView addressView = (TextView) dialogLayout
						.findViewById(R.id.resultAddress);
				addressView.setText("Address: " + restObj.getAddress());
			} else {
				TextView addressView = (TextView) dialogLayout
						.findViewById(R.id.resultAddress);
				addressView.setText("Address: Not Available");
			}

		}

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	
	
	
	
	/* This thread takes care of the Walkscore API request and processing */
	private class MyAsyncTask2 extends AsyncTask<String, Void, Void> {
		
		String walkscoreResponse = "";
		String formattedAddress;
		Integer walkscore = null;
		String walkDesc = null;
		// Holds Key Value pairs from a JSON source
		JSONObject jsonObject;
	
		@Override
		protected Void doInBackground(String... args) {
			
			//For each place in destinationResults array, try to find it's walkscore
			for(Destination dest: destinationResults){
				System.out.println("WalkScore API params: ");
				System.out.println("Lat: " + dest.getLatitude());
				System.out.println("Lng: " + dest.getLongitude());
				System.out.println("Address: " + dest.getAddress());
				//Format the address param for the Walkscore request
				formattedAddress = dest.getAddress().replace(" ", "%20");
				System.out.println("Formatted Address: " + formattedAddress);
				
				
				/*http://transit.walkscore.com/transit/score/?lat=47.6101359&lon=-122.3420567&
				 * city=Seattle&state=WA&
				 * wsapikey=ffd1c56f9abcf84872116b4cc2dfcf31*/
				
				/* Making request to Walkscore API for walk score information */
				StringBuilder urlString = new StringBuilder();
				/*urlString.append("http://api.walkscore.com/score?format=json&");
		        urlString.append("address=").append(formattedAddress);								
		        urlString.append("&lat=").append(dest.getLatitude());
		        urlString.append("&lon=").append(dest.getLongitude());		       
		        urlString.append("&wsapikey=").append(WS_API_KEY);*/
				urlString.append("http://www.google.com");
				
				
				
				
				
				/*try something else
				urlString.append("http://transit.walkscore.com/transit/score/?");
		        urlString.append("&lat=").append(dest.getLatitude());
		        urlString.append("&lon=").append(dest.getLongitude());	
		        urlString.append("&city=").append(dest.getLongitude());
		        urlString.append("&state=").append(dest.getLongitude());
		        urlString.append("&wsapikey=").append(WS_API_KEY);
				try something else
				*/
				
				
				
		        
		        System.out.println("yay:"+urlString.toString());

		        HttpURLConnection urlConnection = null;
		        URL url = null;
		        
		        //try out
		       //StringBuilder result = new StringBuilder();

		        try
		        {
		            url = new URL(urlString.toString());
		            urlConnection = (HttpURLConnection) url.openConnection();
		           /* urlConnection.setRequestMethod("GET");
		            urlConnection.setDoOutput(true);
		            urlConnection.setDoInput(true);
		            urlConnection.connect();*/
		            InputStream inStream = null;
		           // Thread.sleep(5000);
		            inStream = urlConnection.getInputStream();
		            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		            String temp = "";
		            System.out.println("About to take input");
		            /*while ((temp = bReader.readLine()) != null){
		            	System.out.println("temp: " +temp);
		                walkscoreResponse += temp;
		            	//result.append(temp);
		            }	*/	
		            
		           while((temp = bReader.readLine()) != null) {
		        	   walkscoreResponse += temp;
		        	   System.out.println("temp: " + temp);
		           }
		           
		            System.out.println("No input");

		            bReader.close();
		            inStream.close();
		            urlConnection.disconnect();
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
							
		        //System.out.println(walkscoreResponse);
		        /* End of Walkscore API request */
		        
		        /* Process Walkscore response */
		        
			    // Get the root JSONObject
		        try{
		        	System.out.println("Response: " + walkscoreResponse);
					jsonObject = new JSONObject(walkscoreResponse);
					String status = jsonObject.getString("status");
					
					if(Integer.parseInt(status) == 1){
						walkscore = Integer.parseInt(jsonObject.getString("walkscore"));
						System.out.println(walkscore);
						walkDesc = jsonObject.getString("description");
						System.out.println(walkDesc);
					}
					else{
						//handle this instance of lack in walkscore by setting to -1?
						//print Walkscore error!!
						System.out.println("No walkscore available for this Adventi destination.");
					}
		        }
		        catch(JSONException e){
		        	System.out.println("JSONException occurred!!");
		        	e.printStackTrace(System.out);
		        }
				
			    /* End of processing Walkscore response*/
				
			}//end for each destination in results array

			return null;

		}// end doInBackground

		/*
		 * After the search has occurred and results array is populated, we want
		 * to display the results using a ListView on the screen. We change the
		 * color of the text in the TextViews of the the ListView to be black
		 * because it is white by default
		 */
		protected void onPostExecute(Void result) {
			listView = (ListView) getListView();
			// listView = (ListView) findViewById(R.id.list);

			// set up ArrayAdapter to use the custom layout for each search
			// result
			if(destinationNamesArr.size() == 0){
				((TextView)findViewById(R.id.searchbypref_term)).setText("No results found.\n" +   "Please check your location services settings or search other related terms.");
				((TextView)findViewById(R.id.searchbypref_term)).setGravity(0x01);
				((TextView)findViewById(R.id.searchbypref_term)).setTextSize(25);
				return;
			}
			
			if (destinationNamesArr != null) {
				listView.setAdapter(adapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_list_item_1, destinationNamesArr) {

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {

						View view = super
								.getView(position, convertView, parent);
						TextView text = (TextView) view
								.findViewById(android.R.id.text1);
						text.setTextColor(Color.BLACK);

						return view;
					}// end getView function

				}); // end setAdapter call
			}// end if
		}// end onPostExecute function

	}// end internal MyAsyncTask2 class
	
	
	
	
}



