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
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.content.Intent;

public class EducationFutureEdit extends Activity implements OnItemSelectedListener {
	
	EditText title, school, location, comments;	
	EditText[] courses;
	
	ArrayAdapter<CharSequence> spinnerAdapter;
	Spinner type;
	
	String futureName;
	ArrayList<String> courseList;
	String[] dataString;
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_future_edit);
		
		courses = new EditText[6];
		courses[0] = (EditText) findViewById(R.id.educationFuture_course0);
		courses[1] = (EditText) findViewById(R.id.educationFuture_course1);
		courses[2] = (EditText) findViewById(R.id.educationFuture_course2);
		courses[3] = (EditText) findViewById(R.id.educationFuture_course3);
		courses[4] = (EditText) findViewById(R.id.educationFuture_course4);
		courses[5] = (EditText) findViewById(R.id.educationFuture_course5);
		title = (EditText) findViewById(R.id.educationFuture_title);
		school = (EditText) findViewById(R.id.educationFuture_school);
		location = (EditText) findViewById(R.id.educationFuture_location);
		comments = (EditText) findViewById(R.id.educationFuture_comments);
		type = (Spinner) findViewById(R.id.educationFuture_spinnerType);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.education_futureType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		type.setAdapter(spinnerAdapter);
		
		//edit option
		if(getIntent().getStringExtra("futureName") != null) {
			
			futureName = getIntent().getStringExtra("futureName");
			iReturn = new Intent();
			iReturn.putExtra("oldName", futureName);
			title.setText(futureName);
			
			//{planType, school, location, comments/notes}
			dataString = getIntent().getStringArrayExtra("dataString");
			//sets the current financeType				
			String[] spinnerValues = getResources().getStringArray(R.array.education_futureType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString[0])) {
					
					this.type.setSelection(i);
				}
			}
			school.setText(dataString[1]);
			location.setText(dataString[2]);
			comments.setText(dataString[3]);
			
			courseList = getIntent().getStringArrayListExtra("course");
			for(int i = 0; i < 6; i++) {
				
				if(!courseList.get(i).equals("")) {
					
					courses[i].setText(courseList.get(i));
					courses[i].setVisibility(View.VISIBLE);
				}
			}
		}
		//add option
		else {
			
			iReturn = new Intent();
			courseList = new ArrayList<String>();
			dataString = new String[4];
			dataString[0] = "Continuing Education";	//default spinner value
		}
	}
	
	public void saveClicked(View view) {
		
		futureName = title.getText().toString();
		iReturn.putExtra("futureName", futureName);
		
		for(int i = 0; i < 6; i++) {
			
			courseList.add(courses[i].getText().toString());
		}
		iReturn.putExtra("course", courseList);
				
		//{planType, school, location, comments/notes}
		dataString[1] = this.school.getText().toString();
		dataString[2] = this.location.getText().toString();
		dataString[3] = this.comments.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	public void courseAddClicked(View view) {
		
		for(int i = 0; i < 6; i++) {
			
			if(!courses[i].isShown()) {
				
				courses[i].setVisibility(view.VISIBLE);
				break;
			}
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        dataString[0] = (String) parent.getItemAtPosition(pos);
    }
	
	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_future_edit, menu);
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
