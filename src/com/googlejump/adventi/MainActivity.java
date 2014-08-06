package com.googlejump.adventi;

import com.googlejump.adventi.models.AdventiUser;
import com.googlejump.adventi.models.AdventiUser.Vehicle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	AdventiUser currentUser = new AdventiUser();
	Context currentAct = this;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentUser.setMethodOfTransport(Vehicle.CAR);
				System.out.println("Car is selected");
				Toast.makeText(currentAct, "Car mode of transportation set", Toast.LENGTH_SHORT ).show();
				Intent launchAct = new Intent(MainActivity.this, ChoicesActivity.class);
				launchAct.putExtra("currentUser", currentUser);
				startActivity(launchAct);
			}
		});
		
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentUser.setMethodOfTransport(Vehicle.BICYCLE);
				System.out.println("Bicycle is selected");
				Toast.makeText(currentAct, "Bicycle mode of transportation set", Toast.LENGTH_SHORT ).show();
				Intent launchAct = new Intent(MainActivity.this, ChoicesActivity.class);
				launchAct.putExtra("currentUser", currentUser);
				startActivity(launchAct);
			}
		});
		
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentUser.setMethodOfTransport(Vehicle.WALK);
				System.out.println("Walk is selected");
				Toast.makeText(currentAct, "Walk mode of transportation set", Toast.LENGTH_SHORT ).show();
				Intent launchAct = new Intent(MainActivity.this, ChoicesActivity.class);
				launchAct.putExtra("currentUser", currentUser);
				startActivity(launchAct);
			}
		});
		
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentUser.setMethodOfTransport(Vehicle.BUS);
				System.out.println("Bus is selected");
				Toast.makeText(currentAct, "Bus mode of transportation set", Toast.LENGTH_SHORT ).show();
				Intent launchAct = new Intent(MainActivity.this, ChoicesActivity.class);
				launchAct.putExtra("currentUser", currentUser);
				startActivity(launchAct);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
