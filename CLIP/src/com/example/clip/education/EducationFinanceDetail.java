package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;

public class EducationFinanceDetail extends Activity {

	TextView title, type, funds, upkeep, comments;
	TextView fundsLabel, upkeepLabel, commentsLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_finance_detail);
		
		title = (TextView) findViewById(R.id.educationFinance_detailTitle);
		type = (TextView) findViewById(R.id.educationFinance_detailType);
		funds = (TextView) findViewById(R.id.educationFinance_detailFunds);
		upkeep = (TextView) findViewById(R.id.educationFinance_detailUpkeep);
		comments = (TextView) findViewById(R.id.educationFinance_detailComments);
		fundsLabel = (TextView) findViewById(R.id.educationFinance_labelFunds);
		upkeepLabel = (TextView) findViewById(R.id.educationFinance_labelUpkeep);
		commentsLabel = (TextView) findViewById(R.id.educationFinance_labelComments);
		
		Intent i = getIntent();
		String financeName = i.getStringExtra("financeName");
		String[] dataString = i.getStringArrayExtra("dataString");
		
		title.setText(financeName);
		//{financeType, funds, upkeep, comments/notes}
		this.type.setText(dataString[0]);
		if(!dataString[1].equals("")) {
			this.funds.setText(dataString[1]);
			this.funds.setVisibility(View.VISIBLE);
			this.fundsLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[2].equals("")) {
			this.upkeep.setText(dataString[2]);
			this.upkeep.setVisibility(View.VISIBLE);
			this.upkeepLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[3].equals("")) {
			this.comments.setText(dataString[3]);
			this.comments.setVisibility(View.VISIBLE);
			this.commentsLabel.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_finance_detail, menu);
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
