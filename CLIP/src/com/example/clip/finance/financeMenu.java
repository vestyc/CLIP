package com.example.clip.finance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.clip.R;

public class financeMenu extends Activity {
	
	Button goal, stock, asset, creditcard, liability;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_menu);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		//button assignments
		goal = (Button) findViewById(R.id.button_fgoal);
		stock = (Button) findViewById(R.id.button_stock);
		asset = (Button) findViewById(R.id.button_asset);
		creditcard = (Button) findViewById(R.id.button_creditcard);
		liability = (Button) findViewById(R.id.button_liability);
		
		//listeners
		goal.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(financeMenu.this, financeGoal.class);
				startActivity(i);
	         }
		});
		
		stock.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(financeMenu.this, financeStock.class);
				startActivity(i);
			}
		});
		
		asset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(financeMenu.this, financeAsset.class);
				startActivity(i);
			}
		});
		
		creditcard.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(financeMenu.this, financeCreditCard.class);
				startActivity(i);
			}
		});
		
		liability.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(financeMenu.this, financeLiability.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_menu, menu);
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
