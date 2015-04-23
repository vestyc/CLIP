package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.content.Intent;

public class CareerJobAppEdit extends Activity implements OnItemSelectedListener {

	ArrayAdapter<CharSequence> spinnerAdapter;
	EditText jobName;
	Button saveButton;
	Spinner status;

	String name;
	String[] data;
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_job_app_edit);
		
		iReturn = new Intent();
		
		jobName = (EditText) findViewById(R.id.jobName);
		saveButton = (Button) findViewById(R.id.job_buttonSave);
		
		status = (Spinner) findViewById(R.id.job_statusSpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.job_status, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		status.setAdapter(spinnerAdapter);
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			//Display current jobName
			jobName.setText(getIntent().getStringExtra("name"));
			iReturn.putExtra("oldName", jobName.getText().toString());
			
			// data = {dateApplied, appStatus, comments}
			data = getIntent().getStringArrayExtra("data");
			//set current date applied
			
			//--------
			
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_job_app_edit, menu);
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
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
