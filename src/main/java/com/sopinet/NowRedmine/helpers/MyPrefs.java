package com.sopinet.NowRedmine.helpers;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface MyPrefs {
	@DefaultString("")
	String key();

	@DefaultString("")
	String server();
	
	@DefaultString("")
	String me();

    // The field lastUpdated will have default value 0
	long lastUpdated();

}


