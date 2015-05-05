package com.example.clip.finance;

import com.example.clip.Entry;
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
import android.content.Intent;

public class financeStockEdit extends Activity {

	EditText stockName, stockAmount, stockPrice;
	Button save;
	String[] data;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_stock_edit);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		i = new Intent();
		
		stockName = (EditText) findViewById(R.id.stockName);
		stockAmount = (EditText) findViewById(R.id.stockAmount);
		stockPrice = (EditText) findViewById(R.id.stockPrice);
		save = (Button) findViewById(R.id.buttonSave);
		
		data = new String[2];						//{stockAmount, stockPrice}
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			//Display current stockName
			stockName.setText(getIntent().getStringExtra("name"));
			i.putExtra("oldName", stockName.getText().toString());
			
			//display current stockAmount
			data = getIntent().getStringArrayExtra("data");
			stockAmount.setText(data[1]);
			
			//display current stockPrice
			data = getIntent().getStringArrayExtra("data");
			stockPrice.setText(data[0]);
		}
	}
	
	public void saveClicked(View view) {
		
		data[0] = stockPrice.getText().toString();
		data[1] = stockAmount.getText().toString();
		i.putExtra("data", this.data);
		i.putExtra("name", this.stockName.getText().toString());
		this.setResult(RESULT_OK, i);
		
		this.finish();
	}
	

}
