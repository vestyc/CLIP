package com.example.clip.finance;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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


public class financeCreditCard extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> listViewAdapter, popUpAdapter;
	ArrayList<String> cardList, popUpItems;
	ListPopupWindow popUp;
	String cardName;
	
	HashMap<String, String[]> dataMap;	//<cardName, cardData>
	HashMap<String, int[]> dateAppMap;	//<cardName, dateApplied>
	String[] cardData;					//{amountOwed, minPayment}
	int[]	cardDateApplied;				//{Month, Day, Year}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getListView().setBackgroundColor(Color.GRAY);
		//initiate list view
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String> (this, R.layout.activity_finance_creditcard,
				R.id.label_cardList, cardList);
		this.setListAdapter(listViewAdapter);
		
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
		this.dateAppMap.clear();
		cardList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeCreditCard");
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
			for (ParseObject job : postList) {				
					
				cardName = job.getString("cardName");	
				
				//add data from database 
				cardList.add(cardName);
				
				cardData = new String[2];					//{amountOwed, minPayment}
				cardData[0] = job.getString("amountOwed");	
				cardData[1] = job.getString("minPayment");	
				dataMap.put(cardName, cardData);	
				
				cardDateApplied = new int[3];				//{Month, Day, Year}
				cardDateApplied[0] = job.getInt("month");
				cardDateApplied[1] = job.getInt("day");
				cardDateApplied[2] = job.getInt("year");
				this.dateAppMap.put(cardName, cardDateApplied);
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}
		
		if(cardList.isEmpty()) {
			
			this.resetEmptyList();
		}
		
		//update screen
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		try {
			
			//edit job
			if(requestCode == 1) {	
				
				//remove old data first
				this.cardName = data.getStringExtra("oldName");
				this.cardList.remove(cardName);
				this.dataMap.remove(cardName);
				this.dateAppMap.remove(cardName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				cardList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				dateAppMap.remove(getString(R.string.none));
			}
			
			//add new data
			this.cardName = data.getStringExtra("name");
			this.cardData = data.getStringArrayExtra("data");
			this.cardDateApplied = data.getIntArrayExtra("date");
			
			//adding data to data maps
			this.cardList.add(this.cardName);
			this.dataMap.put(this.cardName, this.cardData);
			this.dateAppMap.put(this.cardName, this.cardDateApplied);
			
			//updating screen, resetting listeners
			this.onContentChanged();
			getListView().setOnItemClickListener(this);
			popUp.setOnItemClickListener(this);
			this.saveToCloud();
		}
		catch(NullPointerException n) {
			//back button press on edit screen
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance_creditcard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(financeCreditCard.this, financeCreditCardEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when cardName != none
			if(!cardName.equals(getString(R.string.none))) {
			
				//edit is clicked
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
												
					Intent i = new Intent(financeCreditCard.this, financeCreditCardEdit.class);
					
					this.cardData = dataMap.get(cardList.get(position));
					this.cardDateApplied = this.dateAppMap.get(cardList.get(position));
					
					i.putExtra("name", cardList.get(position));
					i.putExtra("data", this.cardData);
					i.putExtra("date", this.cardDateApplied);
					
					this.startActivityForResult(i, 1);
				}
				//remove is clicked
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					cardList.remove(cardName);
					dataMap.remove(cardName);
					this.dateAppMap.remove(cardName);
					
					if(cardList.isEmpty()) {
					
						this.resetEmptyList();
					}
					this.onContentChanged();
					getListView().setOnItemClickListener(this);
					popUp.setOnItemClickListener(this);
					this.saveToCloud();
				}
			}
		}
		//show details if job != none
		else if(!cardList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(financeCreditCard.this, financeCreditCardDetail.class);		
			this.cardData = dataMap.get(cardList.get(position));
			this.cardDateApplied = this.dateAppMap.get(cardList.get(position));
			
			i.putExtra("name", cardList.get(position));
			i.putExtra("data", this.cardData);
			i.putExtra("date", this.cardDateApplied);
			
			startActivity(i);
		}
    }
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		this.cardName = cardList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		cardList = new ArrayList<String>();
		cardList.add(getString(R.string.none));
		dataMap = new HashMap<String, String[]>();
		dateAppMap = new HashMap<String, int[]>();
		/*
		cardData = new String[] {"Status N/A", "No minPayment."};
		dataMap = new HashMap<String, String[]>();
		dataMap.put(cardList.get(0), cardData);
		cardDateApplied = new int[3];
		dateAppMap = new HashMap<String, int[]>();
		dateAppMap.put(cardList.get(0), cardDateApplied);
		*/
	}
	
	private void resetEmptyList() {
		
		cardList.add(getString(R.string.none));
		/*
		cardData = new String[] {"Status N/A", "No minPayment."};
		dataMap = new HashMap<String, String[]>();
		dataMap.put(cardList.get(0), cardData);
		cardDateApplied = new int[3];
		dateAppMap = new HashMap<String, int[]>();
		dateAppMap.put(cardList.get(0), cardDateApplied);
		*/
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("financeCreditCard");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject job : postList) {
				
				job.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(String cardName : cardList){
			
			 if(cardName.equals(getString(R.string.none)))
				 continue;
			 
			ParseObject financeCreditCard = new ParseObject("financeCreditCard");
			financeCreditCard.put("Owner", ParseUser.getCurrentUser());
			financeCreditCard.put("cardName", cardName);
			
			cardData = dataMap.get(cardName);
			financeCreditCard.put("amountOwed", cardData[0]);
			financeCreditCard.put("minPayment", cardData[1]);
			
			this.cardDateApplied = this.dateAppMap.get(cardName);
			financeCreditCard.put("month", cardDateApplied[0]);
			financeCreditCard.put("day", cardDateApplied[1]);
			financeCreditCard.put("year", cardDateApplied[2]);
			
			try {
				
				financeCreditCard.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}
