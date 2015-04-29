package com.example.clip.health;

import java.util.regex.Pattern;
import java.util.Calendar;

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

public class HealthVitalEdit extends Activity {

	NumberPicker temp0, temp1, pulse, resp, bp0, bp1;
	CheckBox tempCheck, pulseCheck, respCheck, bpCheck;
	LinearLayout tempLayout, bpLayout;
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_vital_edit);
		
		temp0 = (NumberPicker) findViewById(R.id.healthVital_pickerTemp0);
		temp1 = (NumberPicker) findViewById(R.id.healthVital_pickerTemp1);
		pulse = (NumberPicker) findViewById(R.id.healthVital_pickerPulse);
		resp = (NumberPicker) findViewById(R.id.healthVital_pickerRespiration);
		bp0 = (NumberPicker) findViewById(R.id.healthVital_pickerBP0);
		bp1 = (NumberPicker) findViewById(R.id.healthVital_pickerBP1);
		tempCheck = (CheckBox) findViewById(R.id.healthVital_checkboxTemp);
		pulseCheck = (CheckBox) findViewById(R.id.healthVital_checkboxPulse);
		respCheck = (CheckBox) findViewById(R.id.healthVital_checkboxRespiration);
		bpCheck = (CheckBox) findViewById(R.id.healthVital_checkboxBP);
		tempLayout = (LinearLayout) findViewById(R.id.healthVital_layoutTemp);
		bpLayout = (LinearLayout) findViewById(R.id.healthVital_layoutBP);
		
		temp0.setMinValue(60);
		temp0.setMaxValue(130);
		temp0.setWrapSelectorWheel(false);
		temp1.setMinValue(0);
		temp1.setMaxValue(9);
		temp1.setWrapSelectorWheel(false);
		
		pulse.setMinValue(0);
		pulse.setMaxValue(300);
		pulse.setWrapSelectorWheel(false);
		
		resp.setMinValue(0);
		resp.setMaxValue(40);
		resp.setWrapSelectorWheel(false);
		
		bp0.setMinValue(20);
		bp0.setMaxValue(200);
		bp0.setWrapSelectorWheel(false);
		bp1.setMinValue(20);
		bp1.setMaxValue(120);
		bp1.setWrapSelectorWheel(false);
		
		Intent i = getIntent();
		String[] tempStrArr = new String[2];
		String tempStr;
		if(!i.getStringExtra("temp").equals("")) {
			
			tempStr = i.getStringExtra("temp");
			tempStrArr = tempStr.split(Pattern.quote("."));
			temp0.setValue(Integer.valueOf(tempStrArr[0]));
			temp1.setValue(Integer.valueOf(tempStrArr[1]));
		}
		else {
			
			//default
			temp0.setValue(98);
			temp1.setValue(6);
		}
		
		if(!i.getStringExtra("pulse").equals("")) {
			
			tempStr = i.getStringExtra("pulse");
			pulse.setValue(Integer.valueOf(tempStr));
		}
		else {
			
			//default
			pulse.setValue(80);
		}
			
		if(!i.getStringExtra("resp").equals("")) {
		
			tempStr = i.getStringExtra("resp");
			resp.setValue(Integer.valueOf(tempStr));
		}
		else {
			
			//default
			resp.setValue(15);
		}
			
		if(!i.getStringExtra("bp").equals("")) {
	
			tempStr = i.getStringExtra("bp");
			tempStrArr = tempStr.split("/");
			bp0.setValue(Integer.valueOf(tempStrArr[0]));
			bp1.setValue(Integer.valueOf(tempStrArr[1]));
		}
		else {
			
			//default
			bp0.setValue(130);
			bp1.setValue(85);
		}
		
		iReturn = new Intent();
	}
	
	public void saveClicked(View view) {
		
		String data;
		
		if(tempCheck.isChecked()) {
			
			data = temp0.getValue() + "." + temp1.getValue();
			iReturn.putExtra("temperature", data);
		}
		else {
			
			iReturn.putExtra("temperature", "");
		}
		
		if(pulseCheck.isChecked()) {
			
			data = pulse.getValue() + "";
			iReturn.putExtra("pulse", data);
		}
		else {
			
			iReturn.putExtra("pulse", "");
		}
		
		if(bpCheck.isChecked()) {
			
			data = bp0.getValue() + "/" + bp1.getValue();
			iReturn.putExtra("bp", data);
		}
		else {
			
			iReturn.putExtra("bp", "");
		}
		
		if(respCheck.isChecked()) {
			
			data = resp.getValue() + "";
			iReturn.putExtra("resp", data);
		}
		else {
			
			iReturn.putExtra("resp", "");
		}
		
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
	
	public void temperatureCheckboxClicked(View view) {
		
		if(tempCheck.isChecked()) {
			
			tempLayout.setVisibility(view.VISIBLE);
		}
		else {
			
			tempLayout.setVisibility(view.GONE);
		}
	}
	
	public void pulseCheckboxClicked(View view) {
		
		if(pulseCheck.isChecked()) {
			
			pulse.setVisibility(view.VISIBLE);
		}
		else {
			
			pulse.setVisibility(view.GONE);
		}
	}
	
	public void respirationCheckboxClicked(View view) {
		
		if(respCheck.isChecked()) {
			
			this.resp.setVisibility(view.VISIBLE);
		}
		else {
			
			this.resp.setVisibility(view.GONE);
		}
	}
	
	public void bpCheckboxClicked(View view) {
		
		if(bpCheck.isChecked()) {
			
			this.bpLayout.setVisibility(view.VISIBLE);
		}
		else {
			
			bpLayout.setVisibility(view.GONE);
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_vital_edit, menu);
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
