package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CareerJobAppDetail extends Activity {

	String name;
	String[] data;
	int[] date;
	TextView appStatus, comments, jobName, dateApplied;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_job_app_detail);
		
		appStatus = (TextView) findViewById(R.id.appStatus);
		comments = (TextView) findViewById(R.id.jobComments);
		jobName = (TextView) findViewById(R.id.jobName);
		dateApplied = (TextView) findViewById(R.id.dateApplied);
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
		date = getIntent().getIntArrayExtra("date");
		
		// data = {appStatus, comments}
		if(name != null) {
		
			jobName.setText(name);
		}
		if(data != null) {
		
			appStatus.setText(data[0]);
			comments.setText(data[1]);
		}
		if(date != null) {
		
			dateApplied.setText(date[0]+"/"+date[1]+"/"+date[2]);
		}
	}
}
