package com.example.borderwait;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoginFragment extends Fragment {

	//Variables to hold references to objects on the page
	EditText etEmail, etPassword;
	CheckBox cbxRemember;
	Button btnSignUp, btnSignIn;
	Boolean isValidUser = false;
	
	//Setup shared preferences
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	public static final String PREFS = "BWPrefs";
	
	//Variables for storing user data
	String name, email, password;
	
	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		
		//Get shared preferences
		settings = ((MainActivity)getActivity()).getSharedPreferences(PREFS, 0);
		editor = settings.edit();
		
		//Get reference to each object on bottom fragment
		etEmail = (EditText)rootView.findViewById(R.id.etEmail);
		etPassword = (EditText)rootView.findViewById(R.id.etPassword);
		cbxRemember = (CheckBox)rootView.findViewById(R.id.cbxRemember);
		btnSignUp = (Button)rootView.findViewById(R.id.btnsignup);
		btnSignIn = (Button)rootView.findViewById(R.id.btnSignin);
		
		//Setup Listeners
		//Sign In
		btnSignIn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Check if user input an email or password
				if (etEmail.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0)
				{
					Boolean isRemembered = settings.getBoolean("remembered", false);
					//Check if user is remembered from before
					if (isRemembered)
					{
						//User is valid already
						isValidUser = true;
						
						//Fill out userdata variables
		        		email = settings.getString("email", "");
		        		name = settings.getString("name", "");
		        		password = settings.getString("password", "");
					}
					else
					{
						//User is not remembered, look him up
						//User input something in both fields, check DB for user
						ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
						
						//Lets see if this user exists
						query.whereEqualTo("email", etEmail.getText().toString());
						try 
						{
							//Get a list of found users
							List<ParseObject> userList = query.find();
							
							//Check if user found
							if (userList.size() > 0) 
							{
					            //Found user, now we gotta check password
					        	if (etPassword.getText().toString().equals(userList.get(0).getString("password")))
					        	{
					        		//Password matches, proceed
					        		isValidUser = true;
					        		//Does he want us to remember him?
						        	if (cbxRemember.isChecked())
						        	{
						        		//User wants us to remember him for later
						        		editor.putBoolean("remembered", true);
						        		editor.putString("email", userList.get(0).getString("email"));
						        		editor.putString("name", userList.get(0).getString("name"));
						        		editor.putString("password", userList.get(0).getString("password"));
						        	}
						        	else
						        	{
						        		//User doesn't want to be remembered, remove the shared prefs
						        		editor.clear();
						        	}
						        	
						        	//Commit the preferences
					        		editor.commit();
					        		
					        		//Fill out userdata variables
					        		email = userList.get(0).getString("email");
					        		name = userList.get(0).getString("name");
					        		password = userList.get(0).getString("password");
					        	}
							}
						} 
						catch (ParseException e) 
						{
							//User wasnt found, set boolean
							isValidUser = false;
						}
					}
				    
				    //If user is found, log him in
				    if (isValidUser)
				    {
				    	//Send data back to activity for use in other fragment
				    	((MainActivity)getActivity()).setUserData(name, email, password);
				    	
				    	//Replace the fragment thats already there
						getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.userInfoContainer, new LoggedinFragment()).commit();
				    }
				    else
				    {
				    	//Didn't find user, show an error
			            Toast.makeText(getActivity(), "No user found or incorrect password.",
				                Toast.LENGTH_LONG).show();
				    }
				}
				else
				{
					//User did not input something, check both fields
					if (etEmail.getText().toString().length() > 0)
					{
						//Email has data in it, password must not have any data
						Toast.makeText(getActivity(), "You must enter a password",
				                Toast.LENGTH_LONG).show();
					}
					else
					{
						//Email is empty might as well tell them to fix it regardless if password is filled or not
						Toast.makeText(getActivity(), "You must enter an email address",
				                Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		//Sign Up
		btnSignUp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Call method to switch activities
				((MainActivity)getActivity()).signUp();
			}
		});
		
		return rootView;
	}
}