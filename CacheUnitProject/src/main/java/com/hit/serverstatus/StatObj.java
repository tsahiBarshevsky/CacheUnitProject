package com.hit.serverstatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatObj {
	String time;
	 
	Long currentUsers;
	Long maxUsers;
	Double avarageDatamodelID;
	Map<String, String>[] userID;
	
	@SuppressWarnings("unchecked")
	public StatObj() {
		avarageDatamodelID = new Double(0);
		currentUsers = new Long(0);
		List<Map<String,String>> tmp = new ArrayList<>();
		userID = tmp.toArray((Map<String,String>[]) new Map[0]);
	}
	
	public void setTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		time = timeFormat.format(now);
	}
	
	public String getTime(){
		
		return time;
	}
	
	public void setCurrentUsers(Long flag){
		currentUsers +=flag;
	}
	
	public Long getCurrentUsers(){
		
		return currentUsers;
	}
	public void setMaxUsers(Long max){
		maxUsers = max;
	}
	
	public Long getMaxUsers(){
		
		return maxUsers;
	}
	public void setAvarageDatamodelID(Integer avg){
		
		avarageDatamodelID = (avarageDatamodelID*userID.length+avg)/(userID.length+1);
	}
	
	public Double getAvarageDatamodelID(){
		
		return avarageDatamodelID;
	}
	@SuppressWarnings("unchecked")
	public void setUserID(Map<String, String> ids){
		List<Map<String, String>> users = new ArrayList<>();
		users.add(ids);		
	
		int size = users.size();
		userID = users.toArray((Map<String,String>[]) new Map[size]);
	}
	
	public Map<String, String>[] getUserID(){
		
		return userID;
	}
	
	
	public String toString(){
		
		return time + " " + currentUsers.toString() + " " + maxUsers.toString() + " " + avarageDatamodelID.toString() + " " + userID.toString();
	}
}
