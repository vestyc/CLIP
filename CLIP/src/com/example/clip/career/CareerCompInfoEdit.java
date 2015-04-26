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
import android.content.Intent;
import android.view.View;

public class CareerCompInfoEdit extends Activity {

	Button buttonSave;
	EditText editName, editProductLOB, editLocation, editPhone, editEmail, editFacts,
		editConsiderations, editInterviewOutcome, editInterviewLessons;
	CheckBox checkInterviewDate, checkResumeDate;
	DatePicker pickerResume, pickerInterview;
	
	Intent iReturn;
	String[] data;
	int[] resumeDate, interviewDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_comp_info_edit);
		
		//layout assignments
		buttonSave = (Button) findViewById(R.id.compInfo_buttonSave);
		editName = (EditText) findViewById(R.id.compInfo_name);
		editProductLOB = (EditText) findViewById(R.id.compInfo_productLOB);
		editLocation = (EditText) findViewById(R.id.compInfo_location);
		editPhone = (EditText) findViewById(R.id.compInfo_phone);
		editEmail = (EditText) findViewById(R.id.compInfo_email);
		editFacts = (EditText) findViewById(R.id.compInfo_facts);
		editConsiderations = (EditText) findViewById(R.id.compInfo_consideration);
		editInterviewOutcome = (EditText) findViewById(R.id.compInfo_interviewOutcome);
		editInterviewLessons = (EditText) findViewById(R.id.compInfo_interviewLessons);
		checkInterviewDate = (CheckBox) findViewById(R.id.compInfo_checkboxInterviewDate);
		checkResumeDate = (CheckBox) findViewById(R.id.compInfo_checkboxResumeDate);
		pickerResume = (DatePicker) findViewById(R.id.compInfo_datePickerResume);
		pickerInterview = (DatePicker) findViewById(R.id.compInfo_datePickerInterview);
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			
		}
		//add option
		else {
			
			data = new String[8];
			resumeDate = new int[3];
			interviewDate = new int[3];
		}
		
		iReturn = new Intent();
	}
	
	public void saveClicked(View view) {
		
		iReturn.putExtra("name", this.editName.getText().toString());
		
		//{0           1         2      3      4      5                    6                 7               }
		//{productLOB, location, phone, email, facts, considerationReason, interviewOutcome, interviewLessons}
		if(this.editProductLOB.getText() != null)
			data[0] = this.editProductLOB.getText().toString();
		
		if(this.editLocation.getText() != null)
			data[1] = this.editLocation.getText().toString();
		
		if(this.editPhone.getText() != null)
			data[2] = this.editPhone.getText().toString();
		
		if(this.editEmail.getText() != null)
			data[3] = this.editEmail.getText().toString();
		
		if(this.editFacts.getText() != null)
			data[4] = this.editFacts.getText().toString();
		
		if(this.editConsiderations.getText() != null)
			data[5] = this.editConsiderations.getText().toString();
		
		if(this.editInterviewOutcome.getText() != null)
			data[6] = this.editInterviewOutcome.getText().toString();
		
		if(this.editInterviewLessons.getText() != null)
			data[7] = this.editInterviewLessons.getText().toString();
		iReturn.putExtra("data", this.data);
		
		//dateType = {resumeSubDate, interviewDate}
		//[dateType][month, day, year]		
		if(this.checkResumeDate.isChecked()) {
			
			this.resumeDate[0] = this.pickerResume.getMonth();
			this.resumeDate[1] = this.pickerResume.getDayOfMonth();
			this.resumeDate[2] = this.pickerResume.getYear();
			iReturn.putExtra("resumeDate", resumeDate);
		}		
		if(this.checkInterviewDate.isChecked()) {
			
			this.interviewDate[0] = this.pickerInterview.getMonth();
			this.interviewDate[1] = this.pickerInterview.getDayOfMonth();
			this.interviewDate[2] = this.pickerInterview.getYear();
			iReturn.putExtra("interviewDate", interviewDate);
		}
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	public void onInterviewCheckboxClicked(View view) {
		
		if(this.checkInterviewDate.isChecked()) {
			
			this.pickerInterview.setVisibility(View.VISIBLE);
		}
		else {
			
			this.pickerInterview.setVisibility(View.GONE);
		}
	}
	
	public void onResumeCheckboxClicked(View view) {
		
		if(this.checkResumeDate.isChecked()) {
			
			this.pickerResume.setVisibility(View.VISIBLE);
		}
		else {
			
			this.pickerResume.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_comp_info_edit, menu);
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
