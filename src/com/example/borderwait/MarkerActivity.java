package com.example.borderwait;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MarkerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marker);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.marker, menu);
		return true;
	}

}
