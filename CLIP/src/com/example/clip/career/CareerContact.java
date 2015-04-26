package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.Intent;

import com.parse.*;

public class CareerContact extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	HashMap<String, String[]> dataStringMap;	//<contactName, dataString>
	HashMap<String, int[]> dataIntMap;			//<contactName, dataInt>
	String[] dataString;						//{affiliation, comments}
	int[] dataInt;								//{estMonth, estDay, estYear, #OfUse}
	
	String contactName;
	ArrayList<String> contactList;
	ArrayAdapter<String> listViewAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_career_contact, 
				R.id.label_contactList, contactList);
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
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		//remove local data (overriding with cloud data)
		dataStringMap.clear();
		dataIntMap.clear();
		contactList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("careerContact");
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
			for (ParseObject contact : postList) {				
					
				contactName = contact.getString("contactName");	
				
				//add data from database 
				contactList.add(contactName);
				
				//<contactName, dataString>
				//{affiliation, comments}				
				this.dataString = new String[2];
				this.dataString[0] = contact.getString("affiliation");
				this.dataString[1] = contact.getString("comments");
				this.dataStringMap.put(contactName, dataString);
				
				//<contactName, dataInt>
				//{estMonth, estDay, estYear, #OfUse}	
				this.dataInt = new int[4];
				this.dataInt[0] = contact.getInt("month");
				this.dataInt[1] = contact.getInt("day");
				this.dataInt[2] = contact.getInt("year");
				this.dataInt[3] = contact.getInt("uses");
				this.dataIntMap.put(contactName, dataInt);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(contactList.isEmpty()) {
			
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
				this.contactName = data.getStringExtra("oldName");
				this.contactList.remove(contactName);
				this.dataStringMap.remove(contactName);
				this.dataIntMap.remove(contactName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				contactList.remove(getString(R.string.none));
			}
			
			//add new data
			this.contactName = data.getStringExtra("name");
			
			contactList.add(contactName);
			dataStringMap.put(contactName, data.getStringArrayExtra("dataString"));
			dataIntMap.put(contactName, data.getIntArrayExtra("dataInt"));			
			
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
		getMenuInflater().inflate(R.menu.career_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(CareerContact.this, CareerContactEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!contactName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(CareerContact.this, CareerContactEdit.class);
					
					i.putExtra("name", contactName);
					
					//<contactName, dataString>
					//{affiliation, comments}
					this.dataString = this.dataStringMap.get(contactName);
					i.putExtra("dataString", this.dataString);
					
					//<contactName, dataInt>
					//{estMonth, estDay, estYear, #OfUse}
					this.dataInt = this.dataIntMap.get(contactName);
					i.putExtra("dataInt", this.dataInt);
					
					startActivityForResult(i, 1);
				}
				//remove is clicked 
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					contactList.remove(contactName);
					dataStringMap.remove(contactName);
					dataIntMap.remove(contactName);
					
					if(contactList.isEmpty()) {
						
						this.resetEmptyList();
					}
					
					this.updateScreen();
					this.saveToCloud();
				}
			}
		}
		//show details if company != none
		else if(!contactList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(CareerContact.this, CareerContactDetail.class);
			
			contactName = contactList.get(position);
			i.putExtra("name", contactName);
			
			//<contactName, dataString>
			//{affiliation, comments}
			this.dataString = this.dataStringMap.get(contactName);
			i.putExtra("dataString", dataString);
			
			//<contactName, dataInt>
			//{estMonth, estDay, estYear, #OfUse}
			this.dataInt = this.dataIntMap.get(contactName);
			i.putExtra("dataInt", dataInt);
			
			startActivity(i);
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		contactName = contactList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		contactName = new String();
		contactName = getString(R.string.none);
		
		contactList = new ArrayList<String>();
		contactList.add(contactName);
		
		this.dataIntMap = new HashMap<String, int[]> ();
		this.dataStringMap = new HashMap<String, String[]> ();
		
		this.dataInt = new int[4];
		this.dataString = new String[2];
	}
	
	private void resetEmptyList() {
		
		contactList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("careerContact");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject contact : postList) {
				
				contact.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		for(String contactName : contactList){
			 
			if(contactName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject careerContact = new ParseObject("careerContact");
			careerContact.put("Owner", ParseUser.getCurrentUser());
			
			careerContact.put("contactName", contactName);
			
			//<contactName, dataString>
			//{affiliation, comments}
			this.dataString = this.dataStringMap.get(contactName);
			careerContact.put("affiliation", dataString[0]);
			careerContact.put("comments", dataString[1]);
			
			//<contactName, dataInt>
			//{estMonth, estDay, estYear, #OfUse}	
			this.dataInt = this.dataIntMap.get(contactName);
			careerContact.put("month", dataInt[0]);
			careerContact.put("day", dataInt[1]);
			careerContact.put("year", dataInt[2]);
			careerContact.put("uses", dataInt[3]);
			
			try {
				
				careerContact.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		}
	}	
}
