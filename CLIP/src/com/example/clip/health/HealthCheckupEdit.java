package com.example.clip.health;

import java.util.Calendar;

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
import android.widget.EditText;
import android.widget.NumberPicker;

public class HealthCheckupEdit extends Activity {

	NumberPicker goodChol, badChol, totalChol, bp0, bp1, glucose;
	EditText other;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_checkup_edit);
		
		goodChol = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerCholHDL);
		badChol = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerCholLDL);
		totalChol = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerCholTotal);
		bp0 = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerBP0);
		bp1 = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerBP1);
		glucose = (NumberPicker) this.findViewById(R.id.healthCheckup_pickerGlucose);
		other = (EditText) this.findViewById(R.id.healthCheckup_editOther);
		
		goodChol.setMinValue(0);
		goodChol.setMaxValue(200);
		goodChol.setWrapSelectorWheel(false);
		
		badChol.setMinValue(0);
		badChol.setMaxValue(200);
		badChol.setWrapSelectorWheel(false);
		
		totalChol.setMinValue(0);
		totalChol.setMaxValue(300);
		totalChol.setWrapSelectorWheel(false);
		
		bp0.setMinValue(20);
		bp0.setMaxValue(200);
		bp0.setWrapSelectorWheel(false);
		bp1.setMinValue(20);
		bp1.setMaxValue(120);
		bp1.setWrapSelectorWheel(false);
		
		glucose.setMinValue(0);
		glucose.setMaxValue(200);
		glucose.setWrapSelectorWheel(false);
		
		//has been updated before
		if(getIntent().getStringExtra("goodChol") != null) {
			
			Intent i = getIntent();
			goodChol.setValue(Integer.parseInt(i.getStringExtra("goodChol")));
			badChol.setValue(Integer.parseInt(i.getStringExtra("badChol")));
			totalChol.setValue(Integer.parseInt(i.getStringExtra("totalChol")));
			bp0.setValue(Integer.parseInt(i.getStringExtra("bp0")));
			bp1.setValue(Integer.parseInt(i.getStringExtra("bp1")));
			glucose.setValue(Integer.parseInt(i.getStringExtra("glucose")));
			other.setText(i.getStringExtra("other"));
		}
		//first time updating
		else {
			
			//set to "average" values
			goodChol.setValue(60);
			badChol.setValue(100);
			totalChol.setValue(200);
			bp0.setValue(130);
			bp1.setValue(85);
			glucose.setValue(120);
		}
		
		iReturn = new Intent();
	}
	
	public void saveClicked(View view) {
		
		iReturn.putExtra("goodChol", Integer.toString(goodChol.getValue()));
		iReturn.putExtra("badChol", Integer.toString(badChol.getValue()));
		iReturn.putExtra("totalChol", Integer.toString(totalChol.getValue()));
		iReturn.putExtra("bp0", Integer.toString(bp0.getValue()));
		iReturn.putExtra("bp1", Integer.toString(bp1.getValue()));
		iReturn.putExtra("glucose", Integer.toString(glucose.getValue()));
		iReturn.putExtra("other", other.getText().toString());
		
		Calendar currentTime = Calendar.getInstance();
		int[] lastUpdate = new int[5]; //hour, minute, month, day, year
		lastUpdate[0] = currentTime.get(Calendar.HOUR_OF_DAY);
		lastUpdate[1] = currentTime.get(Calendar.MINUTE);
		lastUpdate[2] = currentTime.get(Calendar.MONTH) + 1;
		lastUpdate[3] = currentTime.get(Calendar.DAY_OF_MONTH);
		lastUpdate[4] = currentTime.get(Calendar.YEAR);
		iReturn.putExtra("lastUpdate", lastUpdate);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
}
