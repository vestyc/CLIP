package com.example.clip.health;

import java.util.ArrayList;
import java.util.List;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class HealthVital extends Activity {

	TextView temperature, pulse, respiration, bloodPressure, lastUpdate;
	int[] lastUpdateInt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_vital);
		
		temperature = (TextView) findViewById(R.id.healthVital_detailTemp);
		pulse = (TextView) findViewById(R.id.healthVital_detailPulse);
		respiration = (TextView) findViewById(R.id.healthVital_detailRespiration);
		bloodPressure = (TextView) findViewById(R.id.healthVital_detailBloodPressure);
		lastUpdate = (TextView) findViewById(R.id.healthVital_detailLastUpdate);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthVital");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		// Create query for objects of type "Post"

		// Restrict to cases where the author is the current user.
		// Note that you should pass in a ParseUser and not the
		// String reperesentation of that user
		// Run the query
		
		try {
		
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			if(postList.isEmpty()) {
				
				temperature.setText("N/A");
				this.pulse.setText("N/A");
				this.respiration.setText("N/A");
				this.bloodPressure.setText("N/A");
				this.lastUpdate.setText("N/A");
			}
			else {
				
				for (ParseObject vital : postList) {				
						
					if(!vital.getString("temperature").equals(""))
						temperature.setText(vital.getString("temperature"));
					else
						temperature.setText("N/A");
					
					if(!vital.getString("pulse").equals(""))
						pulse.setText(vital.getString("pulse"));
					else
						temperature.setText("N/A");
					
					if(!vital.getString("respiration").equals(""))
						respiration.setText(vital.getString("respiration"));
					else
						respiration.setText("N/A");
					
					if(!vital.getString("bloodPressure").equals(""))
						bloodPressure.setText(vital.getString("bloodPressure"));
					else
						bloodPressure.setText("N/A");
					
					if(!vital.getString("lastUpdateString").equals("")) 
						this.lastUpdate.setText(vital.getString("lastUpdateString"));
					else
						this.lastUpdate.setText("N/A");
				}
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {

			//return from edit
			temperature.setText(data.getStringExtra("temperature"));
			pulse.setText(data.getStringExtra("pulse"));
			respiration.setText(data.getStringExtra("resp"));
			bloodPressure.setText(data.getStringExtra("bp"));
			
			this.lastUpdateInt = data.getIntArrayExtra("lastUpdate");
			this.lastUpdate.setText(lastUpdateInt[0] + ":" + lastUpdateInt[1] +
					" " + lastUpdateInt[2] + "/" + lastUpdateInt[3] + "/" + lastUpdateInt[4]);
			
			this.saveToCloud();
		}
		catch(NullPointerException e) {
			
			Toast.makeText(this.getApplicationContext(), "You didn't save!", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_vital, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			
			Intent i = new Intent(HealthVital.this, HealthVitalEdit.class);
			
			if(this.temperature.getText().toString().equals("N/A"))
				i.putExtra("temp", "");
			else
				i.putExtra("temp", this.temperature.getText().toString());
			
			if(this.pulse.getText().toString().equals("N/A"))
				i.putExtra("pulse", "");
			else
				i.putExtra("pulse", this.pulse.getText().toString());
			
			if(this.bloodPressure.getText().toString().equals("N/A"))
				i.putExtra("bp", "");
			else
				i.putExtra("bp", this.bloodPressure.getText().toString());
			
			if(this.respiration.getText().toString().equals("N/A"))
				i.putExtra("resp", "");
			else
				i.putExtra("resp", this.respiration.getText().toString());
			
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthVital");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject vital : postList) {
				
				vital.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
				 
		ParseObject healthVital = new ParseObject("healthVital");
		healthVital.put("Owner", ParseUser.getCurrentUser());
		
		healthVital.put("temperature", this.temperature.getText().toString());
		healthVital.put("pulse", this.pulse.getText().toString());
		healthVital.put("respiration", this.respiration.getText().toString());
		healthVital.put("bloodPressure", this.bloodPressure.getText().toString());
		healthVital.put("lastUpdateString", this.lastUpdate.getText().toString());
		
		ArrayList<Integer> date = new ArrayList<Integer>();
		for(int temp : this.lastUpdateInt) {
			
			date.add(temp);
		}
		healthVital.put("lastUpdateInt", date);
		
		try {
			
			healthVital.save();
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
	}
}
