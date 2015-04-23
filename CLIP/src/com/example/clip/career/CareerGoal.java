package com.example.clip.career;

import com.example.clip.Entry;
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
import android.widget.*;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.Intent;

public class CareerGoal extends ListActivity implements OnItemClickListener, OnItemLongClickListener{

	HashMap<String, String[]> dataMap;	//<goalName, goalData>
	String[] goalData;					//{goalLength, goalDate}
	String goalName;
	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> goalList, popUpItems;
	ListPopupWindow popUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//build initial goalData	
		goalList = new ArrayList<String>();
		dataMap = new HashMap<String, String[]>();
		
		goalList.add("None");
		goalData = new String[] {"Goal Type N/A", "Completion Date N/A"};
		dataMap.put(goalList.get(0), goalData);
		
		//initiate the pop-up list
		popUp = new ListPopupWindow(this);
		popUpItems = new ArrayList<String>();
		popUpItems.add("Edit");
		popUpItems.add("Remove");
		popUpAdapter = new ArrayAdapter<String>(this, R.layout.edit_remove_popup,
				R.id.label_popUp, popUpItems);
		popUp.setAdapter(popUpAdapter);
		popUp.setModal(true);
		popUp.setWidth(200);
		popUp.setHeight(ListPopupWindow.WRAP_CONTENT);
		
		//initiate list view
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_career_goal,
				R.id.label, goalList);		
		setListAdapter(listViewAdapter);
		
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
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
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//action_add
		if(requestCode == 0) {
			
			//clears any initial data
			if(goalList.get(0).equals("None")) {
			
				goalList.remove(0);
				dataMap.remove("None");
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
			if(this.popUpItems.get(position).equals("Edit")) {
											
				Intent i = new Intent(CareerGoal.this, CareerGoalEdit.class);
				i.putExtra("name", goalName);
				i.putExtra("data", dataMap.get(goalName));
				this.startActivityForResult(i, 1);
			}
			//remove is clicked
			else if(this.popUpItems.get(position).equals("Remove")) {
				
				goalList.remove(goalName);
				dataMap.remove(goalName);
				
				if(goalList.isEmpty()) {
				
					goalList.add("None");
					goalData = new String[] {"Goal Type N/A", "Completion Date N/A"};
					dataMap.put(goalList.get(0), goalData);
				}
				this.onContentChanged();
			}
		}
		else {		
			
			Intent i = new Intent(CareerGoal.this, CareerGoalDetail.class);		
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
