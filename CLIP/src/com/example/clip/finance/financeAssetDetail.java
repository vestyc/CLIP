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

public class financeAssetDetail extends Activity {

	String name;
	String[] data;
	TextView assetAmount, assetName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_asset_detail);
		
		assetName = (TextView) findViewById(R.id.assetName);
		assetAmount = (TextView) findViewById(R.id.assetAmount);	
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		assetName.setText(name);
		assetAmount.setText("Amount:\n" + data[0]);
	}
}
