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
		setContentView(R.layout.activity_finance_stock_detail);
		
		stockName = (TextView) findViewById(R.id.stockName);
		stockAmount = (TextView) findViewById(R.id.stockAmount);
		stockPrice = (TextView) findViewById(R.id.stockPrice);		
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		stockName.setText(name);
		stockPrice.setText("Price:\n" + data[0]);
		stockAmount.setText("Amount:\n" + data[1]);
	}
}
