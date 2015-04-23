package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class CareerJobApp extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> jobList, popUpItems;
	ListPopupWindow popUp;
	String jobName;
	
	HashMap<String, String[]> dataMap;	//<jobName, jobData>
	String[] jobData;					//{dateApplied, appStatus, comments}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//initiate list
		jobList = new ArrayList<String>();
		jobList.add(getString(R.string.none));	
		jobData = new String[] {getString(R.string.none),getString(R.string.none),
				getString(R.string.none)};
		dataMap = new HashMap<String, String[]>();
		dataMap.put(jobList.get(0), jobData);
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_career_job_app,
				R.id.label_jobList, jobList);
		this.setListAdapter(listViewAdapter);
		
		//initiate the pop-up list
		popUp = new ListPopupWindow(this);
		popUpItems = new ArrayList<String>();
		popUpItems.add(getString(R.string.action_edit));
		popUpItems.add(getString(R.string.action_remove));
		popUpAdapter = new ArrayAdapter<String>(this, R.layout.edit_remove_popup,
				R.id.label_popUp, popUpItems);
		popUp.setAdapter(popUpAdapter);
		popUp.setModal(true);
		popUp.setWidth(200);
		popUp.setHeight(ListPopupWindow.WRAP_CONTENT);
		
		//listeners
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//action_add
		if(requestCode == 0) {
			
			
		}
		else if(requestCode == 1) {	//edit goal
			
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_job_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(CareerJobApp.this, CareerJobAppEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//edit is clicked
			if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
											
				Intent i = new Intent(CareerJobApp.this, CareerJobAppEdit.class);
				this.startActivityForResult(i, 1);
			}
			//remove is clicked
			else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
				
				jobList.remove(jobName);
				//dataMap.remove(goalName);
				
				if(jobList.isEmpty()) {
				
					jobList.add(getString(R.string.none));
					//goalData = new String[] {"Goal Type N/A", "Completion Date N/A"};
					//dataMap.put(goalList.get(0), goalData);
				}
				this.onContentChanged();
			}
		}
		else {
			
			Intent i = new Intent(CareerJobApp.this, CareerJobAppDetail.class);		
			this.jobData = dataMap.get(jobList.get(position));
			i.putExtra("name", jobList.get(position));
			i.putExtra("data", this.jobData);
			startActivity(i);
		}
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.jobName = jobList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
}
