package com.example.clip;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Login extends Activity {

	EditText userName;
	EditText passWord;
	File fileCheck;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
		userName = (EditText)findViewById(R.id.userName);
		passWord = (EditText)findViewById(R.id.passWord);
		userName.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		passWord.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

	}

	
	public void LoginButton(View v) 
	{
		String usernametxt = userName.getText().toString();
		String passwordtxt = passWord.getText().toString();

		// Send data to Parse.com for verification
		ParseUser.logInInBackground(usernametxt, passwordtxt,
				new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							// If user exist and authenticated, send user to Welcome.class
							Intent intent = new Intent(
									Login.this,
									Entry.class);
							startActivity(intent);
							Toast.makeText(getApplicationContext(),
									"Successfully Logged in",
									Toast.LENGTH_LONG).show();
							finish();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"No such user exist, please signup",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		// add-write text into file
		
	}
	
	public void Register(View v)
	{
		String usernametxt = userName.getText().toString();
		String passwordtxt = passWord.getText().toString();
		
		// Force user to fill up the form
		if (usernametxt.equals("") && passwordtxt.equals("")) {
			Toast.makeText(getApplicationContext(),
					"Please complete the sign up form",
					Toast.LENGTH_LONG).show();

		} else {
			// Save new user data into Parse.com Data Storage
			ParseUser user = new ParseUser();
			user.setUsername(usernametxt);
			user.setPassword(passwordtxt);
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						// Show a simple Toast message upon successful registration
						Toast.makeText(getApplicationContext(),
								"Successfully Signed up, please log in.",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Sign up Error", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
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
