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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class HealthRecipe extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	String recipeName;
	ArrayList<String> recipeList;
	ArrayAdapter<String> listViewAdapter;
	
	String[] dataString;	//URL, notes
	HashMap<String, String[]> dataStringMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getListView().setBackgroundColor(Color.GRAY);
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_health_recipe, 
				R.id.healthRecipe_list, recipeList);
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
				recipeList.remove(recipeName);
				dataStringMap.remove(recipeName);
				
				if(recipeList.isEmpty()) {
					
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
		recipeList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthRecipe");
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
			for (ParseObject recipe : postList) {				
					
				recipeName = recipe.getString("recipeName");	
				
				//add data from database 
				recipeList.add(recipeName);
				
				//dataStringMap
				ArrayList<String> tempDataString = (ArrayList<String>) recipe.get("dataString");
				dataString = new String[2];
				tempDataString.toArray(dataString);
				dataStringMap.put(recipeName, dataString);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(recipeList.isEmpty()) {
			
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
				this.recipeName = data.getStringExtra("oldName");
				this.recipeList.remove(recipeName);
				this.dataStringMap.remove(recipeName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				recipeList.remove(getString(R.string.none));
			}
			
			//add new data
			recipeName = new String();
			recipeName = data.getStringExtra("recipeName");
			
			recipeList.add(recipeName);
			dataString = new String[2];
			dataString = data.getStringArrayExtra("dataString");
			dataStringMap.put(recipeName, dataString);
			
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

			Intent i = new Intent(HealthRecipe.this, HealthRecipeEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			if(!recipeName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(HealthRecipe.this, HealthRecipeEdit.class);
					
					i.putExtra("recipeName", recipeName);
					
					this.dataString = this.dataStringMap.get(recipeName);
					i.putExtra("dataString", this.dataString);
					
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
		else if(!recipeList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(HealthRecipe.this, HealthRecipeDetail.class);
			
			recipeName = recipeList.get(position);
			i.putExtra("recipeName", recipeName);
			
			this.dataString = this.dataStringMap.get(recipeName);
			i.putExtra("dataString", dataString);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		recipeName = recipeList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		recipeList = new ArrayList<String>();
		recipeList.add(getString(R.string.none));
		
		this.dataString = new String[2];
		
		this.dataStringMap = new HashMap<String, String[]>();
	}
	
	private void resetEmptyList() {
		
		recipeList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("healthRecipe");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject recipe : postList) {
				
				recipe.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String recipeName : recipeList){
			 
			if(recipeName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject healthRecipe = new ParseObject("healthRecipe");
			healthRecipe.put("Owner", ParseUser.getCurrentUser());
			
			healthRecipe.put("recipeName", recipeName);
			
			dataString = dataStringMap.get(recipeName);
			ArrayList<String> tempDataString = new ArrayList<String>();
			for(String data : dataString) {
				
				tempDataString.add(data);
			}
			healthRecipe.put("dataString", tempDataString);			
			
			try {
				
				healthRecipe.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
