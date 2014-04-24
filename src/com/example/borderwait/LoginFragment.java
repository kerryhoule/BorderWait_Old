package com.example.borderwait;

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

public class LoginFragment extends Fragment {

	//Variables to hold references to objects on the page
	EditText etEmail, etPassword;
	CheckBox cbxRemember;
	Button btnSignUp, btnSignIn;
	
	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		
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
					//User input something in both fields, check DB for user
					
					
					//Replace the fragment thats already there
					getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.userInfoContainer, new LoggedinFragment()).commit();
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