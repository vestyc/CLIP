package com.example.clip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;




public class MainActivity extends Activity {

	EditText userName;
	EditText passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		userName = (EditText)findViewById(R.id.userName);
		passWord = (EditText)findViewById(R.id.passWord);

	}

	
	public void LoginButton(View v) {
		// add-write text into file
		try {
			FileOutputStream fileout=openFileOutput(userName.getText().toString()+".txt", MODE_PRIVATE);
			OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
			outputWriter.write(userName.getText().toString());
			outputWriter.write("\n");
			outputWriter.write(passWord.getText().toString());
			outputWriter.close();
			
			//display file saved message
			Toast.makeText(getBaseContext(), "File saved successfully!",
			Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void ReadBtn(View v) {
		//reading text from file
		try {
			FileInputStream fileIn=openFileInput(userName.getText().toString()+".txt");
			InputStreamReader InputRead= new InputStreamReader(fileIn);
			
			char[] inputBuffer= new char[100];
			String s="";
			int charRead;
			
			while ((charRead=InputRead.read(inputBuffer))>0) {
				// char to string conversion
				String readstring=String.copyValueOf(inputBuffer,0,charRead);
				s +=readstring;					
			}
			InputRead.close();
			Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
}
