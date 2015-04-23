package com.example.clip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.clip.career.CareerMenu;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Entry extends Activity {

	Button career;
	EditText txtsave;
	TextView txtreturn;
	Button btnsave;
	Boolean newUser;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		
		newUser = intent.getExtras().getBoolean("newUser");

		career = (Button) findViewById(R.id.button_career);


		career.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (newUser)
				{
					ParseObject careerGoal = new ParseObject("careerGoal");
					careerGoal.put("Owner", ParseUser.getCurrentUser());
					careerGoal.put("goalNameDefault", "None");
					careerGoal.put("goalType", "None");
					careerGoal.put("goalDate", "None");
					careerGoal.saveInBackground();
					Intent i = new Intent(Entry.this, CareerMenu.class);
					startActivity(i);
				}
				
				
	         }
		});
	}
	
	public void logOut()
	{
		
		Intent i = new Intent(Entry.this, MainActivity.class);
		ParseUser.getCurrentUser().logOut();
		startActivity(i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		 switch (item.getItemId()) {
	        case R.id.action_logout:
	            logOut();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
