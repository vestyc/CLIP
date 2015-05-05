package com.example.clip.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
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

public class financeStock extends ListActivity implements OnItemClickListener, OnItemLongClickListener{

	HashMap<String, String[]> dataMap;	//<stockName, stockData>
	String[] stockData;					//{stockPrice, stockAmount}
	String stockName;
	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> stockList, popUpItems;
	ListPopupWindow popUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getListView().setBackgroundColor(Color.GRAY);
		//initiate list view			
		stockList = new ArrayList<String>();
		stockList.add(getString(R.string.none));		
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_finance_stock,
				R.id.label, stockList);		
		setListAdapter(listViewAdapter);
		
		//initiate data variables
		dataMap = new HashMap<String, String[]>();
		stockData = new String[2];
		
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
		stockList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeStock");
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
			for (ParseObject stock : postList) {			
					
				stockName = stock.getString("stockName");	
				stockList.remove(getString(R.string.none));		//remove "none" if present
				
				//add data from database 
				stockList.add(stockName);
				stockData = new String[2];
				stockData[0] = stock.getString("stockPrice");
				stockData[1] = stock.getString("stockAmount");	
				dataMap.put(stockName, stockData);		
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(stockList.isEmpty()) {
			
			stockList.add(getString(R.string.none));
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
		getMenuInflater().inflate(R.menu.finance_stock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(financeStock.this, financeStockEdit.class);
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
				stockList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				
				//stockLength, stockDate
				stockData = data.getStringArrayExtra("data");
				
				//stockName -- add to list
				stockList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), stockData);
				
				//update screen
				this.onContentChanged();
				getListView().setOnItemClickListener(this);
				popUp.setOnItemClickListener(this);
			}
			else if(requestCode == 1) {	//edit stock
				
				//remove old data
				dataMap.remove(data.getStringExtra("oldName"));
				stockList.remove(data.getStringExtra("oldName"));
				
				//stockLength, stockDate
				stockData = data.getStringArrayExtra("data");	
				
				//stockName -- add to list
				stockList.add(data.getStringExtra("name"));
				
				//save data to dataMap
				dataMap.put(data.getStringExtra("name"), stockData);
				
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
			
			//only operate when stockName != none
			if(!stockName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(financeStock.this, financeStockEdit.class);
					i.putExtra("name", stockName);
					i.putExtra("data", dataMap.get(stockName));
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					stockList.remove(stockName);
					dataMap.remove(stockName);
					
					if(stockList.isEmpty()) {
					
						stockList.add(getString(R.string.none));
						stockData = new String[] {"stock Type N/A", "Completion Date N/A"};
						dataMap.put(stockList.get(0), stockData);
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		else if(!stockList.get(position).equals(getString(R.string.none)))  {		
			
			Intent i = new Intent(financeStock.this, financeStockDetail.class);		
			this.stockData = dataMap.get(stockList.get(position));
			i.putExtra("name", stockList.get(position));
			i.putExtra("data", this.stockData);
			startActivity(i);	
		}		
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.stockName = stockList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeStock");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject stock : postList) {
				
				stock.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(Map.Entry<String, String[]> entry : dataMap.entrySet()){
			
			if(entry.getKey().equals(getString(R.string.none)))
				continue;
			 
			ParseObject financeStock = new ParseObject("financeStock");
			financeStock.put("Owner", ParseUser.getCurrentUser());
			financeStock.put("stockName", entry.getKey());
			financeStock.put("stockPrice", entry.getValue()[0]);
			financeStock.put("stockAmount", entry.getValue()[1]);
			
			try {
				
				financeStock.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}

