package com.example.clip.health;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.*;

public class HealthDietDetail extends Activity {

	TextView name, type, date, sun, mon, tues, wed, thurs, fri, sat;
	LinearLayout schedule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_diet_detail);
		
		name = (TextView) this.findViewById(R.id.healthDiet_detailName);
		type = (TextView) this.findViewById(R.id.healthDiet_detailType);
		date = (TextView) this.findViewById(R.id.healthDiet_detailDate);
		sun = (TextView) this.findViewById(R.id.healthDiet_detailSunday);
		mon = (TextView) this.findViewById(R.id.healthDiet_detailMonday);
		tues = (TextView) this.findViewById(R.id.healthDiet_detailTuesday);
		wed = (TextView) this.findViewById(R.id.healthDiet_detailWednesday);
		thurs = (TextView) this.findViewById(R.id.healthDiet_detailThursday);
		fri = (TextView) this.findViewById(R.id.healthDiet_detailFriday);
		sat = (TextView) this.findViewById(R.id.healthDiet_detailSaturday);
		schedule = (LinearLayout) this.findViewById(R.id.healthDiet_layoutSchedule);
		
		Intent i = getIntent();
		String dietName = i.getStringExtra("dietName");
		String[] dataString = i.getStringArrayExtra("dataString");
		String[] dataString2 = i.getStringArrayExtra("dataString2");
		int[] dateStart = i.getIntArrayExtra("dateStart");
		int[] dateEnd = i.getIntArrayExtra("dateEnd");
		
		name.setText(dietName);
		if(dataString[0].equals("") &&
			dataString[1].equals("") &&
			dataString[2].equals("") &&
			dataString[3].equals("") &&
			dataString[4].equals("") &&
			dataString[5].equals("") &&
			dataString[6].equals("") ) {
			
			schedule.setVisibility(View.GONE);
		}
		else {
			
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
		}
		
		date.setText(dateStart[0] + "/" + dateStart[1] + "/" + dateStart[2]  + " to " +
				dateEnd[0] + "/" + dateEnd[1] + "/" + dateEnd[2]);
		
		//dietType, notes/instructions
		this.type.setText(dataString2[0]);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_diet_detail, menu);
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
