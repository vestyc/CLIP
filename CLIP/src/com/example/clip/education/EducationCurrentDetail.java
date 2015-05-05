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
}
