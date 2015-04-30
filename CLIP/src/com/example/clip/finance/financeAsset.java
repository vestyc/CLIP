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

public class financeAsset extends ListActivity implements OnItemClickListener, OnItemLongClickListener{

	HashMap<String, String[]> dataMap;	//<assetName, assetData>
	String[] assetData;					//{assetAmount}
	String assetName;
	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> assetList, popUpItems;
	ListPopupWindow popUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		
		//initiate list view			
		assetList = new ArrayList<String>();
		assetList.add(getString(R.string.none));		
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_finance_asset,
				R.id.label, assetList);		
		setListAdapter(listViewAdapter);
		
		//initiate data variables
		dataMap = new HashMap<String, String[]>();
		assetData = new String[1];
		
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
		assetList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeasset");
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
			for (ParseObject asset : postList) {			
					
				assetName = asset.getString("assetName");	
				assetList.remove(getString(R.string.none));		//remove "none" if present
				
				//add data from database 
				assetList.add(assetName);
				assetData = new String[1];
				assetData[0] = asset.getString("assetType");	
				dataMap.put(assetName, assetData);		
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(assetList.isEmpty()) {
			
			assetList.add(getString(R.string.none));
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
		getMenuInflater().inflate(R.menu.finance_asset, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(financeAsset.this, financeAssetEdit.class);
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
				assetList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				
				//assetLength, assetDate
				assetData = data.getStringArrayExtra("data");
				
				//assetName -- add to list
				assetList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), assetData);
				
				//update screen
				this.onContentChanged();
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			else if(requestCode == 1) {	//edit asset
				
				//remove old data
				dataMap.remove(data.getStringExtra("oldName"));
				assetList.remove(data.getStringExtra("oldName"));
				
				//assetLength, assetDate
				assetData = data.getStringArrayExtra("data");	
				
				//assetName -- add to list
				assetList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), assetData);
				
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
			
			//only operate when assetName != none
			if(!assetName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(financeAsset.this, financeAssetEdit.class);
					i.putExtra("name", assetName);
					i.putExtra("data", dataMap.get(assetName));
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					assetList.remove(assetName);
					dataMap.remove(assetName);
					
					if(assetList.isEmpty()) {
					
						assetList.add(getString(R.string.none));
						assetData = new String[] {"asset Type/ N/A"};
						dataMap.put(assetList.get(0), assetData);
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		else if(!assetList.get(position).equals(getString(R.string.none)))  {		
			
			Intent i = new Intent(financeAsset.this, financeAssetDetail.class);		
			this.assetData = dataMap.get(assetList.get(position));
			i.putExtra("name", assetList.get(position));
			i.putExtra("data", this.assetData);
			startActivity(i);	
		}		
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.assetName = assetList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeasset");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject asset : postList) {
				
				asset.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(Map.Entry<String, String[]> entry : dataMap.entrySet()){
			
			if(entry.getKey().equals(getString(R.string.none)))
				continue;
			 
			ParseObject financeasset = new ParseObject("financeasset");
			financeasset.put("Owner", ParseUser.getCurrentUser());
			financeasset.put("assetName", entry.getKey());
			financeasset.put("assetType", entry.getValue()[0]);
			
			try {
				
				financeasset.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}

