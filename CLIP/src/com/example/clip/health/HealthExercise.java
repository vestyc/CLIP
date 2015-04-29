package com.example.clip.health;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.example.clip.education.EducationCurrent;
import com.example.clip.education.EducationCurrentDetail;
import com.example.clip.education.EducationCurrentEdit;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

public class HealthExercise extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	String exerciseName;
	ArrayList<String> exerciseList;
	ArrayAdapter<String> listViewAdapter;
	
	String[] dataString;						//sun, mon, tues, wed, thurs, fri, sat
	int[][] dataInt;							//[dateStart/dateEnd][month, day, year]
	HashMap<String, String[]> dataStringMap;
	HashMap<String, int[][]> dataIntMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//initiate empty list
				this.createEmptyList();
				listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_health_exercise, 
						R.id.healthExercise_list, exerciseList);
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
						exerciseList.remove(exerciseName);
						dataStringMap.remove(exerciseName);
						dataIntMap.remove(exerciseName);
						
						if(exerciseList.isEmpty()) {
							
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
		exerciseList.clear();
		dataIntMap.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthExercise");
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
			for (ParseObject exercise : postList) {				
					
				exerciseName = exercise.getString("exerciseName");	
				
				//add data from database 
				exerciseList.add(exerciseName);
				
				//dataStringMap
				ArrayList<String> tempDataString = (ArrayList<String>) exercise.get("dataString");
				tempDataString.toArray(dataString);
				dataStringMap.put(exerciseName, dataString);
				
				//dataIntMap
				ArrayList<Integer> dateStart = (ArrayList<Integer>) exercise.get("dateStart");
				ArrayList<Integer> dateEnd = (ArrayList<Integer>) exercise.get("dateEnd");
				dataInt = new int[2][3];
				for(int i=0; i < dataInt[0].length; i++) {
					
					dataInt[0][i] = dateStart.get(i);
					dataInt[1][i] = dateEnd.get(i);
				}
				dataIntMap.put(exerciseName, dataInt);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(exerciseList.isEmpty()) {
			
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
				this.exerciseName = data.getStringExtra("oldName");
				this.exerciseList.remove(exerciseName);
				this.dataStringMap.remove(exerciseName);
				this.dataIntMap.remove(exerciseName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				exerciseList.remove(getString(R.string.none));
			}
			
			//add new data
			this.exerciseName = data.getStringExtra("exerciseName");
			
			exerciseList.add(exerciseName);
			dataStringMap.put(exerciseName, data.getStringArrayExtra("dataString"));
			
			dataInt = new int[2][3];
			dataInt[0] = data.getIntArrayExtra("dateStart");
			dataInt[1] = data.getIntArrayExtra("dateEnd");
			dataIntMap.put(exerciseName, dataInt);
			
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
		getMenuInflater().inflate(R.menu.health_exercise, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {

			Intent i = new Intent(HealthExercise.this, HealthExerciseEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!exerciseName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(HealthExercise.this, HealthExerciseEdit.class);
					
					i.putExtra("exerciseName", exerciseName);
					
					this.dataString = this.dataStringMap.get(exerciseName);
					i.putExtra("dataString", this.dataString);
					
					this.dataInt = this.dataIntMap.get(exerciseName);
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
		else if(!exerciseList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(HealthExercise.this, HealthExerciseDetail.class);
			
			exerciseName = exerciseList.get(position);
			i.putExtra("exerciseName", exerciseName);
			
			this.dataString = this.dataStringMap.get(exerciseName);
			i.putExtra("dataString", dataString);
			
			this.dataInt = this.dataIntMap.get(exerciseName);
			i.putExtra("dateStart", dataInt[0]);
			i.putExtra("dateEnd", dataInt[1]);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		exerciseName = exerciseList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		exerciseList = new ArrayList<String>();
		exerciseList.add(getString(R.string.none));
		
		this.dataInt = new int[2][3];
		this.dataString = new String[7];
		
		this.dataIntMap = new HashMap<String, int[][]>();
		this.dataStringMap = new HashMap<String, String[]>();
	}
	
	private void resetEmptyList() {
		
		exerciseList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthExercise");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject exercise : postList) {
				
				exercise.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String exerciseName : exerciseList){
			 
			if(exerciseName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject healthExercise = new ParseObject("healthExercise");
			healthExercise.put("Owner", ParseUser.getCurrentUser());
			
			healthExercise.put("exerciseName", exerciseName);
			
			//degree type
			dataString = dataStringMap.get(exerciseName);
			ArrayList<String> tempDataString = new ArrayList<String>();
			for(String data : dataString) {
				
				tempDataString.add(data);
			}
			healthExercise.put("dataString", tempDataString);
			
			dataInt = dataIntMap.get(exerciseName);
			ArrayList<Integer> dateStart = new ArrayList<Integer>();
			ArrayList<Integer> dateEnd = new ArrayList<Integer>();
			for(int i=0; i < dataInt[0].length; i++) {
				
				dateStart.add(dataInt[0][i]);
				dateEnd.add(dataInt[1][i]);
			}
			healthExercise.addAll("dateStart", dateStart);
			healthExercise.addAll("dateEnd", dateEnd);			
			
			try {
				
				healthExercise.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
