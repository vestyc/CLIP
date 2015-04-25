package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.Intent;

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
		
		//listeners
		this.getListView().setOnItemClickListener(this);
		this.getListView().setOnItemLongClickListener(this);
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
					
					//dates must be sent through bundle because it's a 2d array (int[][])
					Bundle datesExtra = new Bundle();
					datesExtra.putSerializable("dates", this.datesMap.get(companyName));
					i.putExtra("dates", datesExtra);
					
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
					
					this.onContentChanged();
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
	}
	private void resetEmptyList() {
		
		companyList.add(getString(R.string.none));
	}
}
