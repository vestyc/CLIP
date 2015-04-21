package com.example.clip.career;

import com.example.clip.Entry;
import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;

public class CareerGoalEdit extends Activity {

	EditText goalName, goalDate;
	Button save;
	String[] data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_goal_edit);
		
		goalName = (EditText) findViewById(R.id.goalName);
		goalDate = (EditText) findViewById(R.id.goalDate);
		save = (Button) findViewById(R.id.buttonSave);
		
		data = new String[2];			//{goalLength, goalDate}
		data[0] = "Short term goal.";	//selected by default
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_goal_edit, menu);
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
	
	public void saveClicked(View view) {
		
		Intent i = new Intent();
		i.putExtra("data", this.data);
		i.putExtra("name", this.goalName.getText().toString());
		
		this.setResult(RESULT_OK, i);
		
		this.finish();
	}
	
	public void onRadioButtonClicked(View view) {
		
		//See if the button is checked
		boolean checked = ((RadioButton) view).isChecked();
		
		//See which button was clicked
		switch(view.getId()) {
		
		case R.id.goalLengthLong:
			
			if(checked) {
				
				data[0] = "Long term goal.";
			}
			break;
			
		case R.id.goalLengthShort:
			
			if(checked) {
				
				data[0] = "Short term goal.";
			}
			break;
		}
	}
}
