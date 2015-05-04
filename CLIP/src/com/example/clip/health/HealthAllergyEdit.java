package com.example.clip.health;

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
import android.widget.EditText;

public class HealthAllergyEdit extends Activity {

	EditText editName;
	String name;
	Intent iReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_allergy_edit);
		
		editName = (EditText) this.findViewById(R.id.healthAllergy_name);
		
		//edit option
		if(getIntent().getStringExtra("allergyName") != null) {
			
			name = getIntent().getStringExtra("allergyName");
			iReturn = new Intent();
			iReturn.putExtra("oldName", name);
			editName.setText(name);
		}
		//add option
		else {
			
			iReturn = new Intent();
		}
	}

	public void saveClicked(View view) {
		
		iReturn.putExtra("allergyName", editName.getText().toString());
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.health_allergy_edit, menu);
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
