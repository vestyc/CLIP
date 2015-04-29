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
import android.widget.*;
import android.content.Intent;

public class HealthExerciseEdit extends Activity {
	
	EditText title, sun, mon, tues, wed, thurs, fri, sat;
	DatePicker dateStartPicker, dateEndPicker;

	String exerciseName;
	String[] dataString;
	int[] dateStart, dateEnd;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_exercise_edit);
		
		title = (EditText) findViewById(R.id.healthExercise_editTitle);
		sun = (EditText) findViewById(R.id.healthExercise_editSunday);
		mon = (EditText) findViewById(R.id.healthExercise_editMonday);
		tues = (EditText) findViewById(R.id.healthExercise_editTuesday);
		wed = (EditText) findViewById(R.id.healthExercise_editWednesday);
		thurs = (EditText) findViewById(R.id.healthExercise_editThursday);
		fri = (EditText) findViewById(R.id.healthExercise_editFriday);
		sat = (EditText) findViewById(R.id.healthExercise_editSaturday);
		dateStartPicker = (DatePicker) findViewById(R.id.healthExercise_dateStart);
		dateEndPicker = (DatePicker) findViewById(R.id.healthExercise_dateEnd);
		
		//edit option
		if(getIntent().getStringExtra("exerciseName") != null) {
			
			exerciseName = getIntent().getStringExtra("exerciseName");
			dataString = getIntent().getStringArrayExtra("dataString");
			dateStart = getIntent().getIntArrayExtra("dateStart");
			dateEnd = getIntent().getIntArrayExtra("dateEnd");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", exerciseName);
			title.setText(exerciseName);
			
			sun.setText(dataString[0]);
			mon.setText(dataString[1]);
			tues.setText(dataString[2]);
			wed.setText(dataString[3]);
			thurs.setText(dataString[4]);
			fri.setText(dataString[5]);
			sat.setText(dataString[6]);
			
			dateStartPicker.updateDate(dateStart[2], dateStart[0] - 1, dateStart[1]);
			
			dateEndPicker.updateDate(dateEnd[2], dateEnd[0] - 1, dateEnd[1]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[7];
			dateStart = new int[3];
			dateEnd = new int[3];
		}
	}
	
	public void saveClicked(View view) {
		
		exerciseName = title.getText().toString();
		iReturn.putExtra("exerciseName", exerciseName);
		
		dataString[0] = sun.getText().toString();
		dataString[1] = mon.getText().toString();
		dataString[2] = tues.getText().toString();
		dataString[3] = wed.getText().toString();
		dataString[4] = thurs.getText().toString();
		dataString[5] = fri.getText().toString();
		dataString[6] = sat.getText().toString();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_exercise_edit, menu);
		return true;
	}
}
