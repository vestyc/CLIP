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

public class financeAssetEdit extends Activity {

	EditText assetName, assetAmount;
	Button save;
	String[] data;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_asset_edit);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		i = new Intent();
		
		assetName = (EditText) findViewById(R.id.assetName);
		assetAmount = (EditText) findViewById(R.id.assetAmount);
		save = (Button) findViewById(R.id.buttonSave);
		
		data = new String[1];						//{assetAmount}
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			//Display current assetName
			assetName.setText(getIntent().getStringExtra("name"));
			i.putExtra("oldName", assetName.getText().toString());
			
			//display current assetAmount
			data = getIntent().getStringArrayExtra("data");
			assetAmount.setText(data[0]);
			
		}
	}
	
	public void saveClicked(View view) {
		
		data[0] = assetAmount.getText().toString();
		i.putExtra("data", this.data);
		i.putExtra("name", this.assetName.getText().toString());
		this.setResult(RESULT_OK, i);
		
		this.finish();
	}
	

}
