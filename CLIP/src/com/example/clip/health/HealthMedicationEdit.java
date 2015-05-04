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

public class HealthMedicationEdit extends Activity {

	EditText name, instruction;
	DatePicker dateStartPicker, dateEndPicker;
	
	Intent iReturn;
	String medicationName;
	String dataString;
	int[] dateStart, dateEnd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_medication_edit);
		
		name = (EditText) this.findViewById(R.id.healthMedication_editName);
		instruction = (EditText) this.findViewById(R.id.healthMedication_editInstruction);
		dateStartPicker = (DatePicker) this.findViewById(R.id.healthMedication_dateStart);
		dateEndPicker = (DatePicker) this.findViewById(R.id.healthMedication_dateEnd);
		
		//edit option
		if(getIntent().getStringExtra("medicationName") != null) {
			
			medicationName = getIntent().getStringExtra("medicationName");
			dataString = getIntent().getStringExtra("dataString");
			dateStart = getIntent().getIntArrayExtra("dateStart");
			dateEnd = getIntent().getIntArrayExtra("dateEnd");
			
			iReturn = new Intent();
			iReturn.putExtra("oldName", medicationName);
			name.setText(medicationName);
			
			instruction.setText(dataString);
						
			dateStartPicker.updateDate(dateStart[2], dateStart[0] - 1, dateStart[1]);
			
			dateEndPicker.updateDate(dateEnd[2], dateEnd[0] - 1, dateEnd[1]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dateStart = new int[3];
			dateEnd = new int[3];
		}
	}
	
	public void saveClicked(View view) {
		
		medicationName = name.getText().toString();
		iReturn.putExtra("medicationName", medicationName);
		
		dataString = this.instruction.getText().toString();
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
}
