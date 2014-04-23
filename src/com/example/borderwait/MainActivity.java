package com.example.borderwait;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;

public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, OnMapLongClickListener {
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	Boolean isConnected;
	GoogleMap map;
	LatLng myLatLng;
	Location myLocation;
	LocationClient lcMyLocation;
	
	//Variables to hold references to objects on the page
	EditText etEmail, etPassword;
	CheckBox cbxRemember;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Start with Login fragment
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.userInfoContainer, new LoginFragment()).commit();
		}
		
		//Get reference to each object on bottom fragment
		etEmail = (EditText)findViewById(R.id.etEmail);
		etPassword = (EditText)findViewById(R.id.etPassword);
		cbxRemember = (CheckBox)findViewById(R.id.cbxRemember);
		
		//Get location services ready to go
		lcMyLocation = new LocationClient(this, this, this);
		
		//Test Connection first
		isConnected = TestConnection();
		
		//Check if connection to Internet succeeded
		if (isConnected)
		{	
			//Get parse ready to go
			Parse.initialize(this, "4rMpAqbh9hdSyIRcvefxtInZtaPz3Ef7K8tGaEWS", "MXxegBkLTfCyM4kSHBlgQSj6y54ZaFtdjmV20SV2");
			
			//Get reference to the map
			map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			//Set click listener for the map
			map.setOnMapLongClickListener(this);
		}
		else
		{
			Toast.makeText(this, "You are not connected!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean TestConnection() {
		boolean conStatus;
		
		//Lets fire the connectivity checker
		conStatus = isOnline();
		
		//Return whether connected or not
		return conStatus;
	}
	
	public boolean isOnline() {
		//Create the connectivity manager, this object 
	    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    //Create network info object to hold current network status
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    //If there is active network info and the phone is Connected or Connecting then a network is present
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    
	    //If those weren't present, no network is available
	    return false;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
		}
		
	}

	@Override
	public void onConnected(Bundle dataBundle) {
        //Get the location
      	myLocation = lcMyLocation.getLastLocation();
		
      	//Add current location to map
		map.setMyLocationEnabled(true);
		
		//Lets get our location as Latitude and Longitude
		myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
		
		//Make the map automatically go there
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 5.0f));
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
        Toast.makeText(this, "LocationService Disconnected, please reconnect",
                Toast.LENGTH_SHORT).show();
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        lcMyLocation.connect();
    }

	
	@Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        lcMyLocation.disconnect();
        super.onStop();
    }
	
	@Override    
	public void onMapLongClick(LatLng point) {
	    map.addMarker(new MarkerOptions()
	        .position(point)
	        .title("You are here")           
	        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));  
	}
	
	//Method to sign user in to access saved markers
	public void signIn(View v)
	{
		//Check if user input an email or password
		if (!(etEmail.getText().toString().length() == 0 && etPassword.getText().toString().length() == 0))
		{
			//User input something in both fields, check DB for user
			
			
			//Replace the fragment thats already there
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.userInfoContainer, new LoggedinFragment()).commit();
		}
		else
		{
			//User did not input something, check both fields
			if (etEmail.getText().toString().length() > 0)
			{
				//Email has data in it, password must not have any data
				Toast.makeText(this, "You must enter a password",
		                Toast.LENGTH_LONG).show();
			}
			else
			{
				//Email is empty might as well tell them to fix it regardless if password is filled or not
				Toast.makeText(this, "You must enter an email address",
		                Toast.LENGTH_LONG).show();
			}
		}
	}
	
	//Method to allow user to sign up
	public void signUp(View v)
	{
		
	}
}
