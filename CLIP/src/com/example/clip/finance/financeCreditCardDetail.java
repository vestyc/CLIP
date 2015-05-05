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
}
