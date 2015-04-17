package com.example.clip.career;

import com.example.clip.R;
import com.example.clip.R.id;
import com.example.clip.R.layout;
import com.example.clip.R.menu;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

public class CareerGoal extends ListActivity implements OnItemClickListener{

	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_career_goal);
		
		String[] goals = new String[] {"Internship", "Graduate",
				"CEO of Google", "Add Goal"};
		
		adapter = new ArrayAdapter<String>(this, R.layout.activity_career_goal,
				R.id.label, goals);
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.career_goal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // your code is here on item click
		Intent i = new Intent(CareerGoal.this, CareerGoalDetail.class);
		startActivity(i);		
    }
}
