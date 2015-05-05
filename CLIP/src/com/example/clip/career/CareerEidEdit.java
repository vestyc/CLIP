package com.example.clip.career;

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

public class CareerEidEdit extends Activity {

	EditText name, url, user, pass;
	Intent iReturn;
	String eidName;
	String[] dataString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_eid_edit);
		
		name = (EditText) this.findViewById(R.id.careerEid_editName);
		url = (EditText) this.findViewById(R.id.careerEid_editURL);
		user = (EditText) this.findViewById(R.id.careerEid_editUser);
		pass = (EditText) this.findViewById(R.id.careerEid_editPassword);
		
		//edit option
		if(getIntent().getStringExtra("eidName") != null) {
			
			iReturn = new Intent();
			eidName = getIntent().getStringExtra("eidName");
			dataString = getIntent().getStringArrayExtra("dataString");
			
			iReturn.putExtra("oldName", eidName);
			
			name.setText(eidName);
			url.setText(dataString[0]);
			user.setText(dataString[1]);
			pass.setText(dataString[2]);
		}
		//add option
		else {
			
			iReturn = new Intent();
			dataString = new String[3];
		}
	}

	public void saveClicked(View view) {
		
		iReturn.putExtra("eidName", name.getText().toString());
		dataString[0] = url.getText().toString();
		dataString[1] = user.getText().toString();
		dataString[2] = pass.getText().toString();
		iReturn.putExtra("dataString", dataString);
		
		this.setResult(RESULT_OK, iReturn);
		this.finish();
	}
}
