package com.example.borderwait;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
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
	
	//Variables to hold for logging in user
	String email, name, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Start with Login fragment
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.userInfoContainer, new LoginFragment()).commit();
		}
		
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
	
	//Method to switch activities
	public void signUp()
	{
		//Go to sign up activity
		Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
	}
	
	//Method to set userdata between fragment changes
	public void setUserData(String passedName, String passedEmail, String passedPassword)
	{
		//Set the user data
		name = passedName;
		email = passedEmail;
		password = passedPassword;
	}
	
	//Method to grab userdata between fragment changes
	public String[] getUserData()
	{
		//Make a string array
		String[] userData = {name, email, password};
		
		//Return that array
		return userData;
	}
}
