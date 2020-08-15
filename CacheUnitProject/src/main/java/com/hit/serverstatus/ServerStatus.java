package com.hit.serverstatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;


public class ServerStatus {
	
	StatObj stat;
	
	File log;
	FileWriter fw;
	
	Type ref;

	public ServerStatus(StatObj stat) {
		
				
		log = new File("log.txt");
		
		ref = new TypeToken<StatObj>(){}.getType();

		this.stat = stat;
	}
	
	
	public void get() {
		JsonElement currentLog;
				
		currentLog = new Gson().toJsonTree(stat, ref);
		
		try {
			fw = new FileWriter(log, true);
			fw.write(currentLog.toString() + "\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
