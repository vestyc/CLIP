package com.example.clip.finance;

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

public class financeGoalDetail extends Activity {

	String name;
	String[] data;
	TextView goalType, goalDate, goalName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_goal_detail);
		
		goalName = (TextView) findViewById(R.id.goalName);
		goalType = (TextView) findViewById(R.id.goalType);
		goalDate = (TextView) findViewById(R.id.dateToComplete);		
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		goalName.setText(name);
		goalType.setText(data[0]);
		goalDate.setText("Complete by:\n" + data[1]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_goal_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			return true;
		}
		else if(id == R.id.action_remove) {
			
			return true;
		}
			
		return super.onOptionsItemSelected(item);
	}
}
