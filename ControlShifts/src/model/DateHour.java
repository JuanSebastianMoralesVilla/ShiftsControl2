package model;

import java.util.Calendar;

public class DateHour {

	private long time;
	
	public DateHour(long time) {
		this.time=time;
	}
	
	
	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public void showDate() {
		Calendar systemDate= Calendar.getInstance();
		systemDate.setTimeInMillis(Calendar.getInstance().getTimeInMillis()-time);
		System.out.println(systemDate.getTime().toString()); 
	}
}
