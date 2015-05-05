package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

public class CareerGoalDetail extends Activity {

	String name;
	String[] data;
	TextView goalType, goalDate, goalName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_goal_detail);
		
		goalName = (TextView) findViewById(R.id.goalName);
		goalType = (TextView) findViewById(R.id.goalType);
		goalDate = (TextView) findViewById(R.id.dateToComplete);		
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		goalName.setText(name);
		goalType.setText(data[0]);
		goalDate.setText("Complete by:\n" + data[1]);
	}
}
