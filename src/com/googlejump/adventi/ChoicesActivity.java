package com.googlejump.adventi;

import com.googlejump.adventi.models.AdventiUser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ChoicesActivity extends Activity {
	
	AdventiUser currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent info = getIntent();
		currentUser = (AdventiUser) info.getSerializableExtra("currentUser");
		
		
		
		Button button = (Button) findViewById(R.id.Option1);
		button.setOnClickListener (new View.OnClickListener(){
		    public void onClick(View v) {
		    	Intent launchEatSearchAct = new Intent(ChoicesActivity.this, SearchActivity.class);

		    	launchEatSearchAct.putExtra("currentUser", currentUser);
				startActivity(launchEatSearchAct);		
			}
		}); 

		Button button02 = (Button) findViewById(R.id.Option2);
		button02.setOnClickListener (new View.OnClickListener(){
		    public void onClick(View v) {
		    	//launchEatSearchAct.putExtra("currentUser", currentUser);
		    	setContentView(R.layout.option2);
		   }
		}); 

		Button button01 = (Button) findViewById(R.id.Option3);
		button01.setOnClickListener (new View.OnClickListener(){
		    public void onClick(View v) {
		    	//launchEatSearchAct.putExtra("currentUser", currentUser);
		    	setContentView(R.layout.option3);
		   }
		}); 

		Button button1 = (Button) findViewById(R.id.Option4);
		button1.setOnClickListener (new View.OnClickListener(){
		    public void onClick(View v) {
		    	//launchEatSearchAct.putExtra("currentUser", currentUser);
		    	setContentView(R.layout.option4);
		   }
		});
		
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choices, menu);
		return true;
	}

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

}
