package com.example.clip.health;

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

public class HealthExerciseDetail extends Activity {

	TextView sun, mon, tues, wed, thurs, fri, sat, title, date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_exercise_detail);
		
		title = (TextView) findViewById(R.id.healthExercise_detailName);
		sun = (TextView) findViewById(R.id.healthExercise_detailSunday);
		mon = (TextView) findViewById(R.id.healthExercise_detailMonday);
		tues = (TextView) findViewById(R.id.healthExercise_detailTuesday);
		wed = (TextView) findViewById(R.id.healthExercise_detailWednesday);
		thurs = (TextView) findViewById(R.id.healthExercise_detailThursday);
		fri = (TextView) findViewById(R.id.healthExercise_detailFriday);
		sat = (TextView) findViewById(R.id.healthExercise_detailSaturday);
		date = (TextView) findViewById(R.id.healthExercise_detailDate);
		
		Intent i = getIntent();
		String exerciseName = i.getStringExtra("exerciseName");
		String[] dataString = i.getStringArrayExtra("dataString");
		int[] dateStart = i.getIntArrayExtra("dateStart");
		int[] dateEnd = i.getIntArrayExtra("dateEnd");
		
		title.setText(exerciseName);
		
		if(!dataString[0].equals(""))
			sun.setText(dataString[0]);
		else
			sun.setText("None");
		
		if(!dataString[1].equals(""))
			mon.setText(dataString[1]);
		else
			mon.setText("None");
		
		if(!dataString[2].equals(""))
			tues.setText(dataString[2]);
		else
			tues.setText("None");
		
		if(!dataString[3].equals(""))
			wed.setText(dataString[3]);
		else
			wed.setText("None");
		
		if(!dataString[4].equals(""))
			thurs.setText(dataString[4]);
		else
			thurs.setText("None");
		
		if(!dataString[5].equals(""))
			fri.setText(dataString[5]);
		else
			fri.setText("None");
		
		if(!dataString[6].equals(""))
			sat.setText(dataString[6]);
		else
			sat.setText("None");
		
		date.setText(dateStart[0] + "/" + dateStart[1] + "/" + dateStart[2]  + " to " +
				dateEnd[0] + "/" + dateEnd[1] + "/" + dateEnd[2]);
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
}
