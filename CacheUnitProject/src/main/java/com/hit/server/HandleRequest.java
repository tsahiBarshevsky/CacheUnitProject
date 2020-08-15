package com.hit.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hit.authentication.AuthManeger;
import com.hit.dm.DataModel;
import com.hit.serverstatus.StatObj;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> extends Object implements Runnable {
	
	Socket s;
	CacheUnitController<T> controller;
	
	Type ref;
	
	Map<String, String> headers;
	DataModel<T>[] body;
	
	ObjectInputStream input;
	ObjectOutputStream output;
	
	String req;	
	
	AuthManeger auth;
	String sessionID;
	String action;
	boolean valid;	
	Map<String, String> userID;
	
	StatObj statObj;
	Integer noOfDM;
	
	public HandleRequest(Socket s, CacheUnitController<T> controller, StatObj statObj) {
		this.s = s;
		this.controller = controller;
		ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
		auth = new AuthManeger();

		userID = new HashMap<String, String>();
		this.statObj = statObj;
		this.statObj.setCurrentUsers(new Long(1));
		noOfDM = new Integer(0);
	}
	
	public void run() {
		try {
			input=new ObjectInputStream(s.getInputStream());
			output=new ObjectOutputStream(s.getOutputStream());
			
			output.writeObject("you are connected to the server");
			output.flush();
			
			while(!s.isClosed()) {
				
				req = (String)input.readObject();
				if(req.equals("stop")) {
					statObj.setCurrentUsers(new Long(-1));
					statObj.setAvarageDatamodelID(noOfDM);
					userID.put("userId", UUID.randomUUID().toString());
					userID.put("Requests", noOfDM.toString());
					statObj.setUserID(userID);
					break;
				}
					
				Request<DataModel<T>[]> request = null;
				try {
					request = new Gson().fromJson(req, ref);
				}
				catch(JsonSyntaxException e) {
					output.writeObject("json error");
					output.flush();	
					request = null;
				}
			
				if(request != null) {				
					headers = request.getHeaders();
					body = request.getBody();
					
					valid = false;
					sessionID = headers.get("sessionId");
					if (sessionID != null) {
						valid = auth.validateSession(sessionID);
					}
					
					action = headers.get("action");
					if (action == null) {
						action = "";
					}
					
					if(body != null) {
						noOfDM += body.length;
					}
								
					if(action.equals("UPDATE") && valid == true) {
						Boolean ret = new Boolean(controller.update(body));						
						output.writeObject(ret);
						output.flush();
					}
			
					else if(action.equals("GET") && valid == true) {
						body = controller.get(body);
						request.setBody(body);
						
						if(body == null) {
							output.writeObject("get error");
							output.flush();	
						}
						else {
							output.writeObject("array");
							int size = body.length;
							output.writeObject(new Integer(size));
							for(int i=0;i<size;i++) {
								output.writeObject(body[i].toString());
							}
							output.flush();
						}
					}
			
					else if(action.equals("DELETE") && valid == true) {
						Boolean ret = new Boolean(controller.delete(body));
						output.writeObject(ret);
						output.flush();					
					}
					
					else if(action.equals("LOGIN")) {
						sessionID = auth.loginProcess(headers.get("username") + "_" + headers.get("password"));
						if(sessionID != null) {
							output.writeObject(sessionID);
						}
						else {
							output.writeObject("login error");
						}
						output.flush();	
					}
					
					else {
						output.writeObject("request error");
						output.flush();	
					}
				}
			}
		}
		catch(EOFException e) {
		//	e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally{ 
			try {
				input.close();
				output.close();
				if(s != null && !s.isClosed()) {
					s.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
