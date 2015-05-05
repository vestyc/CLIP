package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import android.content.res.Resources;

import java.lang.Integer;

public class CareerContactDetail extends Activity {
	
	TextView name, affiliation, established, useNumber, comments;
	TextView affiliationLabel, commentsLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_career_contact_detail);
		
		this.assignViews();
		
		Intent i = getIntent();
		String name = i.getStringExtra("name");
		String[] dataString = i.getStringArrayExtra("dataString");
		int[] dataInt = i.getIntArrayExtra("dataInt");
		
		this.name.setText(name);
		
		//<contactName, dataString>
		//{affiliation, comments}
		if(!dataString[0].equals("")) {
			this.affiliation.setText(dataString[0]);
			this.affiliation.setVisibility(View.VISIBLE);
			this.affiliationLabel.setVisibility(View.VISIBLE);
		}
		if(!dataString[1].equals("")) {
			this.comments.setText(dataString[1]);
			this.comments.setVisibility(View.VISIBLE);
			this.commentsLabel.setVisibility(View.VISIBLE);
		}
		
		//<contactName, dataInt>
		//{estMonth, estDay, estYear, #OfUse}
		this.established.setText(dataInt[0] + "/" + dataInt[1] + "/" + dataInt[2]);
		
		Integer nOfUses = Integer.valueOf(dataInt[3]);
		this.useNumber.setText(nOfUses.toString());
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		this.finish();
	}
	
	private void assignViews() {
				
		name = (TextView) findViewById(R.id.contact_detailName);
		affiliation = (TextView) findViewById(R.id.contact_detailAffiliation);
		established = (TextView) findViewById(R.id.contact_detailEstablished);
		useNumber = (TextView) findViewById(R.id.contact_detailNumberOfUse);
		comments = (TextView) findViewById(R.id.contact_detailComments);
		affiliationLabel = (TextView) findViewById(R.id.contact_labelAffiliation);
		commentsLabel = (TextView) findViewById(R.id.contact_labelComments);
	}
}
