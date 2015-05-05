package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class CareerEidDetail extends Activity {

	TextView name, url, user, pass;
	TextView urlLabel, userLabel;
	CheckBox checkbox;
	
	String[] dataString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_eid_detail);
		
		name = (TextView) this.findViewById(R.id.careerEid_detailName);
		url = (TextView) this.findViewById(R.id.careerEid_detailURL);
		user = (TextView) this.findViewById(R.id.careerEid_detailUsername);
		pass = (TextView) this.findViewById(R.id.careerEid_detailPassword);
		urlLabel = (TextView) this.findViewById(R.id.careerEid_labelURL);
		userLabel = (TextView) this.findViewById(R.id.careerEid_labelUsername);
		checkbox = (CheckBox) this.findViewById(R.id.careerEid_checkboxPassword);
		
		String name = getIntent().getStringExtra("eidName");
		dataString = getIntent().getStringArrayExtra("dataString");
		
		this.name.setText(name);
		url.setText(dataString[0]);
		user.setText(dataString[1]);
		pass.setText(dataString[2]);
		
		if(dataString[0].equals("")) {
			
			urlLabel.setVisibility(View.GONE);
			url.setVisibility(View.GONE);
		}
		else {
			
			urlLabel.setVisibility(View.VISIBLE);
			url.setVisibility(View.VISIBLE);
		}
		
		if(dataString[1].equals("")) {
			
			userLabel.setVisibility(View.GONE);
			user.setVisibility(View.GONE);
		}
		else {
			
			userLabel.setVisibility(View.VISIBLE);
			user.setVisibility(View.VISIBLE);
		}
		
		if(dataString[2].equals("")) {
			
			pass.setVisibility(View.GONE);
			checkbox.setVisibility(View.GONE);
		}
		else {
			
			pass.setVisibility(View.VISIBLE);
			checkbox.setVisibility(View.VISIBLE);
		}
	}
	
	public void pwCheckboxClicked(View view) {
		
		
		if(checkbox.isChecked()) {
			
			//pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			pass.setTransformationMethod(null);
		}
		else {
			
			//pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
		this.onContentChanged();
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
}
