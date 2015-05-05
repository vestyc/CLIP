package com.example.clip.career;

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

public class CareerCompInfoDetail extends Activity {
	
	TextView name, productLOB, location, phone, email, facts, consideration, resumeDate, interviewDate,
		interviewOutcome, interviewLessons;
	
	TextView productLOBLabel, locationLabel, phoneLabel, emailLabel, factsLabel, considerationLabel,
		resumeDateLabel, interviewDateLabel, interviewOutcomeLabel, interviewLessonsLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_comp_info_detail);
		
		this.assignViews();
		
		Intent i = getIntent();
		String name = i.getStringExtra("name");
		String[] data = i.getStringArrayExtra("data");
		int[] resumeDate = i.getIntArrayExtra("resumeDate");
		int[] interviewDate = i.getIntArrayExtra("interviewDate");
		
		//{0           1         2      3      4      5                    6                 7               }
		//{productLOB, location, phone, email, facts, considerationReason, interviewOutcome, interviewLessons}
		this.name.setText(name);
		
		if(!data[0].equals("")) {
			this.productLOB.setText(data[0]);
			this.productLOB.setVisibility(View.VISIBLE);
			this.productLOBLabel.setVisibility(View.VISIBLE);
		}
		if(!data[1].equals("")) {
			this.location.setText(data[1]);
			this.location.setVisibility(View.VISIBLE);
			this.locationLabel.setVisibility(View.VISIBLE);
		}
		if(!data[2].equals("")) {
			this.phone.setText(data[2]);
			this.phone.setVisibility(View.VISIBLE);
			this.phoneLabel.setVisibility(View.VISIBLE);
		}
		if(!data[3].equals("")) {
			this.email.setText(data[3]);
			this.email.setVisibility(View.VISIBLE);
			this.emailLabel.setVisibility(View.VISIBLE);
		}
		if(!data[4].equals("")) {
			this.facts.setText(data[4]);
			this.facts.setVisibility(View.VISIBLE);
			this.factsLabel.setVisibility(View.VISIBLE);
		}
		if(!data[5].equals("")) {
			this.consideration.setText(data[5]);
			this.consideration.setVisibility(View.VISIBLE);
			this.considerationLabel.setVisibility(View.VISIBLE);
		}
		if(!data[6].equals("")) {
			this.interviewOutcome.setText(data[6]);
			this.interviewOutcome.setVisibility(View.VISIBLE);
			this.interviewOutcomeLabel.setVisibility(View.VISIBLE);
		}
		if(!data[7].equals("")) {
			this.interviewLessons.setText(data[7]);
			this.interviewLessons.setVisibility(View.VISIBLE);
			this.interviewLessonsLabel.setVisibility(View.VISIBLE);
		}
		
		if(resumeDate != null) {
			this.resumeDate.setText(resumeDate[0] + "/" + resumeDate[1] + "/" + resumeDate[2]);
			this.resumeDate.setVisibility(View.VISIBLE);
			this.resumeDateLabel.setVisibility(View.VISIBLE);
		}
		if(interviewDate != null) {
			this.interviewDate.setText(interviewDate[0] + "/" + interviewDate[1] + "/" + interviewDate[2]);
			this.interviewDate.setVisibility(View.VISIBLE);
			this.interviewDateLabel.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
	
	private void assignViews() {
		
		//view assignments
		name = (TextView) findViewById(R.id.compInfo_detailName);
		productLOB = (TextView) findViewById(R.id.compInfo_detailProductLOB);
		location = (TextView) findViewById(R.id.compInfo_detailLocation);
		phone = (TextView) findViewById(R.id.compInfo_detailPhone);
		email = (TextView) findViewById(R.id.compInfo_detailEmail);
		facts = (TextView) findViewById(R.id.compInfo_detailFacts);
		consideration = (TextView) findViewById(R.id.compInfo_detailConsideration);
		resumeDate = (TextView) findViewById(R.id.compInfo_detailResumeDate);
		interviewDate = (TextView) findViewById(R.id.compInfo_detailInterviewDate);
		interviewOutcome = (TextView) findViewById(R.id.compInfo_detailInterviewOutcome);
		interviewLessons = (TextView) findViewById(R.id.compInfo_detailInterviewLessons);
		
		productLOBLabel = (TextView) findViewById(R.id.compInfo_labelProductLOB);
		locationLabel = (TextView) findViewById(R.id.compInfo_labelLocation);
		phoneLabel = (TextView) findViewById(R.id.compInfo_labelPhone);
		emailLabel = (TextView) findViewById(R.id.compInfo_labelEmail);
		factsLabel = (TextView) findViewById(R.id.compInfo_labelFacts);
		considerationLabel = (TextView) findViewById(R.id.compInfo_labelConsideration);
		resumeDateLabel = (TextView) findViewById(R.id.compInfo_labelResumeDate);
		interviewDateLabel = (TextView) findViewById(R.id.compInfo_labelInterviewDate);
		interviewOutcomeLabel = (TextView) findViewById(R.id.compInfo_labelInterviewOutcome);
		interviewLessonsLabel = (TextView) findViewById(R.id.compInfo_labelInterviewLessons);
	}
}
