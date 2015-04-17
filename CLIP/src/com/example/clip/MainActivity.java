package com.example.clip;

//import com.example.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button currEducation;
	Button gradPlan;
	Button gradSchools;
	Button financial;
	Button others;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		currEducation = (Button) findViewById(R.id.currEd);
		currEducation.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, CurrEducation.class);
				startActivity(i);
			//	finish();
	         }
		});
		
		gradPlan = (Button) findViewById(R.id.gradPlan);
		gradPlan.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, GradPlan.class);
				startActivity(i);
			//	finish();
	         }
		});
		
		gradSchools = (Button) findViewById(R.id.gradSchool);
		gradSchools.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, GradSchools.class);
				startActivity(i);
			//	finish();
	         }
		});
		
		financial = (Button) findViewById(R.id.financial);
		financial.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Financial.class);
				startActivity(i);
			//	finish();
	         }
		});
		
		others = (Button) findViewById(R.id.others);
		others.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Others.class);
				startActivity(i);
			//	finish();
	         }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
