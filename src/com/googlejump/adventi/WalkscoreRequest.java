package com.googlejump.adventi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

import com.googlejump.adventi.models.Destination;

public class WalkscoreRequest {
	ArrayList<Destination> destResults;
    private final String WS_API_KEY = "ffd1c56f9abcf84872116b4cc2dfcf31";

	
	public WalkscoreRequest(ArrayList<Destination> destinationResults){
		this.destResults = destinationResults;
		System.out.println("Starting Walkscore Request");
		new MyAsyncTask2().execute();
	}
	
	
	/* This thread takes care of the Walkscore API request and processing */
	private class MyAsyncTask2 extends AsyncTask<Void, Void, Void> {
		
		String walkscoreResponse;
		String formattedAddress;
		Integer walkscore = null;
		String walkDesc = null;
		// Holds Key Value pairs from a JSON source
		JSONObject jsonObject;
	
		@Override
		protected Void doInBackground(Void... args) {
			System.out.println("DestResults: " + destResults);
			System.out.println("DestResults size: " + destResults.size());
			
			
			//For each place in destinationResults array, try to find it's walkscore
			for(int count = 0; count < destResults.size(); count++){
				Destination dest = destResults.get(count);
				walkscoreResponse = "";
				System.out.println("WalkScore API params: ");
				System.out.println("Lat: " + dest.getLatitude());
				System.out.println("Lng: " + dest.getLongitude());
				System.out.println("Address: " + dest.getAddress());
				//Format the address param for the Walkscore request
				formattedAddress = dest.getAddress().replace(" ", "%20");
				System.out.println("Formatted Address: " + formattedAddress);
				
				
				/* Making request to Walkscore API for walk score information */
				StringBuilder urlString = new StringBuilder();
				urlString.append("http://api.walkscore.com/score?format=json&");
		        urlString.append("address=").append(formattedAddress);								
		        urlString.append("&lat=").append(dest.getLatitude());
		        urlString.append("&lon=").append(dest.getLongitude());		       
		        urlString.append("&wsapikey=").append(WS_API_KEY);
		        
		        System.out.println(urlString.toString());

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
		            inStream = urlConnection.getInputStream();
		            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		            String temp;
		            System.out.println("About to take input");
		            while ((temp = bReader.readLine()) != null){
		            	//System.out.println("temp: " +temp);
		                walkscoreResponse += temp;
		            }
		           // System.out.println("No input");

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
						dest.setWalkScore(walkscore);
						System.out.println(walkscore);
						walkDesc = jsonObject.getString("description");
						dest.setWalkDesc(walkDesc);
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
			
		}// end onPostExecute function

	}// end internal MyAsyncTask2 class

}
