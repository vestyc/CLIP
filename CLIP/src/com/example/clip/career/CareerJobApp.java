package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CareerJobApp extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> jobList, popUpItems;
	ListPopupWindow popUp;
	String jobName;
	
	HashMap<String, String[]> dataMap;	//<jobName, jobData>
	HashMap<String, int[]> dateAppMap;	//<jobName, dateApplied>
	String[] jobData;					//{appStatus, comments}
	int[]	jobDateApplied;				//{Month, Day, Year}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		//initiate list view
		this.createEmptyList();
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
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//remove local data (overriding with cloud data)
		dataMap.clear();
		this.dateAppMap.clear();
		jobList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("careerJob");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		// Create query for objects of type "Post"

		// Restrict to cases where the author is the current user.
		// Note that you should pass in a ParseUser and not the
		// String reperesentation of that user
		// Run the query
		
		try {
		
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject job : postList) {				
					
				jobName = job.getString("jobName");	
				
				//add data from database 
				jobList.add(jobName);
				
				jobData = new String[2];					//{appStatus, comments}
				jobData[0] = job.getString("applicationStatus");	
				jobData[1] = job.getString("comments");	
				dataMap.put(jobName, jobData);	
				
				jobDateApplied = new int[3];				//{Month, Day, Year}
				jobDateApplied[0] = job.getInt("month");
				jobDateApplied[1] = job.getInt("day");
				jobDateApplied[2] = job.getInt("year");
				this.dateAppMap.put(jobName, jobDateApplied);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(jobList.isEmpty()) {
			
			this.resetEmptyList();
		}
		
		//update screen
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
			
			//edit job
			if(requestCode == 1) {	
				
				//remove old data first
				this.jobName = data.getStringExtra("oldName");
				this.jobList.remove(jobName);
				this.dataMap.remove(jobName);
				this.dateAppMap.remove(jobName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				jobList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				dateAppMap.remove(getString(R.string.none));
			}
			
			//add new data
			this.jobName = data.getStringExtra("name");
			this.jobData = data.getStringArrayExtra("data");
			this.jobDateApplied = data.getIntArrayExtra("date");
			
			//adding data to data maps
			this.jobList.add(this.jobName);
			this.dataMap.put(this.jobName, this.jobData);
			this.dateAppMap.put(this.jobName, this.jobDateApplied);
			
			//updating screen, resetting listeners
			this.onContentChanged();
			getListView().setOnItemClickListener(this);
			popUp.setOnItemClickListener(this);
			this.saveToCloud();
		}
		catch(NullPointerException n) {
			//back button press on edit screen
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
			
			//only operate when jobName != none
			if(!jobName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(CareerJobApp.this, CareerJobAppEdit.class);
					
					this.jobData = dataMap.get(jobList.get(position));
					this.jobDateApplied = this.dateAppMap.get(jobList.get(position));
					
					i.putExtra("name", jobList.get(position));
					i.putExtra("data", this.jobData);
					i.putExtra("date", this.jobDateApplied);
					
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					jobList.remove(jobName);
					dataMap.remove(jobName);
					this.dateAppMap.remove(jobName);
					
					if(jobList.isEmpty()) {
					
						this.resetEmptyList();
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		//show details if job != none
		else if(!jobList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(CareerJobApp.this, CareerJobAppDetail.class);		
			this.jobData = dataMap.get(jobList.get(position));
			this.jobDateApplied = this.dateAppMap.get(jobList.get(position));
			
			i.putExtra("name", jobList.get(position));
			i.putExtra("data", this.jobData);
			i.putExtra("date", this.jobDateApplied);
			
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
	
	private void createEmptyList() {
		
		jobList = new ArrayList<String>();
		jobList.add(getString(R.string.none));
		jobData = new String[] {"Status N/A", "No comments."};
		dataMap = new HashMap<String, String[]>();
		dataMap.put(jobList.get(0), jobData);
		jobDateApplied = null;
		dateAppMap = new HashMap<String, int[]>();
		dateAppMap.put(jobList.get(0), jobDateApplied);
	}
	
	private void resetEmptyList() {
		
		jobList.add(getString(R.string.none));
		jobData = new String[] {"Status N/A", "No comments."};
		dataMap = new HashMap<String, String[]>();
		dataMap.put(jobList.get(0), jobData);
		jobDateApplied = null;
		dateAppMap = new HashMap<String, int[]>();
		dateAppMap.put(jobList.get(0), jobDateApplied);
	}
	
private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("careerJob");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject job : postList) {
				
				job.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(String jobName : jobList){
			
			 if(jobName.equals(getString(R.string.none)))
				 continue;
			 
			ParseObject careerJob = new ParseObject("careerJob");
			careerJob.put("Owner", ParseUser.getCurrentUser());
			careerJob.put("jobName", jobName);
			
			jobData = dataMap.get(jobName);
			careerJob.put("applicationStatus", jobData[0]);
			careerJob.put("comments", jobData[1]);
			
			this.jobDateApplied = this.dateAppMap.get(jobName);
			careerJob.put("month", jobDateApplied[0]);
			careerJob.put("day", jobDateApplied[1]);
			careerJob.put("year", jobDateApplied[2]);
			
			try {
				
				careerJob.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}
