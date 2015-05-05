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

public class financeLiabilityDetail extends Activity {

	String name;
	String[] data;
	TextView liabilityAmount, liabilityName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_liability_detail);
		
		liabilityName = (TextView) findViewById(R.id.liabilityName);
		liabilityAmount = (TextView) findViewById(R.id.liabilityAmount);	
		
		name = getIntent().getStringExtra("name");
		data = getIntent().getStringArrayExtra("data");
			
		liabilityName.setText(name);
		liabilityAmount.setText("Amount:\n" + data[0]);
	}
}
