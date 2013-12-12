package com.sopinet.NowRedmine;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.sopinet.NowRedmine.helpers.Issues;
import com.sopinet.NowRedmine.helpers.MyPrefs_;
import com.sopinet.NowRedmine.helpers.Projects;
import com.sopinet.NowRedmine.helpers.Var;
import com.sopinet.utils.SimpleContent;
import com.sopinet.utils.SimpleContent.ApiException;
import com.sopinet.utils.list.ImageTitleList;

@EActivity(R.layout.activity_newissue)
public class NewIssueActivity extends ActionBarActivity {
	@ViewById
	EditText issue_title;
	
	@ViewById
	Spinner issue_project;
	
	private ProgressDialog pd;
	public static Projects projects;
	
	@Pref
	MyPrefs_ myPreferences;
	
	@AfterViews
	void init() {
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setHomeButtonEnabled(true);
    	
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();    	
    	if (Intent.ACTION_SEND.equals(action) && type != null) {
    		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    	    if (sharedText != null) {
    	    	issue_title.setText(sharedText);
    	    }
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
	    List<String> SpinnerArray =  new ArrayList<String>();
	    for(int i = 0; i < projects.projects.length; i++) {
	    	SpinnerArray.add(projects.projects[i].name);
	    }

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinnerArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    issue_project.setAdapter(adapter);		
		
		pd.dismiss();
	}
	
	@UiThread
	void showsaveOk() {
		pd.dismiss();
		Toast.makeText(NewIssueActivity.this, "Creada con éxito", Toast.LENGTH_LONG).show();
		startActivity(new Intent(NewIssueActivity.this, MyListActivity_.class));
		// TODO: Go to MyListActivity_
	}
	
	@Click
	void save() {
		showWait();
		saveBG();
		showsaveOk();
	}
	
	@Background
	void saveBG() {
		String URL_SERVER = myPreferences.server().get();
		String KEY = myPreferences.key().get();
		String url = URL_SERVER + "/issues.json?key=" + KEY;
		
		JSONObject json = new JSONObject();
		JSONObject json_issue = new JSONObject();
		
		try {
			json_issue.put("project_id", projects.projects[(int) issue_project.getSelectedItemId()].id);
			json_issue.put("subject", issue_title.getText().toString());
			// TODO: Buscar mi ID (en el mylist.. puede que aparezca)
			json_issue.put("assigned_to_id", myPreferences.me().get());
			json.put("issue", json_issue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String string_json = json.toString();
		
		SimpleContent.nowPostJSON(url, string_json);
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
    	String get_projects = URL_SERVER + Var.URL_PROJECTS;
    	String data = "key=" + KEY + "&limit=100";
    	
    	String jbody = null;
		try {
			jbody = sc.getUrlContent(get_projects, data);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		// parse content
    	final Type entriesCPD = new TypeToken<Projects>(){}.getType();
    	if (jbody.startsWith("<")) {
    		showError();
    	} else {
    		projects = new Gson().fromJson(jbody, entriesCPD);
    		
    		showOk();
    	}
	}
	
	
}