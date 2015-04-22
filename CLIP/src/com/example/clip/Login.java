package com.example.clip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;


public class Login extends Activity {

	EditText userName;
	EditText passWord;
	File fileCheck;
	DataStorage myData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		userName = (EditText)findViewById(R.id.userName);
		passWord = (EditText)findViewById(R.id.passWord);
		userName.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		passWord.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		myData = new DataStorage(getApplicationContext());
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();

	}

	
	public void LoginButton(View v) 
	{
		// add-write text into file
		
		try 
		{
			if(myData.checkUserName(userName.getText().toString()))
			{
				if(myData.getPassword(userName.getText().toString()).equals(passWord.getText().toString()))
				{
					Toast.makeText(getBaseContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(this, Entry.class);
					startActivity(i);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getBaseContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
			}	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
	}
	
	public void Register(View v)
	{
		
		try 
		{
			FileOutputStream fileout=openFileOutput(userName.getText().toString()+".txt", MODE_PRIVATE);
			OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
			outputWriter.write(passWord.getText().toString());
			outputWriter.write("\n");
			outputWriter.close();
			
			//display file saved message
			Toast.makeText(getBaseContext(), "Registered Successfully",
			Toast.LENGTH_SHORT).show();
			
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			Intent i = new Intent(this, Entry.class);
			startActivity(i);
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
