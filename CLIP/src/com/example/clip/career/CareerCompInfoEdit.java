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
			
			String name = getIntent().getStringExtra("name");
			String[] data = getIntent().getStringArrayExtra("data");
			int[] resumeDate = getIntent().getIntArrayExtra("resumeDate");
			int[] interviewDate = getIntent().getIntArrayExtra("interviewDate");
			
			//{0           1         2      3      4      5                    6                 7               }
			//{productLOB, location, phone, email, facts, considerationReason, interviewOutcome, interviewLessons}
			this.editName.setText(name);
			this.editProductLOB.setText(data[0]);
			this.editLocation.setText(data[1]);
			this.editPhone.setText(data[2]);
			this.editEmail.setText(data[3]);
			this.editFacts.setText(data[4]);
			this.editConsiderations.setText(data[5]);
			
			if(resumeDate[0] != 0 && resumeDate[1] != 0 && resumeDate[2] != 0) {
				
				this.checkResumeDate.setChecked(true);
				this.pickerResume.setVisibility(View.VISIBLE);
				this.pickerResume.updateDate(resumeDate[2], resumeDate[0], resumeDate[1]);
			}
			
			if(interviewDate[0] != 0 && interviewDate[1] != 0 && interviewDate[2] != 0) {
				
				this.checkInterviewDate.setChecked(true);
				this.pickerInterview.setVisibility(View.VISIBLE);
				this.pickerInterview.updateDate(interviewDate[2], interviewDate[0], interviewDate[1]);
			}
			
			this.editInterviewOutcome.setText(data[6]);
			this.editInterviewLessons.setText(data[7]);
			
			this.data = data;
			this.resumeDate = resumeDate;
			this.interviewDate = interviewDate;
			iReturn = new Intent();
			iReturn.putExtra("oldName", name);
		}
		//add option
		else {
			
			data = new String[8];
			resumeDate = new int[3];
			interviewDate = new int[3];
			iReturn = new Intent();
		}
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
			
			this.resumeDate[0] = this.pickerResume.getMonth() + 1;
			this.resumeDate[1] = this.pickerResume.getDayOfMonth();
			this.resumeDate[2] = this.pickerResume.getYear();
			iReturn.putExtra("resumeDate", resumeDate);
		}		
		if(this.checkInterviewDate.isChecked()) {
			
			this.interviewDate[0] = this.pickerInterview.getMonth() + 1;
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
}
