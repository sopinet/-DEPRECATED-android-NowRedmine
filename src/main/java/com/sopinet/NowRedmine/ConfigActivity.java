package com.sopinet.NowRedmine;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.sopinet.NowRedmine.helpers.MyPrefs_;

@EActivity(R.layout.activity_config)
public class ConfigActivity extends ActionBarActivity {
	@ViewById
	EditText redmine_server;
	
	@ViewById
	EditText redmine_key;
	
	@Pref
	MyPrefs_ myPreferences;
	
	@AfterViews
	void init() {
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setHomeButtonEnabled(true);
    	
    	redmine_server.setText(myPreferences.server().get());
    	redmine_key.setText(myPreferences.key().get());
	}
	
	@Click
	void save() {
		myPreferences.server().put(redmine_server.getText().toString());
		myPreferences.key().put(redmine_key.getText().toString());
		
	   	Intent intent = new Intent(ConfigActivity.this, MyListActivity_.class); 
	    startActivity(intent);		
	}
}