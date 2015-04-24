package com.example.clip.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
	
	ParseQuery<ParseObject> query = ParseQuery.getQuery("financeGoal");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		 
		//build initial goalData	
		goalList = new ArrayList<String>();
		dataMap = new HashMap<String, String[]>();
		goalData = new String[3];
		
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
		
		ParseAnalytics.trackAppOpened(getIntent());
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeGoal");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		// Create query for objects of type "Post"

			// Restrict to cases where the author is the current user.
			// Note that you should pass in a ParseUser and not the
			// String reperesentation of that user
			// Run the query
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> postList, ParseException e) {
					if (e == null) {
						goalList.clear();
						dataMap.clear();
						// If there are results, update the list of posts
						// and notify the adapter
						for (ParseObject goal : postList) {
							goalData[0] = goal.getString("goalType");
							goalData[1] = goal.getString("goalDate");
							goalList.add(goal.getString("goalName"));
							dataMap.put(goal.getString("goalName"), goalData);
						}
						((ArrayAdapter<String>)getListAdapter()).notifyDataSetChanged();

					} else {
						Log.d("Post retrieval", "Error: " + e.getMessage());
					}

				}

			});
			
		//initiate list view
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_finance_goal,
				R.id.label, goalList);		
		setListAdapter(listViewAdapter);
		
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		


	}
	
	@Override
	protected void onPause()
	{
		super.onPause(); 
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> postList, ParseException e) {
				if (e == null) {
					// If there are results, update the list of posts
					// and notify the adapter
					for (ParseObject goal : postList) {
						goal.deleteInBackground();
					}
					
				} else {
					Log.d("Post retrieval", "Error: " + e.getMessage());
				}

			}

		});
		
		 for(Map.Entry<String, String[]> entry : dataMap.entrySet()){
			ParseObject financeGoal = new ParseObject("financeGoal");
			financeGoal.put("Owner", ParseUser.getCurrentUser());
			financeGoal.put("goalName", entry.getKey());
			financeGoal.put("goalType", entry.getValue()[0]);
			financeGoal.put("goalDate", entry.getValue()[1]);
			financeGoal.saveInBackground();
		 }
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
		
		//action_add
		if(requestCode == 0) {
			
			//clears any initial data
			if(goalList.get(0).equals(getString(R.string.none))) {
			
				goalList.remove(0);
				dataMap.remove(getString(R.string.none));
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
			popUp.setOnItemClickListener(this);
		}
		else if(requestCode == 1) {	//edit goal
			
			//goalLength, goalDate
			goalData = data.getStringArrayExtra("data");	
			
			//goalName -- add to list
			goalList.add(data.getStringExtra("name"));
			
			//save data to dataMap
			dataMap.put(data.getStringExtra("name"), goalData);
			
			//remove old data
			dataMap.remove(data.getStringExtra("oldName"));
			goalList.remove(data.getStringExtra("oldName"));
			
			//update screen
			this.onContentChanged();
			
			//reset listeners
			getListView().setOnItemClickListener(this);
			popUp.setOnItemClickListener(this);
		}
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
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
			}
		}
		else {		
			
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
}
