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
	EditText jobName, jobComments;
	Button saveButton;
	Spinner status;
	//DatePickerDialog datePicker;

	String name;
	String[] data;
	int[] dataDate;
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_job_app_edit);
		
		iReturn = new Intent();
		
		jobName = (EditText) findViewById(R.id.jobName);
		jobComments = (EditText) findViewById(R.id.jobComments);
		saveButton = (Button) findViewById(R.id.job_buttonSave);		
		status = (Spinner) findViewById(R.id.job_statusSpinner);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.job_status, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		status.setAdapter(spinnerAdapter);
		
		//datePicker = new DatePickerDialog(this, )
		
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
		else {
			
			data = new String[3];
			dataDate = new int[3];	//{month, day, year}
		}
	}
	
	public void saveClicked(View view) {
			
		// data = {dateApplied, appStatus, comments}
		//data[0] = 
		//i.putExtra("data", this.data);
		//i.putExtra("name", this.goalName.getText().toString());
		//this.setResult(RESULT_OK, i);
		
		this.finish();
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
        data[1] = (String) parent.getItemAtPosition(pos);
    }

	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
	
	/*
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
	  
		dataDate[0] = month;
		dataDate[1] = day;
		dataDate[2] = year;
	}
	
	public void showDatePickerDialog(View view) {
	
		DialogFragment newFragment = new DialogFragment();
		newFragment.show(new FragmentTransaction(), "datePicker");
	}
	*/
}
