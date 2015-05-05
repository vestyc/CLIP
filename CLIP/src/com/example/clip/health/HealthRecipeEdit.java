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

public class HealthRecipeEdit extends Activity {

	EditText name, url, notes;
	Intent iReturn;
	String recipeName;
	String[] dataString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_recipe_edit);
		
		name = (EditText) this.findViewById(R.id.healthRecipe_editName);
		url = (EditText) this.findViewById(R.id.healthRecipe_editURL);
		notes = (EditText) this.findViewById(R.id.healthRecipe_editNotes);
		
		//edit option
		if(getIntent().getStringExtra("recipeName") != null) {
			
			iReturn = new Intent();
			recipeName = getIntent().getStringExtra("recipeName");
			dataString = getIntent().getStringArrayExtra("dataString");
			
			iReturn.putExtra("oldName", recipeName);
			
			name.setText(recipeName);
			url.setText(dataString[0]);
			notes.setText(dataString[1]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[2];
		}
	}
	
	public void saveClicked(View view) {
		
		iReturn.putExtra("recipeName", name.getText().toString());
		dataString[0] = url.getText().toString();
		dataString[1] = notes.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
}
