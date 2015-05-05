package com.example.clip.career;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.clip.R;

public class CareerMenu extends Activity {
	
	Button goal, jobApp, compInfo, eId, contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_menu);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		//button assignments
		goal = (Button) findViewById(R.id.button_goal);
		jobApp = (Button) findViewById(R.id.button_jobApp);
		compInfo = (Button) findViewById(R.id.button_compInfo);
		eId = (Button) findViewById(R.id.button_eId);
		contact = (Button) findViewById(R.id.button_contact);
		
		//listeners
		goal.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(CareerMenu.this, CareerGoal.class);
				startActivity(i);
	         }
		});
		
		jobApp.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(CareerMenu.this, CareerJobApp.class);
				startActivity(i);
			}
		});
		
		compInfo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(CareerMenu.this, CareerCompInfo.class);
				startActivity(i);
	         }
		});
		
		contact.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(CareerMenu.this, CareerContact.class);
				startActivity(i);
	         }
		});
		
		eId.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(CareerMenu.this, CareerEid.class);
				startActivity(i);
	         }
		});
	}
}
