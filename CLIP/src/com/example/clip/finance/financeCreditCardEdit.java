package com.example.clip.finance;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.content.Intent;


public class financeCreditCardEdit extends Activity{

	EditText cardName, minPayment, amountOwed;
	Button saveButton;
	DatePicker datePicker;

	String name;	//cardName
	String[] data;	//{amountOwed, minPayment}
	int[] dataDate;	//{month, day, year}
	
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_creditcard_edit);
		
		iReturn = new Intent();
		
		cardName = (EditText) findViewById(R.id.cardName);
		amountOwed = (EditText) findViewById(R.id.amountOwed);
		minPayment = (EditText) findViewById(R.id.minPayment);
		saveButton = (Button) findViewById(R.id.card_buttonSave);		
		datePicker = (DatePicker) findViewById(R.id.card_datePicker);
		data = new String[2];
		dataDate = new int[3];	//{month, day, year}
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			//Display current cardName
			cardName.setText(getIntent().getStringExtra("name"));
			iReturn.putExtra("oldName", cardName.getText().toString());
			
			//display current amountOwed
			data = getIntent().getStringArrayExtra("data");
			amountOwed.setText(data[0]);
			
			//display current minPayment
			data = getIntent().getStringArrayExtra("data");
			minPayment.setText(data[1]);
			
			dataDate = getIntent().getIntArrayExtra("date");	//{Month, Day, Year}
			if(dataDate != null) {
				
				datePicker.updateDate(dataDate[2], dataDate[0] - 1, dataDate[1]);
			}
		}
		}
		
	
	public void saveClicked(View view) {
			
		iReturn.putExtra("name", cardName.getText().toString());
		data[0] = amountOwed.getText().toString();
		data[1] = minPayment.getText().toString();
		iReturn.putExtra("data", data);
		
		//{month, day, year}
		dataDate[0] = 1 + datePicker.getMonth();
		dataDate[1] = datePicker.getDayOfMonth();
		dataDate[2] = datePicker.getYear();
		iReturn.putExtra("date", dataDate);
				
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}	
}
