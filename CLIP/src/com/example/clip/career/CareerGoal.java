package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class CareerGoal extends ListActivity implements OnItemClickListener{

	HashMap<String, String[]> dataMap;	//<goalName, goalData>
	String[] goalData;					//{goalLength, goalDate}
	ArrayAdapter<String> adapter;
	ArrayList<String> goalList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//build goalData	
		goalList = new ArrayList<String>();
		goalList.add("None");
		dataMap = new HashMap<String, String[]>();
		
		adapter = new ArrayAdapter<String>(this, R.layout.activity_career_goal,
				R.id.label, goalList);		
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_goal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(CareerGoal.this, CareerGoalEdit.class);
			startActivityForResult(i, 0, null);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//action_add
		if(requestCode == 0) {
			
			if(goalList.get(0).equals("None")) {
			
				goalList.remove(0);
			}
			
			//goalLength, goalDate
			goalData = data.getStringArrayExtra("data");		
			//goalName -- add to list
			goalList.add(data.getStringExtra("name"));
			//save data to dataMap
			dataMap.put(data.getStringExtra("name"), goalData);
			//update screen
			this.onContentChanged();
			getListView().setOnItemClickListener(this);
		}
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		// your code is here on item click
		Intent i = new Intent(CareerGoal.this, CareerGoalDetail.class);		
		this.goalData = dataMap.get(goalList.get(position));
		i.putExtra("name", goalList.get(position));
		i.putExtra("data", this.goalData);
		startActivity(i);		
    }
}
