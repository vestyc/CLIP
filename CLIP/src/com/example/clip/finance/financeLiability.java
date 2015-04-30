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
import android.widget.*;
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

public class financeLiability extends ListActivity implements OnItemClickListener, OnItemLongClickListener{

	HashMap<String, String[]> dataMap;	//<liabilityName, liabilityData>
	String[] liabilityData;					//{liabilityAmount}
	String liabilityName;
	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> liabilityList, popUpItems;
	ListPopupWindow popUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		//initiate list view			
		liabilityList = new ArrayList<String>();
		liabilityList.add(getString(R.string.none));		
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_finance_liability,
				R.id.label, liabilityList);		
		setListAdapter(listViewAdapter);
		
		//initiate data variables
		dataMap = new HashMap<String, String[]>();
		liabilityData = new String[1];
		
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
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		
		//remove local data (overriding with cloud data)
		dataMap.clear();
		liabilityList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeliability");
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
			for (ParseObject liability : postList) {			
					
				liabilityName = liability.getString("liabilityName");	
				liabilityList.remove(getString(R.string.none));		//remove "none" if present
				
				//add data from database 
				liabilityList.add(liabilityName);
				liabilityData = new String[1];
				liabilityData[0] = liability.getString("liabilityType");	
				dataMap.put(liabilityName, liabilityData);		
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(liabilityList.isEmpty()) {
			
			liabilityList.add(getString(R.string.none));
		}
		
		//update screen
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_liability, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(financeLiability.this, financeLiabilityEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
		
			//action_add
			if(requestCode == 0) {
				
				//clears any initial data
				liabilityList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				
				//liabilityLength, liabilityDate
				liabilityData = data.getStringArrayExtra("data");
				
				//liabilityName -- add to list
				liabilityList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), liabilityData);
				
				//update screen
				this.onContentChanged();
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			else if(requestCode == 1) {	//edit liability
				
				//remove old data
				dataMap.remove(data.getStringExtra("oldName"));
				liabilityList.remove(data.getStringExtra("oldName"));
				
				//liabilityLength, liabilityDate
				liabilityData = data.getStringArrayExtra("data");	
				
				//liabilityName -- add to list
				liabilityList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), liabilityData);
				
				//update screen
				this.onContentChanged();
				
				//reset listeners
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			
			this.saveToCloud();
		}
		catch(NullPointerException n) {
			//back button press on edit screen
		}
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when liabilityName != none
			if(!liabilityName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(financeLiability.this, financeLiabilityEdit.class);
					i.putExtra("name", liabilityName);
					i.putExtra("data", dataMap.get(liabilityName));
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					liabilityList.remove(liabilityName);
					dataMap.remove(liabilityName);
					
					if(liabilityList.isEmpty()) {
					
						liabilityList.add(getString(R.string.none));
						liabilityData = new String[] {"liability Type/ N/A"};
						dataMap.put(liabilityList.get(0), liabilityData);
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		else if(!liabilityList.get(position).equals(getString(R.string.none)))  {		
			
			Intent i = new Intent(financeLiability.this, financeLiabilityDetail.class);		
			this.liabilityData = dataMap.get(liabilityList.get(position));
			i.putExtra("name", liabilityList.get(position));
			i.putExtra("data", this.liabilityData);
			startActivity(i);	
		}		
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.liabilityName = liabilityList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeliability");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject liability : postList) {
				
				liability.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(Map.Entry<String, String[]> entry : dataMap.entrySet()){
			
			if(entry.getKey().equals(getString(R.string.none)))
				continue;
			 
			ParseObject financeliability = new ParseObject("financeliability");
			financeliability.put("Owner", ParseUser.getCurrentUser());
			financeliability.put("liabilityName", entry.getKey());
			financeliability.put("liabilityType", entry.getValue()[0]);
			
			try {
				
				financeliability.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}

