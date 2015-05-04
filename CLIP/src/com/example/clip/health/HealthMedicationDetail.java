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
import android.widget.TextView;

public class HealthMedicationDetail extends Activity {

	TextView name, date, instruction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_medication_detail);
		
		name = (TextView) this.findViewById(R.id.healthMedication_detailName);
		instruction = (TextView) this.findViewById(R.id.healthMedication_detailInstruction);
		date = (TextView) this.findViewById(R.id.healthMedication_detailDate);
		
		Intent i = getIntent();
		int[] dateS = i.getIntArrayExtra("dateStart");
		int[] dateE = i.getIntArrayExtra("dateEnd");
		String medicationName = i.getStringExtra("medicationName");
		String dataString = i.getStringExtra("dataString");
		name.setText(medicationName);
		instruction.setText(dataString);
		date.setText(dateS[0] + "/" + dateS[1] + "/" + dateS[2] + "  to  " + 
					dateE[0] + "/" + dateE[1] + "/" + dateE[2]);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
}
