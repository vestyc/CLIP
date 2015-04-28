package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;

public class EducationFutureDetail extends Activity {
	
	TextView[] courses;
	TextView title, type, school, location, comments;
	TextView schoolLabel, locationLabel, coursesLabel, commentsLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_future_detail);
		
		courses = new TextView[6];
		courses[0] = (TextView) findViewById(R.id.educationFuture_detailCourse0);
		courses[1] = (TextView) findViewById(R.id.educationFuture_detailCourse1);
		courses[2] = (TextView) findViewById(R.id.educationFuture_detailCourse2);
		courses[3] = (TextView) findViewById(R.id.educationFuture_detailCourse3);
		courses[4] = (TextView) findViewById(R.id.educationFuture_detailCourse4);
		courses[5] = (TextView) findViewById(R.id.educationFuture_detailCourse5);
		title = (TextView) findViewById(R.id.educationFuture_detailTitle);
		type = (TextView) findViewById(R.id.educationFuture_detailType);
		school = (TextView) findViewById(R.id.educationFuture_detailSchool);
		location = (TextView) findViewById(R.id.educationFuture_detailLocation);
		comments = (TextView) findViewById(R.id.educationFuture_detailComments);
		schoolLabel = (TextView) findViewById(R.id.educationFuture_labelSchool);
		locationLabel = (TextView) findViewById(R.id.educationFuture_labelLocation);
		coursesLabel = (TextView) findViewById(R.id.educationFuture_labelCourse);
		commentsLabel = (TextView) findViewById(R.id.educationFuture_labelComments);
		
		Intent i = getIntent();
		String futureName = i.getStringExtra("futureName");
		String[] dataString = i.getStringArrayExtra("dataString");
		ArrayList<String> courseList = i.getStringArrayListExtra("course");
		
		title.setText(futureName);
		//{planType, school, location, comments/notes}
		type.setText(dataString[0]);
		
		if(!dataString[1].equals("")) {
			school.setText(dataString[1]);
			school.setVisibility(View.VISIBLE);
			schoolLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[2].equals("")) {
			location.setText(dataString[2]);
			location.setVisibility(View.VISIBLE);
			locationLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[3].equals("")) {
			comments.setText(dataString[3]);
			comments.setVisibility(View.VISIBLE);
			commentsLabel.setVisibility(View.VISIBLE);
		}
		
		for(int k = 0; k < 6; k++) {
			
			if(!courseList.get(k).equals("")) {
				
				courses[k].setText(courseList.get(k));
				courses[k].setVisibility(View.VISIBLE);
				coursesLabel.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_future_detail, menu);
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
