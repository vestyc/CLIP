package com.example.clip.education;

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

public class EducationGraduateEdit extends Activity implements OnItemSelectedListener {

	EditText name, location, comments, outcome;
	CheckBox dateCheckbox;
	DatePicker datePicker;
	
	ArrayAdapter<CharSequence> spinnerActionAdapter, spinnerPlanAdapter, spinnerDegreeAdapter;
	Spinner action, plan, degree;
	
	String graduateName;
	String[] dataString;
	int[] dataInt;
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_graduate_edit);
		
		name = (EditText) findViewById(R.id.educationGraduate_editName);
		location = (EditText) findViewById(R.id.educationGraduate_editLocation);
		comments = (EditText) findViewById(R.id.educationGraduate_editComments);
		outcome = (EditText) findViewById(R.id.educationGraduate_editOutcome);
		action = (Spinner) findViewById(R.id.educationGraduate_spinnerActionType);
		plan = (Spinner) findViewById(R.id.educationGraduate_spinnerPlanType);
		degree = (Spinner) findViewById(R.id.educationGraduate_spinnerDegreeType);
		dateCheckbox = (CheckBox) findViewById(R.id.educationGraduate_checkboxDate);
		datePicker = (DatePicker) findViewById(R.id.educationGraduate_date);
		
		//spinners
		spinnerActionAdapter = ArrayAdapter.createFromResource(this,
		        R.array.educationGraduate_actionType, android.R.layout.simple_spinner_item);
		spinnerActionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		action.setAdapter(spinnerActionAdapter);
		
		spinnerDegreeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.educationGraduate_degreeType, android.R.layout.simple_spinner_item);
		spinnerDegreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		degree.setAdapter(spinnerDegreeAdapter);
		
		spinnerPlanAdapter = ArrayAdapter.createFromResource(this,
		        R.array.educationGraduate_planType, android.R.layout.simple_spinner_item);
		spinnerPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		plan.setAdapter(spinnerPlanAdapter);
		
		//edit option
		if(getIntent().getStringExtra("graduateName") != null) {
			
			graduateName = getIntent().getStringExtra("graduateName");
			dataString = getIntent().getStringArrayExtra("dataString");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", graduateName);
			this.name.setText(graduateName);
			
			location.setText(dataString[3]);
			
			//actionType, degreeType, planType, location, applicOutcome, comments
			String[] spinnerValues = getResources().getStringArray(R.array.educationGraduate_actionType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString[0])) {
					
					this.action.setSelection(i);
				}
			}
			
			spinnerValues = getResources().getStringArray(R.array.educationGraduate_degreeType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString[1])) {
					
					this.degree.setSelection(i);
				}
			}	
			
			spinnerValues = getResources().getStringArray(R.array.educationGraduate_planType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString[2])) {
					
					this.plan.setSelection(i);
				}
			}
			
			if(getIntent().getIntArrayExtra("dataInt") != null) {
				
				dataInt = getIntent().getIntArrayExtra("dataInt");
				datePicker.updateDate(dataInt[2], dataInt[0], dataInt[1]);
				this.dateCheckbox.setChecked(true);
				datePicker.setVisibility(View.VISIBLE);
			}
			else {
				
				dataInt = new int[3];
			}
			
			outcome.setText(dataString[4]);
			comments.setText(dataString[5]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[6];	//actionType, degreeType, planType, location, applicOutcome, comments
			dataInt = new int[3];		//month, day, year
			
			//defaults
			dataString[0] = "In consideration";
			dataString[1] = "Masters";
			dataString[2] = "Part-time";
		}
		
		action.setOnItemSelectedListener(this);
		degree.setOnItemSelectedListener(this);
		plan.setOnItemSelectedListener(this);
	}
	
	public void saveClicked(View view) {
		
		graduateName = name.getText().toString();
		iReturn.putExtra("graduateName", graduateName);
		
		dataString[3] = location.getText().toString();
		dataString[4] = outcome.getText().toString();
		dataString[5] = comments.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		if(dateCheckbox.isChecked()) {
			dataInt[0] = 1 + datePicker.getMonth();
			dataInt[1] = datePicker.getDayOfMonth();
			dataInt[2] = datePicker.getYear();
			iReturn.putExtra("dataInt", dataInt);
		}
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	public void onDateCheckboxClicked(View view) {
		
		if(this.dateCheckbox.isChecked()) {
			
			this.datePicker.setVisibility(view.VISIBLE);
		}
		else {
			
			this.datePicker.setVisibility(view.GONE);
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		
		String item = (String) parent.getItemAtPosition(position);
		String[] spinnerActionValues = getResources().getStringArray(R.array.educationGraduate_actionType);
		String[] spinnerDegreeValues = getResources().getStringArray(R.array.educationGraduate_degreeType);
		String[] spinnerPlanValues = getResources().getStringArray(R.array.educationGraduate_planType);
		
		//actionType, degreeType, planType, location, applicOutcome
		for(String arrayValue : spinnerActionValues) {
			
			if(item.equals(arrayValue)) {
				
				dataString[0] = item;
				return;
			}
		}
		for(String arrayValue : spinnerDegreeValues) {
			
			if(item.equals(arrayValue)) {
				
				dataString[1] = item;
				return;
			}
		}
		for(String arrayValue : spinnerPlanValues) {
			
			if(item.equals(arrayValue)) {
				
				dataString[2] = item;
				return;
			}
		}
	}
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}
}
