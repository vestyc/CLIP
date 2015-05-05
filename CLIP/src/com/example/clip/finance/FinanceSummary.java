package com.example.clip.finance;

import java.util.ArrayList;
import java.util.Calendar;
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
						
					net.setText(String.format("%.2f", summary.getDouble("net")));
					own.setText(String.format("%.2f", summary.getDouble("own")));
					debt.setText(String.format("%.2f", summary.getDouble("debt")));
					
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
			//STOCKS
			ParseQuery<ParseObject> query = ParseQuery.getQuery("financeStock");
			query.whereEqualTo("Owner", ParseUser.getCurrentUser());
			
			try {
				
				List<ParseObject> postList = query.find();				
			
				for(ParseObject stock : postList) {
					
					String price = stock.getString("stockPrice");
					String amount = stock.getString("stockAmount");
					price = price.replace("$", "");
					price = price.replace(",", "");
					this.ownDouble = ownDouble + (Double.valueOf(price) * Double.valueOf(amount));
				}
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
			
			//ASSETS
			query = ParseQuery.getQuery("financeAsset");
			query.whereEqualTo("Owner", ParseUser.getCurrentUser());
			
			try {
				
				List<ParseObject> postList = query.find();				
			
				for(ParseObject asset : postList) {
					
					String price = asset.getString("assetType");
					price = price.replace("$", "");
					price = price.replace(",", "");
					this.ownDouble = ownDouble + Double.valueOf(price);
				}
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
			
			//CREDIT CARDS
			query = ParseQuery.getQuery("financeCreditCard");
			query.whereEqualTo("Owner", ParseUser.getCurrentUser());
			
			try {
				
				List<ParseObject> postList = query.find();				
			
				for(ParseObject credit : postList) {
					
					String debt = credit.getString("amountOwed");
					debt = debt.replace("$", "");
					debt = debt.replace(",", "");
					this.debtDouble = debtDouble + Double.valueOf(debt);
				}
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
			
			//LIABILITIES
			query = ParseQuery.getQuery("financeLiability");
			query.whereEqualTo("Owner", ParseUser.getCurrentUser());
			
			try {
				
				List<ParseObject> postList = query.find();				
			
				for(ParseObject liability : postList) {
					
					String debt = liability.getString("liabilityType");
					debt = debt.replace("$", "");
					debt = debt.replace(",", "");
					this.debtDouble = debtDouble + Double.valueOf(debt);
				}
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
			
			this.netDouble = this.ownDouble - this.debtDouble;
			
			Calendar currentTime = Calendar.getInstance();
			lastUpdate = new int[5]; //hour, minute, month, day, year
			lastUpdate[0] = currentTime.get(Calendar.HOUR_OF_DAY);
			lastUpdate[1] = currentTime.get(Calendar.MINUTE);
			lastUpdate[2] = currentTime.get(Calendar.MONTH) + 1;
			lastUpdate[3] = currentTime.get(Calendar.DAY_OF_MONTH);
			lastUpdate[4] = currentTime.get(Calendar.YEAR);
			
			this.updateScreen();
			this.saveToCloud();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void updateScreen() {
		
		net.setText(String.format("%.2f", this.netDouble));
		own.setText(String.format("%.2f", this.ownDouble));
		debt.setText(String.format("%.2f", this.debtDouble));		
		date.setText("Last updated\n" + lastUpdate[0] + ":" + lastUpdate[1] + "--" +
					lastUpdate[2] + "/" + lastUpdate[3] + "/" + lastUpdate[4]);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeSummary");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject summary : postList) {
				
				summary.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
				 
		ParseObject financeSummary = new ParseObject("financeSummary");
		
		financeSummary.put("Owner", ParseUser.getCurrentUser());
		financeSummary.put("net", netDouble);
		financeSummary.put("own", ownDouble);
		financeSummary.put("debt", debtDouble);
		
		ArrayList<Integer> date = new ArrayList<Integer>();
		for(int temp : this.lastUpdate) {
			
			date.add(temp);
		}
		financeSummary.put("lastUpdate", date);
		
		try {
			
			financeSummary.save();
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
	}
}
