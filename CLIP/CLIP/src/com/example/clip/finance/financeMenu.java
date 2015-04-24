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
import com.parse.ParseObject;
import com.parse.ParseUser;

public class financeMenu extends Activity {

		Button fgoal, stock, asset, cc, liability, summary;

		Boolean newUser = false;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_finance_menu);
			getActionBar().setDisplayHomeAsUpEnabled(false);
			Intent intent = getIntent();
			newUser = intent.getBooleanExtra("newUser", false);
			
			//button assignments
			fgoal = (Button) findViewById(R.id.button_fgoal);
			stock = (Button) findViewById(R.id.button_stock);
			asset = (Button) findViewById(R.id.button_asset);
			cc = (Button) findViewById(R.id.button_cc);
			liability = (Button) findViewById(R.id.button_liability);
			summary = (Button) findViewById(R.id.button_summary);
			
			//listeners
			fgoal.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					if(newUser)
					{
						ParseObject financeGoal = new ParseObject("financeGoal");
						financeGoal.put("Owner", ParseUser.getCurrentUser());
						financeGoal.put("goalName", "None");
						financeGoal.put("goalType", "None");
						financeGoal.put("goalDate", "None");
						financeGoal.saveInBackground();

						Intent i = new Intent(financeMenu.this, financeGoal.class);
						startActivity(i);

					}
					else
					{
						Intent i = new Intent(financeMenu.this, financeGoal.class);
						startActivity(i);

					}
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