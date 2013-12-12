package com.sopinet.NowRedmine.helpers;

import com.sopinet.utils.data.ImageTitleData;

// http://redmine.sopinet.es:3000/issues.json?
// key=e50a4ae3329274b8fdb9af251215fb7bf43001e8
// &assigned_to_id=me&sort=priority:desc

public class Issue extends ImageTitleData {
	public String id = "-";
	public IdName project;
	public IdName tracker;
	public IdName status;
	public IdName priority;
	public String priority_level = "0";
	public IdName author;
	public IdName assigned_to;
	public IdName category;
	public String subject = "-";
	public String description = "-";
	public String start_date = "-";
	public String done_ratio = "-";
	public String estimated_hours = "-";
	public String created_on = "-";
	public String updated_on = "-";
	
	@Override
	public String getTitle() {
		return this.subject;
	}
	
	@Override
	public String getSubtitle() {
		return this.created_on;
	}

	@Override
	public String getImage() {
		/*
		 * Ordenar Priority no tiene id ;)
		http://dummyimage.com/
		http://dummyimage.com/250/F00/F00
		return null;
		*/
		String coloradd = this.priority_level + this.priority_level;
		return "http://dummyimage.com/250/F"+coloradd+"/F"+coloradd;
	}	
} 

