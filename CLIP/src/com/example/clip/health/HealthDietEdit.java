package com.example.clip.health;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class HealthDietEdit extends Activity implements OnItemSelectedListener {

	EditText title, sun, mon, tues, wed, thurs, fri, sat;
	EditText notes;
	DatePicker dateStartPicker, dateEndPicker;
	CheckBox checkbox;
	LinearLayout schedule;

	ArrayAdapter<CharSequence> spinnerAdapter;
	Spinner type;
	
	String dietName;
	String[] dataString;
	String[] dataString2;
	int[] dateStart, dateEnd;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_diet_edit);
		
		title = (EditText) findViewById(R.id.healthDiet_editTitle);
		sun = (EditText) findViewById(R.id.healthDiet_editSunday);
		mon = (EditText) findViewById(R.id.healthDiet_editMonday);
		tues = (EditText) findViewById(R.id.healthDiet_editTuesday);
		wed = (EditText) findViewById(R.id.healthDiet_editWednesday);
		thurs = (EditText) findViewById(R.id.healthDiet_editThursday);
		fri = (EditText) findViewById(R.id.healthDiet_editFriday);
		sat = (EditText) findViewById(R.id.healthDiet_editSaturday);
		dateStartPicker = (DatePicker) findViewById(R.id.healthDiet_dateStart);
		dateEndPicker = (DatePicker) findViewById(R.id.healthDiet_dateEnd);
		type = (Spinner) findViewById(R.id.healthDiet_spinnerType);
		notes = (EditText) findViewById(R.id.healthDiet_editNotes);
		checkbox = (CheckBox) findViewById(R.id.healthDiet_checkbox);
		schedule = (LinearLayout) findViewById(R.id.healthDiet_layoutSchedule);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.healthDiet_type, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		type.setAdapter(spinnerAdapter);
		
		//edit option
		if(getIntent().getStringExtra("dietName") != null) {
			
			dietName = getIntent().getStringExtra("dietName");
			dataString = getIntent().getStringArrayExtra("dataString");
			dataString2 = getIntent().getStringArrayExtra("dataString2");
			dateStart = getIntent().getIntArrayExtra("dateStart");
			dateEnd = getIntent().getIntArrayExtra("dateEnd");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", dietName);
			title.setText(dietName);
			
			schedule.setVisibility(View.VISIBLE);
			checkbox.setChecked(true);
			sun.setText(dataString[0]);
			mon.setText(dataString[1]);
			tues.setText(dataString[2]);
			wed.setText(dataString[3]);
			thurs.setText(dataString[4]);
			fri.setText(dataString[5]);
			sat.setText(dataString[6]);
			
			String[] spinnerValues = getResources().getStringArray(R.array.healthDiet_type);
			for(int i = 0; i < spinnerValues.length; i++) {
				
				if(spinnerValues[i].equals(dataString2[0])) {
					
					this.type.setSelection(i);
				}
			}
			notes.setText(dataString2[1]);
			
			dateStartPicker.updateDate(dateStart[2], dateStart[0] - 1, dateStart[1]);
			
			dateEndPicker.updateDate(dateEnd[2], dateEnd[0] - 1, dateEnd[1]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[7];
			dataString2 = new String[2];
			dateStart = new int[3];
			dateEnd = new int[3];
			
			dataString2[0] = "Low Carb";	//default spinner value
		}
	}
	
	public void saveClicked(View view) {
		
		dietName = title.getText().toString();
		iReturn.putExtra("dietName", dietName);
		
		if(checkbox.isChecked()) {
			dataString[0] = sun.getText().toString();
			dataString[1] = mon.getText().toString();
			dataString[2] = tues.getText().toString();
			dataString[3] = wed.getText().toString();
			dataString[4] = thurs.getText().toString();
			dataString[5] = fri.getText().toString();
			dataString[6] = sat.getText().toString();
			iReturn.putExtra("dataString", dataString);
		}
		else {
			dataString = new String[7];	//empty
			for(int i=0; i<dataString.length; i++) {
				dataString[i] = "";
			}
			iReturn.putExtra("dataString", dataString);
		}
		
		dataString2[1] = notes.getText().toString();
		iReturn.putExtra("dataString2", dataString2);
		
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
	
	public void checkboxClicked(View view) {
		
		if(checkbox.isChecked()) {
			
			schedule.setVisibility(view.VISIBLE);
		}
		else {
			
			schedule.setVisibility(view.GONE);
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        dataString2[0] = (String) parent.getItemAtPosition(pos);
    }
	
	@Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_diet_edit, menu);
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
