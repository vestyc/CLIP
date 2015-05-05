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

public class financeLiabilityEdit extends Activity {

	EditText liabilityName, liabilityAmount;
	Button save;
	String[] data;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finance_liability_edit);
		i = new Intent();
		
		liabilityName = (EditText) findViewById(R.id.liabilityName);
		liabilityAmount = (EditText) findViewById(R.id.liabilityAmount);
		save = (Button) findViewById(R.id.buttonSave);
		
		data = new String[1];						//{liabilityAmount}
		
		//edit option
		if(getIntent().getStringExtra("name") != null) {
			
			//Display current liabilityName
			liabilityName.setText(getIntent().getStringExtra("name"));
			i.putExtra("oldName", liabilityName.getText().toString());
			
			//display current liabilityAmount
			data = getIntent().getStringArrayExtra("data");
			liabilityAmount.setText(data[0]);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_liability_edit, menu);
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
	
	public void saveClicked(View view) {
		
		data[0] = liabilityAmount.getText().toString();
		i.putExtra("data", this.data);
		i.putExtra("name", this.liabilityName.getText().toString());
		this.setResult(RESULT_OK, i);
		
		this.finish();
	}
	

}
