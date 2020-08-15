package com.hit.authentication;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class AuthManeger {
	
	Map<String, String> users;
	
	String filePath;
	File file;
	
	Scanner scan;
	String line;
	
	String currentSession;
	
	Type ref;
	
	public AuthManeger() {
		filePath = "src/main/resource/users.dat";
		file = new File(filePath);
		
		ref = new TypeToken<Map<String, String>>(){}.getType();
		
		try {
			scan = new Scanner(file);
			while(scan.hasNext()) {
				line = scan.nextLine().toString();
				try {
					users = new Gson().fromJson(line, ref);
				}
				catch(JsonSyntaxException e) {
					users = null;
					System.out.println("users load error");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		scan.close();
		
		currentSession = UUID.randomUUID().toString();
	}

	public String loginProcess(String userAndPassword) {
		
		String userName;
		String passWord;
		String check;
		
		int size = userAndPassword.length();
		int mark = 0;
		for (int i=0;i<size;i++) {
			if(userAndPassword.charAt(i) == '_') {
				mark = i;
			}
		}
		
		userName = userAndPassword.substring(0, mark);
		passWord = userAndPassword.substring(mark +1, size);
		
		check = users.get(userName);
		if (check == null) {
			return null;
		}
		
		if(users.get(userName).equals(passWord)) {
			return currentSession + "_" + new Long(System.currentTimeMillis()).toString();
		}
		
		return null;
	}
	
	public boolean validateSession(String session) {
		
		if(session == null) {
			return false;
		}
		
		String sessionTime;
		String sessionName;
		long st=0;
		
		int size = session.length();
		int mark = 0;
		for (int i=0;i<size;i++) {
			if(session.charAt(i) == '_') {
				mark = i;
			}
		}
		
		sessionName = session.substring(0, mark);
		sessionTime = session.substring(mark +1, size);
		size = sessionTime.length();
		for(int i=0;i<size;i++) {
			st*=10;
			st+=sessionTime.charAt(i) - 48;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		
		if(st - calendar.getTimeInMillis() > 0 && currentSession.equals(sessionName)) {
			return true;
		}
		
		return false;
	}
}