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

public class CareerContactEdit extends Activity {

	EditText name, affiliation, comments;
	DatePicker date;
	NumberPicker uses;
	
	String contactName;
	String[] dataString;
	int[] dataInt;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_contact_edit);
		
		name = (EditText) findViewById(R.id.contact_name);
		affiliation = (EditText) findViewById(R.id.contact_affiliation);
		comments = (EditText) findViewById(R.id.contact_comments);
		date = (DatePicker) findViewById(R.id.contact_datePickerEstablished);
		uses = (NumberPicker) findViewById(R.id.contact_numberPickerUses);
		
		uses.setMaxValue(100);
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			contactName = getIntent().getStringExtra("name");
			iReturn = new Intent();
			iReturn.putExtra("oldName", contactName);
			
			name.setText(contactName);
			
			dataString = getIntent().getStringArrayExtra("dataString");
			dataInt = getIntent().getIntArrayExtra("dataInt");
			
			affiliation.setText(dataString[0]);
			comments.setText(dataString[1]);
			date.updateDate(dataInt[2], dataInt[0], dataInt[1]);
			uses.setValue(dataInt[3]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[2];
			dataInt = new int[4];
		}
	}
	
	public void saveClicked(View view) {
		
		contactName = this.name.getText().toString();
		iReturn.putExtra("name", contactName);
		
		//{affiliation, comments}
		if(this.affiliation.getText() != null)
			dataString[0] = this.affiliation.getText().toString();
		if(this.comments.getText() != null)
			dataString[1] = this.comments.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		//{estMonth, estDay, estYear, #OfUse}
		dataInt[0] = this.date.getMonth();
		dataInt[1] = this.date.getDayOfMonth();
		dataInt[2] = this.date.getYear();
		dataInt[3] = this.uses.getValue();
		iReturn.putExtra("dataInt", dataInt);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_contact_edit, menu);
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
