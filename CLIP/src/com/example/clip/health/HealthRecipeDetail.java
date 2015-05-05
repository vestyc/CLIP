package com.example.clip.health;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HealthRecipeDetail extends Activity {

	TextView name, url, notes;
	TextView urlLabel, notesLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_recipe_detail);
		
		name = (TextView) this.findViewById(R.id.healthRecipe_detailName);
		url = (TextView) this.findViewById(R.id.healthRecipe_detailURL);
		notes = (TextView) this.findViewById(R.id.healthRecipe_detailNotes);
		urlLabel = (TextView) this.findViewById(R.id.healthRecipe_labelURL);
		notesLabel = (TextView) this.findViewById(R.id.healthRecipe_labelNotes);
		
		String name = getIntent().getStringExtra("recipeName");
		String[] dataString = getIntent().getStringArrayExtra("dataString");
		
		this.name.setText(name);
		url.setText(dataString[0]);
		notes.setText(dataString[1]);
		
		if(dataString[0].equals("")) {
			
			urlLabel.setVisibility(View.GONE);
			url.setVisibility(View.GONE);
		}
		else {
			
			urlLabel.setVisibility(View.VISIBLE);
			url.setVisibility(View.VISIBLE);
		}
		
		if(dataString[1].equals("")) {
			
			notesLabel.setVisibility(View.GONE);
			notes.setVisibility(View.GONE);
		}
		else {
			
			notesLabel.setVisibility(View.VISIBLE);
			notes.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
}
