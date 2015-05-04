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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class HealthCheckup extends Activity {

	TextView goodChol, badChol, totalChol, bp, glucose, other, date;
	int[] lastUpdate;	//hour, minute, month, day, year
	String bp0, bp1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_checkup);
		
		goodChol = (TextView) this.findViewById(R.id.healthCheckup_detailCholHDL);
		badChol = (TextView) this.findViewById(R.id.healthCheckup_detailCholLDL);
		totalChol = (TextView) this.findViewById(R.id.healthCheckup_detailCholTotal);
		bp = (TextView) this.findViewById(R.id.healthCheckup_detailBloodPressure);
		glucose = (TextView) this.findViewById(R.id.healthCheckup_detailGlucose);
		other = (TextView) this.findViewById(R.id.healthCheckup_detailOther);
		date = (TextView) this.findViewById(R.id.healthCheckup_detailLastUpdate);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthCheckup");
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
				
				goodChol.setText("N/A");
				this.badChol.setText("N/A");
				this.bp.setText("N/A");
				this.totalChol.setText("N/A");
				this.glucose.setText("N/A");
				this.other.setText("N/A");
				this.date.setText("N/A");
			}
			else {
				
				for (ParseObject checkup : postList) {				
						
					goodChol.setText(checkup.getString("goodChol"));
					badChol.setText(checkup.getString("badChol"));
					totalChol.setText(checkup.getString("totalChol"));
					glucose.setText(checkup.getString("glucose"));
					
					other.setText(checkup.getString("other"));
					if(other.getText().toString().equals("")) {
						
						TextView label = (TextView) this.findViewById(R.id.healthCheckup_labelOther);
						label.setVisibility(View.INVISIBLE);
						other.setVisibility(View.INVISIBLE);
					}
					else {
						
						TextView label = (TextView) this.findViewById(R.id.healthCheckup_labelOther);
						label.setVisibility(View.VISIBLE);
						other.setVisibility(View.VISIBLE);
					}
					
					bp0 = checkup.getString("bp0");
					bp1 = checkup.getString("bp1");					
					bp.setText(bp0 + "/" + bp1);
					
					ArrayList<Integer> lastUpdateList = (ArrayList<Integer>) checkup.get("lastUpdate");
					this.lastUpdate = new int[lastUpdateList.size()];
					for(int i = 0; i < lastUpdate.length; i++) {
						
						lastUpdate[i] = lastUpdateList.get(i).intValue();
					}
					
					date.setText("Last updated\n" + lastUpdate[0] + ":" + lastUpdate[1] + "--" +
								lastUpdate[2] + "/" + lastUpdate[3] + "/" + lastUpdate[4]);
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
			goodChol.setText(data.getStringExtra("goodChol"));
			badChol.setText(data.getStringExtra("badChol"));
			totalChol.setText(data.getStringExtra("totalChol"));
			glucose.setText(data.getStringExtra("glucose"));
			other.setText(data.getStringExtra("other"));
			
			bp0 = data.getStringExtra("bp0");
			bp1 = data.getStringExtra("bp1");					
			bp.setText(bp0 + "/" + bp1);
			
			this.lastUpdate = data.getIntArrayExtra("lastUpdate");
			date.setText("Last updated\n" + lastUpdate[0] + ":" + lastUpdate[1] +
					" " + lastUpdate[2] + "/" + lastUpdate[3] + "/" + lastUpdate[4]);
			
			this.saveToCloud();
		}
		catch(NullPointerException e) {
			
			Toast.makeText(this.getApplicationContext(), "You didn't save!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_checkup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			
			Intent i = new Intent(HealthCheckup.this, HealthCheckupEdit.class);
			
			if(!goodChol.getText().toString().equals("N/A")) {
				
				i.putExtra("goodChol", goodChol.getText().toString());
				i.putExtra("badChol", badChol.getText().toString());
				i.putExtra("totalChol", totalChol.getText().toString());
				i.putExtra("glucose", glucose.getText().toString());
				i.putExtra("lastUpdate", lastUpdate);
				i.putExtra("bp0", bp0);
				i.putExtra("bp1", bp1);
				i.putExtra("other", other.getText().toString());
			}
			
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthCheckup");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject checkup : postList) {
				
				checkup.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
				 
		ParseObject healthCheckup = new ParseObject("healthCheckup");
		healthCheckup.put("Owner", ParseUser.getCurrentUser());
		
		healthCheckup.put("goodChol", this.goodChol.getText().toString());
		healthCheckup.put("badChol", this.badChol.getText().toString());
		healthCheckup.put("totalChol", totalChol.getText().toString());
		healthCheckup.put("bp", this.bp.getText().toString());
		healthCheckup.put("other", this.other.getText().toString());
		healthCheckup.put("glucose", this.glucose.getText().toString());
		
		healthCheckup.put("bp0", bp0);
		healthCheckup.put("bp1", bp1);
		
		ArrayList<Integer> date = new ArrayList<Integer>();
		for(int temp : this.lastUpdate) {
			
			date.add(temp);
		}
		healthCheckup.put("lastUpdate", date);
		
		try {
			
			healthCheckup.save();
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
	}
}
