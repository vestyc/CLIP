package com.example.clip.finance;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class financeCreditCardDetail extends Activity {

	String name;
	String[] data;
	int[] date;
	TextView amountOwed, minPayment, cardName, dateDue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_creditcard_detail);
		
		amountOwed = (TextView) findViewById(R.id.amountOwed);
		minPayment = (TextView) findViewById(R.id.minPayment);
		cardName = (TextView) findViewById(R.id.cardName);
		dateDue = (TextView) findViewById(R.id.dateDue);
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
		date = getIntent().getIntArrayExtra("date");
		
		// data = {amountOwed, minPayment}
		if(name != null) {
		
			cardName.setText(name);
		}
		if(data != null) {
		
			amountOwed.setText("Total Amount Owed:\n" + data[0]);
			minPayment.setText("Minimum Payment Due:\n" + data[1]);
		}
		if(date != null) {
		
			dateDue.setText("Minimum Payment Due Date:\n" + date[0]+"/"+date[1]+"/"+date[2]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_creditcard_detail, menu);
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
