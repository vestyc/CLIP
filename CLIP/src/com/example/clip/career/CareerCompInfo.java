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

public class CareerCompInfo extends ListActivity implements OnItemClickListener, OnItemLongClickListener {

	ArrayAdapter<String> popUpAdapter;
	ListPopupWindow popUp;
	ArrayList<String> popUpItems;
	
	ArrayAdapter<String> listViewAdapter;
	String companyName;
	ArrayList<String> companyList;
	HashMap<String, String[]> dataMap;		//<companyName, companyData>
	HashMap<String, int[][]> datesMap;		//<companyName, companyDates>
	
	//{0           1         2      3      4      5                    6                 7               }
	//{productLOB, location, phone, email, facts, considerationReason, interviewOutcome, interviewLessons}
	String[] companyData;
	
	//dateType = {resumeSubDate, interviewDate}
	//[dateType][month, day, year]
	int[][] companyDates; 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//initiate empty list
		this.createEmptyList();
		listViewAdapter = new ArrayAdapter<String>(this, R.layout.activity_career_comp_info, 
				R.id.label_compInfoList, companyList);
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
				dataMap.clear();
				datesMap.clear();
				companyList.clear();
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("careerCompInfo");
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
					for (ParseObject company : postList) {				
							
						companyName = company.getString("companyName");	
						
						//add data from database 
						companyList.add(companyName);
						
						//{0           1         2      3      4      5                    6                 7               }
						//{productLOB, location, phone, email, facts, considerationReason, interviewOutcome, interviewLessons}
						this.companyData = new String[8];		
						companyData[0] = company.getString("productLOB");
						companyData[1] = company.getString("location");
						companyData[2] = company.getString("phone");
						companyData[3] = company.getString("email");
						companyData[4] = company.getString("facts");
						companyData[5] = company.getString("considerationReason");
						companyData[6] = company.getString("interviewOutcome");
						companyData[7] = company.getString("interviewLessons");
						this.dataMap.put(companyName, companyData);
						
						//dateType = {resumeSubDate, interviewDate}
						//[dateType][month, day, year]
						this.companyDates = new int[2][3];	
						companyDates[0][0] = company.getInt("resumeSubMonth");
						companyDates[0][1] = company.getInt("resumeSubDay");
						companyDates[0][2] = company.getInt("resumeSubYear");
						
						companyDates[1][0] = company.getInt("interviewMonth");
						companyDates[1][1] = company.getInt("interviewDay");
						companyDates[1][2] = company.getInt("interviewYear");
						this.datesMap.put(companyName, companyDates);						
					}
					
				}catch (ParseException e) {
					
					Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
				}
				
				if(companyList.isEmpty()) {
					
					this.resetEmptyList();
				}
		
		this.updateScreen();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	//	try {
		
			//edit job
			if(requestCode == 1) {	
				
				//remove old data first
				this.companyName = data.getStringExtra("oldName");
				this.companyList.remove(companyName);
				this.dataMap.remove(companyName);
				this.datesMap.remove(companyName);
			}
			//add job
			else if(requestCode == 0) {
				
				//clears any initial data		
				companyList.remove(getString(R.string.none));
				dataMap.remove(getString(R.string.none));
				datesMap.remove(getString(R.string.none));
			}
			
			//add new data
			this.companyName = data.getStringExtra("name");
			this.companyData = data.getStringArrayExtra("data");
			this.companyDates[0] = data.getIntArrayExtra("resumeDate");
			this.companyDates[1] = data.getIntArrayExtra("interviewDate");
			
			//adding data to data maps
			this.companyList.add(this.companyName);
			this.dataMap.put(this.companyName, this.companyData);
			this.datesMap.put(this.companyName, this.companyDates);
			
			this.updateScreen();
			this.saveToCloud();
	/*	}
		catch(NullPointerException n) {
			//back button was pressed instead of save
			Toast.makeText(this.getApplicationContext(), "You didn't save!", Toast.LENGTH_LONG).show();
		}*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_comp_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			
			Intent i = new Intent(CareerCompInfo.this, CareerCompInfoEdit.class);
			startActivityForResult(i, 0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		
		if(popUp.isShowing()) {
			
			popUp.dismiss();
			
			//only operate when companyName != none
			if(!companyName.equals(getString(R.string.none))) {
			
				//edit is clicked 
				if(this.popUpItems.get(position).equals(getString(R.string.action_edit))) {
					
					Intent i = new Intent(CareerCompInfo.this, CareerCompInfoEdit.class);
					
					i.putExtra("name", companyName);
					i.putExtra("data", dataMap.get(companyName));
					
					companyDates = datesMap.get(companyName);
					i.putExtra("resumeDate", companyDates[0]);
					i.putExtra("interviewDate", companyDates[1]);
					
					startActivityForResult(i, 1);
				}
				//remove is clicked 
				else if(this.popUpItems.get(position).equals(getString(R.string.action_remove))) {
					
					companyList.remove(companyName);
					dataMap.remove(companyName);
					datesMap.remove(companyName);
					
					if(companyList.isEmpty()) {
						
						this.resetEmptyList();
					}
					
					this.updateScreen();
					this.saveToCloud();
				}
			}
		}
		//show details if company != none
		else if(!companyList.get(position).equals(getString(R.string.none))) {
			
			Intent i = new Intent(CareerCompInfo.this, CareerCompInfoDetail.class);
			
			i.putExtra("name", companyList.get(position));
			i.putExtra("data", dataMap.get(companyList.get(position)));
			
			//dates must be sent through bundle because it's a 2d array (int[][])
			Bundle datesExtra = new Bundle();
			datesExtra.putSerializable("dates", this.datesMap.get(companyList.get(position)));
			i.putExtra("dates", datesExtra);
			
			startActivity(i);
		}
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		
		companyName = companyList.get(position);
		popUp.setAnchorView(view);
		popUp.show();
		popUp.getListView().setOnItemClickListener(this); 
		return true;
	}
	
	private void createEmptyList() {
		
		companyList = new ArrayList<String>();
		companyList.add(getString(R.string.none));
		
		companyDates = new int[2][3];
		companyData = new String[8];
		
		dataMap = new HashMap<String, String[]>();		//<companyName, companyData>
		datesMap = new HashMap<String, int[][]>();		//<companyName, companyDates>
	}
	
	private void resetEmptyList() {
		
		companyList.add(getString(R.string.none));
	}
	
	private void updateScreen() {
		
		//update screen
		this.onContentChanged();
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
		popUp.setOnItemClickListener(this);
	}
	
	private void saveToCloud() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("careerCompInfo");
		query.whereEqualTo("Owner", ParseUser.getCurrentUser());
		
		try {
			
			List<ParseObject> postList = query.find();
		
			// If there are results, update the list of posts
			// and notify the adapter
			for (ParseObject company : postList) {
				
				company.delete();
			}
			
		}catch (ParseException e) {
			
			Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
		}		
		
		 for(String companyName : companyList){
			 
			if(companyName.equals(getString(R.string.none)))
				continue;
			 
			ParseObject careerCompInfo = new ParseObject("careerCompInfo");
			careerCompInfo.put("Owner", ParseUser.getCurrentUser());
			careerCompInfo.put("companyName", companyName);
			
			companyData = dataMap.get(companyName);
			careerCompInfo.put("productLOB", companyData[0]);
			careerCompInfo.put("location", companyData[1]);
			careerCompInfo.put("phone", companyData[2]);
			careerCompInfo.put("email", companyData[3]);
			careerCompInfo.put("facts", companyData[4]);
			careerCompInfo.put("considerationReason", companyData[5]);
			careerCompInfo.put("interviewOutcome", companyData[6]);
			careerCompInfo.put("interviewLessons", companyData[7]);
			
			companyDates = datesMap.get(companyName);
			if(companyDates != null) {
			
				if(companyDates[0] != null) {
					
					careerCompInfo.put("resumeSubMonth", companyDates[0][0]);
					careerCompInfo.put("resumeSubDay", companyDates[0][1]);
					careerCompInfo.put("resumeSubYear", companyDates[0][2]);
				}
				
				if(companyDates[1] != null) {
					
					careerCompInfo.put("interviewMonth", companyDates[1][0]);
					careerCompInfo.put("interviewDay", companyDates[1][1]);
					careerCompInfo.put("interviewYear", companyDates[1][2]);
				}
			}
			
			try {
				
				careerCompInfo.save();
				
			}catch (ParseException e) {
				
				Toast.makeText(this.getApplicationContext(), "query error!", Toast.LENGTH_LONG).show();
			}
		 }
	}
}
