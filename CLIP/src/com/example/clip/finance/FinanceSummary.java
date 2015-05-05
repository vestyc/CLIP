package com.example.clip.finance;

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
import android.view.View;
import android.widget.*;

public class FinanceSummary extends Activity {

	TextView date, net, own, debt;
	double netDouble, ownDouble, debtDouble;
	int[] lastUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_summary);
		
		date = (TextView) this.findViewById(R.id.financeSummary_detailLastUpdate);
		net = (TextView) this.findViewById(R.id.financeSummary_detailNet);
		own = (TextView) this.findViewById(R.id.financeSummary_detailOwned);
		debt = (TextView) this.findViewById(R.id.financeSummary_detailDebt);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeSummary");
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
				
				date.setText("N/A");
				this.net.setText("N/A");
				this.own.setText("N/A");
				this.debt.setText("N/A");
			}
			else {
				
				for (ParseObject summary : postList) {				
						
					net.setText(Double.toString(summary.getDouble("net")));
					own.setText(Double.toString(summary.getDouble("own")));
					debt.setText(Double.toString(summary.getDouble("debt")));
					
					ArrayList<Integer> lastUpdateList = (ArrayList<Integer>) summary.get("lastUpdate");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_summary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_update) {
			
			//Grab data from Parse and compute values
			ParseQuery<ParseObject> query = ParseQuery.getQuery("financeSummary");
			query.whereEqualTo("Owner", ParseUser.getCurrentUser());
			
			try {
				
				List<ParseObject> postList = query.find();
			
				
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
