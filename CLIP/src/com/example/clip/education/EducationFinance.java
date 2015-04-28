package com.example.clip.education;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.example.clip.career.CareerContact;
import com.example.clip.career.CareerContactDetail;
import com.example.clip.career.CareerContactEdit;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.*;
import android.view.View;
import android.app.AlertDialog.Builder;

public class EducationFinance extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	ArrayAdapter<String> listViewAdapter;
	ArrayList<String> financeList;
	String[] dataString;						//{financeType, funds, upkeep, comments/notes}
	HashMap<String, String[]> dataStringMap;
	
	String financeName;
	
	AlertDialog.Builder removeConfirm;
	boolean safeToRemove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_education_finance, 
				R.id.label_educationFinanceList, financeList);
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
				financeList.remove(financeName);
				dataStringMap.remove(financeName);
				
				if(financeList.isEmpty()) {
					
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
		financeList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationFinance");
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
			for (ParseObject finance : postList) {				
					
				financeName = finance.getString("financeName");	
				
				//add data from database 
				financeList.add(financeName);
				
				//{financeType, funds, upkeep, comments/notes}	
				this.dataString = new String[4];
				this.dataString[0] = finance.getString("financeType");
				this.dataString[1] = finance.getString("funds");
				this.dataString[2] = finance.getString("upkeep");
				this.dataString[3] = finance.getString("comments");
				this.dataStringMap.put(financeName, dataString);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(financeList.isEmpty()) {
			
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
				this.financeName = data.getStringExtra("oldName");
				this.financeList.remove(financeName);
				this.dataStringMap.remove(financeName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				financeList.remove(getString(R.string.none));
			}
			
			//add new data
			this.financeName = data.getStringExtra("financeName");
			
			financeList.add(financeName);
			dataStringMap.put(financeName, data.getStringArrayExtra("dataString"));		
			
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
		getMenuInflater().inflate(R.menu.education_finance, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(EducationFinance.this, EducationFinanceEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!financeName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(EducationFinance.this, EducationFinanceEdit.class);
					
					i.putExtra("financeName", financeName);
					
					//{financeType, funds, upkeep, comments/notes}
					this.dataString = this.dataStringMap.get(financeName);
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
		//show details if company != none
		else if(!financeList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(EducationFinance.this, EducationFinanceDetail.class);
			
			financeName = financeList.get(position);
			i.putExtra("financeName", financeName);
			
			//{financeType, funds, upkeep, comments/notes}
			this.dataString = this.dataStringMap.get(financeName);
			i.putExtra("dataString", dataString);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		financeName = financeList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		financeList = new ArrayList<String>();
		financeList.add(getString(R.string.none));		
		
		dataString = new String[4];
		dataStringMap = new HashMap<String, String[]> ();
	}
	
	private void resetEmptyList() {
		
		financeList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("educationFinance");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject finance : postList) {
				
				finance.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String financeName : financeList){
			 
			if(financeName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject educationFinance = new ParseObject("educationFinance");
			educationFinance.put("Owner", ParseUser.getCurrentUser());
			
			educationFinance.put("financeName", financeName);
			
			//{financeType, funds, upkeep, comments/notes}
			this.dataString = this.dataStringMap.get(financeName);
			educationFinance.put("financeType", dataString[0]);
			educationFinance.put("funds", dataString[1]);
			educationFinance.put("upkeep", dataString[2]);
			educationFinance.put("comments", dataString[3]);
			
			try {
				
				educationFinance.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
