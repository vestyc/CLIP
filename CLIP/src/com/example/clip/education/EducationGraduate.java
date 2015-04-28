package com.example.clip.education;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.example.clip.education.EducationGraduateAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.DialogInterface;
import android.content.Intent;

public class EducationGraduate extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	EducationGraduateAdapter listViewAdapter;
	ArrayList<EducationGraduateData> graduateList;
	EducationGraduateData graduateObj;
	String graduateName;
	
	String[] dataString;						//actionType, degreeType, planType, location, applicOutcome, comments
	HashMap<String, String[]> dataStringMap;
	int[] dataInt;								//applicDate = month, day, year
	HashMap<String, int[]> dataIntMap;
	
	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new EducationGraduateAdapter(this, R.layout.activity_education_graduate, graduateList);
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
				removeGraduate(graduateName);
				dataStringMap.remove(graduateName);
				dataIntMap.remove(graduateName);
				
				if(graduateList.isEmpty()) {
					
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
		graduateList.clear();
		dataIntMap.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationGraduate");
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
			for (ParseObject graduate : postList) {				
				
				//grab data from cloud
				graduateName = graduate.getString("graduateName");
				
				ArrayList<String> tempDataString = (ArrayList<String>) graduate.get("dataString");
				tempDataString.toArray(dataString);
				
				ArrayList<Integer> tempDataInt = (ArrayList<Integer>) graduate.get("dataInt");
				if(tempDataInt != null) {
					
					for(int i = 0; i < 3; i++) {
						
						dataInt[i] = (int) tempDataInt.get(i);
					}
					//dataIntMap
					dataIntMap.put(graduateName, dataInt);
				}
				
				//add data to local variables
				this.addGraduate(graduateName, dataString[0]);
				
				//dataStringMap
				dataStringMap.put(graduateName, dataString);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(graduateList.isEmpty()) {
			
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
				this.graduateName = data.getStringExtra("oldName");
				this.removeGraduate(graduateName);
				this.dataStringMap.remove(graduateName);
				this.dataIntMap.remove(graduateName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				this.removeGraduate(getString(R.string.none));
			}
			
			//actionType, degreeType, planType, location, applicOutcome, comments
			//date
			//get data from extras
			this.graduateName = data.getStringExtra("graduateName");
			dataString = data.getStringArrayExtra("dataString");
			
			//populate maps and lists with data
			this.addGraduate(graduateName, dataString[0]);			
			dataStringMap.put(graduateName, dataString);	
			
			if(data.getIntArrayExtra("dataInt") != null) {
				
				dataInt = data.getIntArrayExtra("dataInt");
				dataIntMap.put(graduateName, dataInt);
			}
			
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
		getMenuInflater().inflate(R.menu.education_graduate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(EducationGraduate.this, EducationGraduateEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!graduateName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(EducationGraduate.this, EducationGraduateEdit.class);
					
					i.putExtra("graduateName", graduateName);
					
					this.dataString = this.dataStringMap.get(graduateName);
					i.putExtra("dataString", this.dataString);
					
					this.dataInt = this.dataIntMap.get(graduateName);
					i.putExtra("dataInt", dataInt);
					
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
		else  {
			
			EducationGraduateData current = graduateList.get(position);			
			if(!current.getName().equals(getString(R.string.none))) {
			
				Intent i = new Intent(EducationGraduate.this, EducationGraduateDetail.class);
				
				graduateName = current.getName();
				i.putExtra("graduateName", graduateName);
				
				this.dataString = this.dataStringMap.get(graduateName);
				i.putExtra("dataString", dataString);
				
				this.dataInt = this.dataIntMap.get(graduateName);
				i.putExtra("dataInt", dataInt);
				
				startActivity(i);
			}
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { 
		
		graduateName = graduateList.get(position).getName();
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void addGraduate(String name, String actionType) {
		
		EducationGraduateData toAdd = new EducationGraduateData(name, actionType);
		graduateList.add(toAdd);
	}
	
	private void removeGraduate(String graduateName) {
		
		EducationGraduateData toDelete = new EducationGraduateData();
		
		for(EducationGraduateData temp : graduateList) {
			
			if(temp.getName().equals(graduateName)) {
				
				toDelete = temp;
				break;
			}
		}
		
		graduateList.remove(toDelete);
	}	
	
	private void createEmptyList() {
		
		graduateObj = new EducationGraduateData(getString(R.string.none), "");
		graduateList = new ArrayList<EducationGraduateData>();
		graduateList.add(graduateObj);
		
		dataString = new String[6];
		dataInt = new int[3];
		
		dataStringMap = new HashMap<String, String[]>();
		dataIntMap = new HashMap<String, int[]>();
	}
	
	private void resetEmptyList() {
		
		graduateObj = new EducationGraduateData(getString(R.string.none), "");
		graduateList.add(graduateObj);
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationGraduate");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject graduate : postList) {
				
				graduate.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(EducationGraduateData graduate : graduateList){
			 
			if(graduate.getName().equals(getString(R.string.none)))
				continue;
			 
			ParseObject educationGraduate = new ParseObject("educationGraduate");
			educationGraduate.put("Owner", ParseUser.getCurrentUser());
			
			graduateName = graduate.getName();
			educationGraduate.put("graduateName", graduateName);
			
			dataString = dataStringMap.get(graduateName);
			ArrayList<String> tempDataString = new ArrayList<String>();
			for(int i=0; i < 6; i++) {
				
				tempDataString.add(dataString[i]);
			}
			educationGraduate.addAll("dataString", tempDataString);
			
			dataInt = dataIntMap.get(graduateName);
			if(dataInt != null) {
				
				ArrayList<Integer> tempDataInt = new ArrayList<Integer>();
				for(int i = 0; i < 3; i++) {
					
					tempDataInt.add((Integer) dataInt[i]);
				}
				educationGraduate.addAll("dataInt", tempDataInt);
			}
			
			try {
				
				educationGraduate.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
