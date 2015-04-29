package com.example.clip.education;

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

public class EducationCurrent extends ListActivity implements OnItemClickListener, OnItemLongClickListener  {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	ArrayAdapter<String> listViewAdapter;
	ArrayList<String> currentList;
	String currentName;
	
	String dataString;		//degreeType
	int[][] dataInt;		//[dateType][month, day, year]
	HashMap<String, String> dataStringMap;
	HashMap<String, int[][]> dataIntMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_education_current, 
				R.id.educationCurrent_listName, currentList);
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
				currentList.remove(currentName);
				dataStringMap.remove(currentName);
				dataIntMap.remove(currentName);
				
				if(currentList.isEmpty()) {
					
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
		currentList.clear();
		dataIntMap.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationCurrent");
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
			for (ParseObject current : postList) {				
					
				currentName = current.getString("currentName");	
				
				//add data from database 
				currentList.add(currentName);
				
				//dataStringMap
				dataStringMap.put(currentName, (String) current.get("dataString"));
				
				//dataIntMap
				ArrayList<Integer> dateStart = (ArrayList<Integer>) current.get("dateStart");
				ArrayList<Integer> dateEnd = (ArrayList<Integer>) current.get("dateEnd");
				dataInt = new int[2][3];
				for(int i=0; i < dataInt[0].length; i++) {
					
					dataInt[0][i] = dateStart.get(i);
					dataInt[1][i] = dateEnd.get(i);
				}
				dataIntMap.put(currentName, dataInt);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(currentList.isEmpty()) {
			
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
				this.currentName = data.getStringExtra("oldName");
				this.currentList.remove(currentName);
				this.dataStringMap.remove(currentName);
				this.dataIntMap.remove(currentName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				currentList.remove(getString(R.string.none));
			}
			
			//add new data
			this.currentName = data.getStringExtra("currentName");
			
			currentList.add(currentName);
			dataStringMap.put(currentName, data.getStringExtra("dataString"));
			
			dataInt = new int[2][3];
			dataInt[0] = data.getIntArrayExtra("dateStart");
			dataInt[1] = data.getIntArrayExtra("dateEnd");
			dataIntMap.put(currentName, dataInt);
			
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
		getMenuInflater().inflate(R.menu.education_current, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(EducationCurrent.this, EducationCurrentEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!currentName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(EducationCurrent.this, EducationCurrentEdit.class);
					
					i.putExtra("currentName", currentName);
					
					this.dataString = this.dataStringMap.get(currentName);
					i.putExtra("dataString", this.dataString);
					
					this.dataInt = this.dataIntMap.get(currentName);
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
		else if(!currentList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(EducationCurrent.this, EducationCurrentDetail.class);
			
			currentName = currentList.get(position);
			i.putExtra("currentName", currentName);
			
			this.dataString = this.dataStringMap.get(currentName);
			i.putExtra("dataString", dataString);
			
			this.dataInt = this.dataIntMap.get(currentName);
			i.putExtra("dateStart", dataInt[0]);
			i.putExtra("dateEnd", dataInt[1]);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		currentName = currentList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		currentList = new ArrayList<String>();
		currentList.add(getString(R.string.none));
		
		this.dataInt = new int[2][3];
		this.dataString = new String();
		
		this.dataIntMap = new HashMap<String, int[][]>();
		this.dataStringMap = new HashMap<String, String>();
	}
	
	private void resetEmptyList() {
		
		currentList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationCurrent");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject current : postList) {
				
				current.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String currentName : currentList){
			 
			if(currentName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject educationCurrent = new ParseObject("educationCurrent");
			educationCurrent.put("Owner", ParseUser.getCurrentUser());
			
			educationCurrent.put("currentName", currentName);
			
			//degree type
			dataString = dataStringMap.get(currentName);
			educationCurrent.put("dataString", dataString);
			
			dataInt = dataIntMap.get(currentName);
			ArrayList<Integer> dateStart = new ArrayList<Integer>();
			ArrayList<Integer> dateEnd = new ArrayList<Integer>();
			for(int i=0; i < dataInt[0].length; i++) {
				
				dateStart.add(dataInt[0][i]);
				dateEnd.add(dataInt[1][i]);
			}
			educationCurrent.addAll("dateStart", dateStart);
			educationCurrent.addAll("dateEnd", dateEnd);			
			
			try {
				
				educationCurrent.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
