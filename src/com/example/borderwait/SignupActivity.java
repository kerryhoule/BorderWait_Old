package com.example.borderwait;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class SignupActivity extends Activity {
	
	//Variables to hold references to objects on the page
	EditText etName, etEmail, etPassword, etConfirm;
	Button btnOk, btnClear, btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		//Get parse ready to go
		Parse.initialize(this, "4rMpAqbh9hdSyIRcvefxtInZtaPz3Ef7K8tGaEWS", "MXxegBkLTfCyM4kSHBlgQSj6y54ZaFtdjmV20SV2");
		
		//Get references to all the edit texts and buttons
		etName = (EditText)findViewById(R.id.etSignupName);
		etEmail = (EditText)findViewById(R.id.etSignupEmail);
		etPassword = (EditText)findViewById(R.id.etSignupPass);
		etConfirm = (EditText)findViewById(R.id.etSignupConfirm);
		btnOk = (Button)findViewById(R.id.btnSignupOk);
		btnClear = (Button)findViewById(R.id.btnSignupClear);
		btnCancel = (Button)findViewById(R.id.btnSignupCancel);
		
		//Build listeners for buttons
		btnOk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Check if all fields have data in them, if not show error for them
				if (etName.getText().toString().length() > 0 && etEmail.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0 && etConfirm.getText().toString().length() > 0)
				{
					//Check that passwords match
					if (etPassword.getText().toString().equals(etConfirm.getText().toString()))
					{
						//All fields have data entered, make a user!
						ParseObject user = new ParseObject("User");
						
						//Fill out the information
						user.put("name", etName.getText().toString());
						user.put("email", etEmail.getText().toString());
						user.put("password", etPassword.getText().toString());
						
						//Save the user to the DB
						user.saveInBackground();
					}
					else
					{
						//Show error with passwords
						Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					//Some fields weren't filled out
					Toast.makeText(SignupActivity.this, "Something wasnt filled out, will find out after", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btnClear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Clear all fields
				etName.setText("");
				etEmail.setText("");
				etPassword.setText("");
				etConfirm.setText("");
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
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
