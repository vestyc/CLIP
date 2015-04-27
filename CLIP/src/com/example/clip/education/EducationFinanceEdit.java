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
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView.OnItemSelectedListener;

public class EducationFinanceEdit extends Activity implements OnItemSelectedListener {

	ArrayAdapter<CharSequence> spinnerAdapter;
	Spinner financeType;
	
	EditText title, funds, upkeep, comments;
	
	String financeName;
	String[] dataString;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_finance_edit);
		
		financeType = (Spinner) findViewById(R.id.educationFinance_spinnerType);
		title = (EditText) findViewById(R.id.educationFinance_name);
		funds = (EditText) findViewById(R.id.educationFinance_funds);
		upkeep = (EditText) findViewById(R.id.educationFinance_upkeep);
		comments = (EditText) findViewById(R.id.educationFinance_comments);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.education_financeType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		financeType.setAdapter(spinnerAdapter);
		
		//edit option
		if(getIntent().getStringExtra("financeName") != null) {
			
			financeName = getIntent().getStringExtra("financeName");
			dataString = getIntent().getStringArrayExtra("dataString");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", financeName);
			
			title.setText(financeName);
			
			//sets the current financeType				
			String[] spinnerValues = getResources().getStringArray(R.array.education_financeType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString[0])) {
					
					this.financeType.setSelection(i);
				}
			}
			
			funds.setText(dataString[1]);
			upkeep.setText(dataString[2]);
			comments.setText(dataString[3]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[4];
			dataString[0] = "Loan";			//financeType is Loan by default
		}
		
		financeType.setOnItemSelectedListener(this);
	}
	
	public void saveClicked(View view) {
		
		financeName = title.getText().toString();
		iReturn.putExtra("financeName", financeName);
		
		//{financeType, funds, upkeep, comments/notes}
		dataString[1] = funds.getText().toString();
		dataString[2] = upkeep.getText().toString();
		dataString[3] = comments.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
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
		getMenuInflater().inflate(R.menu.education_finance_edit, menu);
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
