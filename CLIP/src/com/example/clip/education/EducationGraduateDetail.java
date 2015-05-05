package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;

public class EducationGraduateDetail extends Activity {

	TextView name, action, degree, plan, date, outcome, comments, location;
	TextView dateLabel, outcomeLabel, commentsLabel, locationLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_graduate_detail);
		
		name = (TextView) findViewById(R.id.educationGraduate_detailName);
		action = (TextView) findViewById(R.id.educationGraduate_detailAction);
		degree = (TextView) findViewById(R.id.educationGraduate_detailDegree);
		plan = (TextView) findViewById(R.id.educationGraduate_detailPlan);
		date = (TextView) findViewById(R.id.educationGraduate_detailDate);
		outcome = (TextView) findViewById(R.id.educationGraduate_detailOutcome);
		comments = (TextView) findViewById(R.id.educationGraduate_detailComments);
		location = (TextView) findViewById(R.id.educationGraduate_detailLocation);
		dateLabel = (TextView) findViewById(R.id.educationGraduate_labelDate);
		outcomeLabel = (TextView) findViewById(R.id.educationGraduate_labelOutcome);
		commentsLabel = (TextView) findViewById(R.id.educationGraduate_labelComments);
		locationLabel = (TextView) findViewById(R.id.educationGraduate_labelLocation);
		
		Intent i = getIntent();
		String graduateName = i.getStringExtra("graduateName");
		String[] dataString = i.getStringArrayExtra("dataString");
		int[] dataInt = i.getIntArrayExtra("dataInt");
		
		//actionType, degreeType, planType, location, applicOutcome, comments
		name.setText(graduateName);
		if(!dataString[0].equals("")) {
			action.setText(dataString[0]);
		}
		if(!dataString[1].equals("")) {
			degree.setText(dataString[1]);
		}
		if(!dataString[2].equals("")) {
			plan.setText(dataString[2]);
		}
		if(!dataString[3].equals("")) {
			this.location.setText(dataString[3]);
			this.location.setVisibility(View.VISIBLE);
			this.locationLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[4].equals("")) {
			this.outcome.setText(dataString[4]);
			this.outcome.setVisibility(View.VISIBLE);
			this.outcomeLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[5].equals("")) {
			this.comments.setText(dataString[5]);
			this.comments.setVisibility(View.VISIBLE);
			this.commentsLabel.setVisibility(View.VISIBLE);
		}
		
		if(dataInt != null) {
			this.date.setText(dataInt[0] + "/" + dataInt[1] + "/" + dataInt[2]);
			this.date.setVisibility(View.VISIBLE);
			this.dateLabel.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
}
