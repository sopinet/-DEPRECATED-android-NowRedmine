package com.sopinet.NowRedmine;

import java.lang.reflect.Type;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.sopinet.NowRedmine.helpers.Issues;
import com.sopinet.NowRedmine.helpers.MyPrefs_;
import com.sopinet.NowRedmine.helpers.Var;
import com.sopinet.utils.SimpleContent;
import com.sopinet.utils.SimpleContent.ApiException;
import com.sopinet.utils.list.ImageTitleList;

@EActivity(R.layout.activity_mylist)
public class MyListActivity extends ActionBarActivity {
	@ViewById
	ListView lstIssues;
	
	private ProgressDialog pd;	
	private ImageTitleList adaptador;
	public static Issues issues;
	
	@Pref
	MyPrefs_ myPreferences;
	
	@AfterViews
	void init() {
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setHomeButtonEnabled(true);
    	
    	if (myPreferences.server().get().equals("")) {
    	   	Intent intent = new Intent(MyListActivity.this, ConfigActivity_.class); 
    	    startActivity(intent);	    		
    	}
    	
    	showWait();
    	load();
	}
	
	@UiThread
	void showWait() {
		this.pd = ProgressDialog.show(this, "Cargando", "Espere por favor...", true, false);		
	}
	
	@UiThread
	void showError() {
		pd.dismiss();
		// TODO: Un mensaje de error
	}
	
	@UiThread
	void showOk() {
		pd.dismiss();
		
    	// show content
        adaptador = new ImageTitleList(MyListActivity.this, issues.issues);
        lstIssues.setAdapter(adaptador);
        lstIssues.setTextFilterEnabled(true);
        
        // action in content
        lstIssues.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long iid) {
            	/*
        	   	Intent intent = new Intent(EntriesActivity.this, EntryActivity_.class); 
        	   	intent.putExtra("id", (int) iid);
        	   	intent.putExtra("babyid", id);
        	    startActivity(intent);
        	    */
            }
        });		
	}	
	
	@Background
	void load() {
    	// get content
    	SimpleContent sc = new SimpleContent(this, "nowredmine", 0);
    	/*
    	String data = "email="+myPreferences.user().get();
    	data += "&pass="+myPreferences.pass().getpreferences();
    	data += "&babyalias="+BabiesActivity.babies.data[id].alias;
    	*/
    	String URL_SERVER = myPreferences.server().get();
    	String KEY = myPreferences.key().get();
    	String get_issues = URL_SERVER + Var.URL_ISSUES;
    	String data = "key=" + KEY + Var.OPT_ISSUES;
    	
    	String jbody = null;
		try {
			jbody = sc.getUrlContent(get_issues, data);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		Log.d("TEMA", "BODY: "+get_issues);
		Log.d("TEMA", "BODY: "+data);
		Log.d("TEMA", "BODY: "+jbody);
		// parse content
    	final Type entriesCPD = new TypeToken<Issues>(){}.getType();
    	if (jbody.startsWith("<")) {
    		showError();
    	} else {
    		issues = new Gson().fromJson(jbody, entriesCPD);
    		
    		myPreferences.me().put(issues.issues[0].assigned_to.id);
    		
    		String priority_before = "0";
    		for(int i = 0; i < issues.issues.length; i++) {
    			if (!priority_before.equals(issues.issues[i].priority.id)) {
    				int temp = Integer.valueOf(priority_before);
    				temp++;
    				priority_before = String.valueOf(temp);
    			}
    			issues.issues[i].priority_level = priority_before;
    		}
    		
    		showOk();
    	}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);	    
	    
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
	    switch (item.getItemId()) {
		    case android.R.id.home:
		    	/*
        	   	intent = new Intent(EntryActivity.this, EntriesActivity_.class); 
        	   	intent.putExtra("id", babyid);
        	    startActivity(intent);
        	    */
		    return true;
		    case R.id.action_new:
        	   	intent = new Intent(MyListActivity.this, NewIssueActivity_.class); 
        	    startActivity(intent); 		    		
		    return true;
		    case R.id.action_settings:
        	   	intent = new Intent(MyListActivity.this, ConfigActivity_.class); 
        	    startActivity(intent);		    	
		    return true;
	    }

	    return super.onOptionsItemSelected(item);
	}	
	
	//public static Entries entries;	
}