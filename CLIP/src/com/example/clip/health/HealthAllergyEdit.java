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
}
