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
	DatePicker datePicker;

	String name;	//jobName
	String[] data;	//{appStatus, comments}
	int[] dataDate;	//{month, day, year}
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_job_app_edit);
		
		iReturn = new Intent();
		
		jobName = (EditText) findViewById(R.id.jobName);
		status = (Spinner) findViewById(R.id.job_statusSpinner);
		jobComments = (EditText) findViewById(R.id.job_comments);
		saveButton = (Button) findViewById(R.id.job_buttonSave);		
		datePicker = (DatePicker) findViewById(R.id.job_datePicker);
		
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
			
			// data = {appStatus, comments}
			data = getIntent().getStringArrayExtra("data");
			
			//sets the current status
			if(data != null) {
				
				String[] spinnerValues = getResources().getStringArray(R.array.job_status);
				for(int i = 0; i < spinnerValues.length; i++) {
					
					if(spinnerValues[i].equals(data[0])) {
						
						this.status.setSelection(i);
					}
				}
				
				//set comments, if any
				if(data[1] != null) {
					
					this.jobComments.setText(data[1]);
				}
			}
			
			dataDate = getIntent().getIntArrayExtra("date");	//{Month, Day, Year}
			if(dataDate != null) {
				
				datePicker.updateDate(dataDate[2], dataDate[0] - 1, dataDate[1]);
			}
		}
		else {
			
			data = new String[2];	// data = {appStatus, comments}
			data[0] = "Pending";	//Pending is initially selected
			dataDate = new int[3];	//{month, day, year}
		}
		
		//listeners
		status.setOnItemSelectedListener(this);
	}
	
	public void saveClicked(View view) {
			
		iReturn.putExtra("name", jobName.getText().toString());
		
		data[1] = jobComments.getText().toString();
		iReturn.putExtra("data", data);
		
		//{month, day, year}
		dataDate[0] = 1 + datePicker.getMonth();
		dataDate[1] = datePicker.getDayOfMonth();
		dataDate[2] = datePicker.getYear();
		iReturn.putExtra("date", dataDate);
				
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        data[0] = (String) parent.getItemAtPosition(pos);
    }

	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
