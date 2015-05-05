package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Intent;

public class EducationCurrentEdit extends Activity implements OnItemSelectedListener {

	ArrayAdapter<CharSequence> spinnerAdapter;
	Spinner type;
	
	EditText name;
	
	DatePicker dateStartPicker, dateEndPicker;
	
	Intent iReturn;
	String currentName;
	String dataString;
	int[] dateStart, dateEnd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_current_edit);
		
		type = (Spinner) findViewById(R.id.educationCurrent_spinnerType);
		name = (EditText) findViewById(R.id.educationCurrent_name);
		dateStartPicker = (DatePicker) findViewById(R.id.educationCurrent_dateStart);
		dateEndPicker = (DatePicker) findViewById(R.id.educationCurrent_dateEnd);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.educationCurrent_degreeType, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		type.setAdapter(spinnerAdapter);
		
		//edit option
		if(getIntent().getStringExtra("currentName") != null) {
			
			currentName = getIntent().getStringExtra("currentName");
			dataString = getIntent().getStringExtra("dataString");
			dateStart = getIntent().getIntArrayExtra("dateStart");
			dateEnd = getIntent().getIntArrayExtra("dateEnd");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", currentName);
			name.setText(currentName);
			
			String[] spinnerValues = getResources().getStringArray(R.array.educationCurrent_degreeType);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString)) {
					
					this.type.setSelection(i);
				}
			}
			
			dateStartPicker.updateDate(dateStart[2], dateStart[0] - 1, dateStart[1]);
			
			dateEndPicker.updateDate(dateEnd[2], dateEnd[0] - 1, dateEnd[1]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String("Bachelors");	//default degree type
			dateStart = new int[3];
			dateEnd = new int[3];
		}
		
		type.setOnItemSelectedListener(this);
	}
	
	public void saveClicked(View view) {
		
		currentName = name.getText().toString();
		iReturn.putExtra("currentName", currentName);
		
		iReturn.putExtra("dataString", dataString);
		
		dateStart[0] = dateStartPicker.getMonth() + 1;
		dateStart[1] = dateStartPicker.getDayOfMonth();
		dateStart[2] = dateStartPicker.getYear();
		iReturn.putExtra("dateStart", dateStart);
		
		dateEnd[0] = dateEndPicker.getMonth() + 1;
		dateEnd[1] = dateEndPicker.getDayOfMonth();
		dateEnd[2] = dateEndPicker.getYear();
		iReturn.putExtra("dateEnd", dateEnd);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        dataString = (String) parent.getItemAtPosition(pos);
    }
	
	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
