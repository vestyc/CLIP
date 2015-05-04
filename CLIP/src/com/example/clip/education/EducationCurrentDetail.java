package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.content.Intent;

public class EducationCurrentDetail extends Activity {

	TextView name, dateStart, dateEnd, type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_current_detail);
		
		name = (TextView) findViewById(R.id.educationCurrent_detailName);
		type = (TextView) findViewById(R.id.educationCurrent_detailType);
		dateStart = (TextView) findViewById(R.id.educationCurrent_detailDateStarted);
		dateEnd = (TextView) findViewById(R.id.educationCurrent_detailDateEnd);
		
		Intent i = getIntent();
		int[] dateS = i.getIntArrayExtra("dateStart");
		int[] dateE = i.getIntArrayExtra("dateEnd");
		String currentName = i.getStringExtra("currentName");
		String dataString = i.getStringExtra("dataString");
		name.setText(currentName);
		type.setText(dataString);
		dateStart.setText(dateS[0] + "/" + dateS[1] + "/" + dateS[2]);
		dateEnd.setText(dateE[0] + "/" + dateE[1] + "/" + dateE[2]);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_current_detail, menu);
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
