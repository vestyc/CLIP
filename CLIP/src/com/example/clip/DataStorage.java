package com.example.clip;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.File;
import android.content.Context;
import android.content.ContextWrapper;
import android.app.Application;
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
import java.io.FileNotFoundException;
import java.io.IOException;

public final class DataStorage extends ContextWrapper
{
	File fileCheck;
	String userName;
	String passWord;
	FileInputStream fileIn;
	InputStreamReader InputRead;
	BufferedReader reader;
	public DataStorage(Context context)
	{
		super(context);
	}
	
	public boolean checkUserName(String userName) 
	{
		fileCheck = getBaseContext().getFileStreamPath(userName+".txt");
		if(fileCheck.exists())
		{
			this.userName = userName;
			return true;
			
		}
		return false;
	}
	
	public String getPassword(String myUserName)
	{
		
		try {
			reader = null;
			fileIn= new FileInputStream(getBaseContext().getFileStreamPath(myUserName+".txt"));
			reader = new BufferedReader(new InputStreamReader(fileIn));
			passWord = reader.readLine();

			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
          
        } finally {
        	try{
			reader.close();
			fileIn.close();
        	} catch (IOException ex) {
        		ex.printStackTrace();
        	}
        }
		return passWord;
	}
	

}
