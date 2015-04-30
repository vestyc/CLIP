package com.example.clip.health;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class HealthDiet extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	String dietName;
	ArrayList<String> dietList;
	ArrayAdapter<String> listViewAdapter;
	
	String[] dataString;	//sun, mon, tues, wed, thurs, fri, sat
	String[] dataString2;	//dietType, notes/instructions
	int[][] dataInt;		//[dateStart/dateEnd][month, day, year]
	HashMap<String, String[]> dataStringMap, dataStringMap2;
	HashMap<String, int[][]> dataIntMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_health_diet, 
				R.id.healthDiet_list, dietList);
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
				dietList.remove(dietName);
				dataStringMap.remove(dietName);
				dataStringMap2.remove(dietName);
				dataIntMap.remove(dietName);
				
				if(dietList.isEmpty()) {
					
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
		dataStringMap2.clear();
		dietList.clear();
		dataIntMap.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthDiet");
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
			for (ParseObject diet : postList) {				
					
				dietName = diet.getString("dietName");	
				
				//add data from database 
				dietList.add(dietName);
				
				//dataStringMap
				ArrayList<String> tempDataString = (ArrayList<String>) diet.get("dataString");
				tempDataString.toArray(dataString);
				dataStringMap.put(dietName, dataString);
				
				//dataStringMap2
				tempDataString = (ArrayList<String>) diet.get("dataString2");
				tempDataString.toArray(dataString2);
				dataStringMap2.put(dietName, dataString2);
				
				//dataIntMap
				ArrayList<Integer> dateStart = (ArrayList<Integer>) diet.get("dateStart");
				ArrayList<Integer> dateEnd = (ArrayList<Integer>) diet.get("dateEnd");
				dataInt = new int[2][3];
				for(int i=0; i < dataInt[0].length; i++) {
					
					dataInt[0][i] = dateStart.get(i);
					dataInt[1][i] = dateEnd.get(i);
				}
				dataIntMap.put(dietName, dataInt);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(dietList.isEmpty()) {
			
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
				this.dietName = data.getStringExtra("oldName");
				this.dietList.remove(dietName);
				this.dataStringMap.remove(dietName);
				this.dataStringMap2.remove(dietName);
				this.dataIntMap.remove(dietName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				dietList.remove(getString(R.string.none));
			}
			
			//add new data
			this.dietName = data.getStringExtra("dietName");
			
			dietList.add(dietName);
			dataStringMap.put(dietName, data.getStringArrayExtra("dataString"));
			dataStringMap2.put(dietName, data.getStringArrayExtra("dataString2"));
			
			dataInt = new int[2][3];
			dataInt[0] = data.getIntArrayExtra("dateStart");
			dataInt[1] = data.getIntArrayExtra("dateEnd");
			dataIntMap.put(dietName, dataInt);
			
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
		getMenuInflater().inflate(R.menu.health_diet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {

			Intent i = new Intent(HealthDiet.this, HealthDietEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!dietName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(HealthDiet.this, HealthDietEdit.class);
					
					i.putExtra("dietName", dietName);
					
					this.dataString = this.dataStringMap.get(dietName);
					i.putExtra("dataString", this.dataString);
					
					this.dataString2 = this.dataStringMap2.get(dietName);
					i.putExtra("dataString2", this.dataString2);
					
					this.dataInt = this.dataIntMap.get(dietName);
					i.putExtra("dateStart", dataInt[0]);
					i.putExtra("dateEnd", dataInt[1]);
					
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
		else if(!dietList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(HealthDiet.this, HealthDietDetail.class);
			
			dietName = dietList.get(position);
			i.putExtra("dietName", dietName);
			
			this.dataString = this.dataStringMap.get(dietName);
			i.putExtra("dataString", dataString);
			
			this.dataString2 = this.dataStringMap2.get(dietName);
			i.putExtra("dataString2", dataString2);
			
			this.dataInt = this.dataIntMap.get(dietName);
			i.putExtra("dateStart", dataInt[0]);
			i.putExtra("dateEnd", dataInt[1]);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		dietName = dietList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		dietList = new ArrayList<String>();
		dietList.add(getString(R.string.none));
		
		this.dataInt = new int[2][3];
		this.dataString = new String[7];
		this.dataString2 = new String[2];
		
		this.dataIntMap = new HashMap<String, int[][]>();
		this.dataStringMap = new HashMap<String, String[]>();
		this.dataStringMap2 = new HashMap<String, String[]>();
	}
	
	private void resetEmptyList() {
		
		dietList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthDiet");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject diet : postList) {
				
				diet.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String dietName : dietList){
			 
			if(dietName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject healthDiet = new ParseObject("healthDiet");
			healthDiet.put("Owner", ParseUser.getCurrentUser());
			
			healthDiet.put("dietName", dietName);
			
			dataString = dataStringMap.get(dietName);
			ArrayList<String> tempDataString = new ArrayList<String>();
			for(String data : dataString) {
				
				tempDataString.add(data);
			}
			healthDiet.put("dataString", tempDataString);
			
			dataString2 = dataStringMap2.get(dietName);
			tempDataString = new ArrayList<String>();
			for(String data : dataString2) {
				
				tempDataString.add(data);
			}
			healthDiet.put("dataString2", tempDataString);
			
			dataInt = dataIntMap.get(dietName);
			ArrayList<Integer> dateStart = new ArrayList<Integer>();
			ArrayList<Integer> dateEnd = new ArrayList<Integer>();
			for(int i=0; i < dataInt[0].length; i++) {
				
				dateStart.add(dataInt[0][i]);
				dateEnd.add(dataInt[1][i]);
			}
			healthDiet.addAll("dateStart", dateStart);
			healthDiet.addAll("dateEnd", dateEnd);			
			
			try {
				
				healthDiet.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
