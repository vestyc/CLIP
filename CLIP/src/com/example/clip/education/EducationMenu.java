package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.example.clip.career.CareerGoal;
import com.example.clip.career.CareerMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EducationMenu extends Activity {

	Button currentEdu, gradPlans, futureStudies, financialInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_menu);
		
		currentEdu = (Button) findViewById(R.id.education_buttonCurrentEducation);
		gradPlans = (Button) findViewById(R.id.education_buttonGraduatePlans);
		futureStudies = (Button) findViewById(R.id.education_buttonFutureStudies);
		financialInfo = (Button) findViewById(R.id.education_buttonFinancial);
		
		//listeners
		financialInfo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(EducationMenu.this, EducationFinance.class);
				startActivity(i);
	         }
		});
		
		futureStudies.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(EducationMenu.this, EducationFuture.class);
				startActivity(i);
	         }
		});
		
		gradPlans.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent i = new Intent(EducationMenu.this, EducationGraduate.class);
				startActivity(i);
	         }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_menu, menu);
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
