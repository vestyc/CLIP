package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.*;

public class EducationFuture extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	String futureName;
	ArrayList<String> futureList;
	ArrayAdapter<String> listViewAdapter;
	
	String[] dataString;						//{planType, school, location, comments/notes}
	ArrayList<String> courseList;
	HashMap<String, String[]> dataStringMap;
	HashMap<String, ArrayList<String>> courseMap;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getListView().setBackgroundColor(Color.GRAY);
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_education_future, 
				R.id.label_educationFutureList, futureList);
		this.setListAdapter(listViewAdapter);
		
		//initiate pop-up list (edit/remove)
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
		
		//Setup alert dialog
		removeConfirm = new AlertDialog.Builder(this);
		removeConfirm.setMessage("Are you sure you want to remove?");
		
		removeConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog,int id) {
				
				// if this button is clicked, remove item
				futureList.remove(futureName);
				dataStringMap.remove(futureName);
				courseMap.remove(futureName);
				
				if(futureList.isEmpty()) {
					
					resetEmptyList();
				}
				
				updateScreen();
				saveToCloud();
			}
		  });
		
		removeConfirm.setNegativeButton("No",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog,int id) {
				
				// if this button is clicked, close dialog and do nothing
				dialog.cancel();
			}
		});
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//remove local data (overriding with cloud data)
		dataStringMap.clear();
		futureList.clear();
		courseMap.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationFuture");
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
			for (ParseObject future : postList) {				
					
				futureName = future.getString("futureName");	
				
				//add data from database 
				futureList.add(futureName);
				
				//dataStringMap
				ArrayList<String> tempDataString = (ArrayList<String>) future.get("dataString");
				tempDataString.toArray(dataString);
				dataStringMap.put(futureName, dataString);
				
				//courseMap
				courseMap.put(futureName, (ArrayList<String>) future.get("course"));
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(futureList.isEmpty()) {
			
			this.resetEmptyList();
		}
		
		this.updateScreen();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
		
			//edit job
			if(requestCode == 1) {	
				
				//remove old data first
				this.futureName = data.getStringExtra("oldName");
				this.futureList.remove(futureName);
				this.dataStringMap.remove(futureName);
				this.courseMap.remove(futureName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				futureList.remove(getString(R.string.none));
			}
			
			//add new data
			this.futureName = data.getStringExtra("futureName");
			
			futureList.add(futureName);
			dataStringMap.put(futureName, data.getStringArrayExtra("dataString"));
			
			courseList = data.getStringArrayListExtra("course");
			courseMap.put(futureName, courseList);
			
			this.updateScreen();
			this.saveToCloud();
		}
		catch(NullPointerException n) {
			//back button was pressed instead of save
			Toast.makeText(this.getApplicationContext(), "You didn't save!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.education_future, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(EducationFuture.this, EducationFutureEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!futureName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(EducationFuture.this, EducationFutureEdit.class);
					
					i.putExtra("futureName", futureName);
					
					this.dataString = this.dataStringMap.get(futureName);
					i.putExtra("dataString", this.dataString);
					
					this.courseList = this.courseMap.get(futureName);
					i.putExtra("course", courseList);
					
					startActivityForResult(i, 1);
				}
				//remove is clicked 
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					//create alert dialog
					AlertDialog removePopup = removeConfirm.create();
					removePopup.show();
				}
			}
		}
		//show details if company != none
		else if(!futureList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(EducationFuture.this, EducationFutureDetail.class);
			
			futureName = futureList.get(position);
			i.putExtra("futureName", futureName);
			
			this.dataString = this.dataStringMap.get(futureName);
			i.putExtra("dataString", dataString);
			
			this.courseList = this.courseMap.get(futureName);
			i.putExtra("course", courseList);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		futureName = futureList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		futureList = new ArrayList<String>();
		futureList.add(getString(R.string.none));
		
		dataString = new String[4];
		
		dataStringMap = new HashMap<String, String[]>();
		courseMap = new HashMap<String, ArrayList<String>>();
	}
	
	private void resetEmptyList() {
		
		futureList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationFuture");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject future : postList) {
				
				future.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String futureName : futureList){
			 
			if(futureName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject educationFuture = new ParseObject("educationFuture");
			educationFuture.put("Owner", ParseUser.getCurrentUser());
			
			educationFuture.put("futureName", futureName);
			
			//{planType, school, location, comments/notes}
			dataString = dataStringMap.get(futureName);
			ArrayList<String> tempDataString = new ArrayList<String>();
			for(int i=0; i < 4; i++) {
				
				tempDataString.add(dataString[i]);
			}
			educationFuture.addAll("dataString", tempDataString);
			
			//courseMap
			educationFuture.addAll("course", courseMap.get(futureName));
			
			try {
				
				educationFuture.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
