package com.example.clip.finance;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;

public class financeStockDetail extends Activity {

	String name;
	String[] data;
	TextView stockPrice, stockAmount, stockName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_goal_detail);
		
		stockName = (TextView) findViewById(R.id.stockName);
		stockAmount = (TextView) findViewById(R.id.stockAmount);
		stockPrice = (TextView) findViewById(R.id.stockPrice);		
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		stockName.setText(name);
		stockPrice.setText("Price:\n" + data[0]);
		stockAmount.setText("Amount:\n" + data[1]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_stock_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			return true;
		}
		else if(id == R.id.action_remove) {
			
			return true;
		}
			
		return super.onOptionsItemSelected(item);
	}
}
