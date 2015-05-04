package com.example.clip.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import com.example.clip.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class financeGoal extends ListActivity implements OnItemClickListener, OnItemLongClickListener{

	HashMap<String, String[]> dataMap;	//<goalName, goalData>
	String[] goalData;					//{goalLength, goalDate}
	String goalName;
	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> goalList, popUpItems;
	ListPopupWindow popUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getListView().setBackgroundColor(Color.GRAY);
		//initiate list view			
		goalList = new ArrayList<String>();
		goalList.add(getString(R.string.none));		
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_finance_goal,
				R.id.label, goalList);		
		setListAdapter(listViewAdapter);
		
		//initiate data variables
		dataMap = new HashMap<String, String[]>();
		goalData = new String[2];
		
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
		goalList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeGoal");
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
			for (ParseObject goal : postList) {			
					
				goalName = goal.getString("goalName");	
				goalList.remove(getString(R.string.none));		//remove "none" if present
				
				//add data from database 
				goalList.add(goalName);
				goalData = new String[2];
				goalData[0] = goal.getString("goalType");
				goalData[1] = goal.getString("goalDate");	
				dataMap.put(goalName, goalData);		
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(goalList.isEmpty()) {
			
			goalList.add(getString(R.string.none));
		}
		
		//update screen
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_goal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(financeGoal.this, financeGoalEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
		
			//action_add
			if(requestCode == 0) {
				
				//clears any initial data
				goalList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				
				//goalLength, goalDate
				goalData = data.getStringArrayExtra("data");
				
				//goalName -- add to list
				goalList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), goalData);
				
				//update screen
				this.onContentChanged();
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			else if(requestCode == 1) {	//edit goal
				
				//remove old data
				dataMap.remove(data.getStringExtra("oldName"));
				goalList.remove(data.getStringExtra("oldName"));
				
				//goalLength, goalDate
				goalData = data.getStringArrayExtra("data");	
				
				//goalName -- add to list
				goalList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), goalData);
				
				//update screen
				this.onContentChanged();
				
				//reset listeners
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			
			this.saveToCloud();
		}
		catch(NullPointerException n) {
			//back button press on edit screen
		}
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when goalName != none
			if(!goalName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(financeGoal.this, financeGoalEdit.class);
					i.putExtra("name", goalName);
					i.putExtra("data", dataMap.get(goalName));
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					goalList.remove(goalName);
					dataMap.remove(goalName);
					
					if(goalList.isEmpty()) {
					
						goalList.add(getString(R.string.none));
						goalData = new String[] {"Goal Type N/A", "Completion Date N/A"};
						dataMap.put(goalList.get(0), goalData);
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		else if(!goalList.get(position).equals(getString(R.string.none)))  {		
			
			Intent i = new Intent(financeGoal.this, financeGoalDetail.class);		
			this.goalData = dataMap.get(goalList.get(position));
			i.putExtra("name", goalList.get(position));
			i.putExtra("data", this.goalData);
			startActivity(i);	
		}		
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.goalName = goalList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeGoal");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject goal : postList) {
				
				goal.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(Map.Entry<String, String[]> entry : dataMap.entrySet()){
			
			if(entry.getKey().equals(getString(R.string.none)))
				continue;
			 
			ParseObject financeGoal = new ParseObject("financeGoal");
			financeGoal.put("Owner", ParseUser.getCurrentUser());
			financeGoal.put("goalName", entry.getKey());
			financeGoal.put("goalType", entry.getValue()[0]);
			financeGoal.put("goalDate", entry.getValue()[1]);
			
			try {
				
				financeGoal.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}

