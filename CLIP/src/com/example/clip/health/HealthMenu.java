package com.example.clip.health;

import com.example.clip.Entry;
import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.example.clip.education.EducationMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class HealthMenu extends Activity {

	Button vitals, exercise, diet, checkup, medications, allergies, recipes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_menu);
		
		vitals = (Button) findViewById(R.id.health_buttonVitals);
		exercise = (Button) findViewById(R.id.health_buttonExercise);
		diet = (Button) findViewById(R.id.health_buttonDiet);
		checkup = (Button) findViewById(R.id.health_buttonCheckup);
		medications = (Button) findViewById(R.id.health_buttonMedication);
		allergies = (Button) findViewById(R.id.health_buttonAllergies);
		recipes = (Button) findViewById(R.id.health_buttonRecipes);
		
		vitals.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

					Intent i = new Intent(HealthMenu.this, HealthVital.class);
					startActivity(i);

				}

		});
		
		exercise.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

					Intent i = new Intent(HealthMenu.this, HealthExercise.class);
					startActivity(i);

				}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
