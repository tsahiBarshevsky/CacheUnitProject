package com.hit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.serverstatus.ServerStatus;
import com.hit.serverstatus.StatObj;
import com.hit.services.CacheUnitController;
import com.hit.util.CLI;

public class Server extends Object implements Observer {

	ServerSocket server;
	HandleRequest<String> handle;
	CacheUnitController<String> controller;
	Socket socket;
	Thread client;
	
	List<Thread> clientList;
	
	ServerStatus serverStatus;
	StatObj statObj;
	
	Long maxUsers;
	
	
	public Server() {
		clientList = new ArrayList<Thread>();
		
		statObj = new StatObj();
		
		maxUsers = new Long(0);
	}
	
	public void start()	{
		try {
			server = new ServerSocket(12345);
			do {
				server.setSoTimeout(0);
				
				socket = server.accept();
				
				
				controller = new CacheUnitController<String>();
				handle  = new HandleRequest<String>(socket, controller, statObj);
				
				client = new Thread(handle);
				client.start();
				clientList.add(client);
				if (maxUsers <= clientList.size()) {
					maxUsers = new Long(clientList.size());
				}
				
			} while (socket != null && !socket.isClosed());
		}
		catch (Exception e) {
			  System.out.println("tiered of waiting for connection " + e);
		}
		
		finally {
			try {
				if(server != null && !server.isClosed()) {
					server.close();
				}
				if(socket != null && !socket.isClosed()) {
					socket.close();
			
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void update(Observable o, Object arg) {
	
		if(o instanceof CLI) {
			
			if(arg == "start") {
				start();
			}
			else if(arg == "stop") {
				try {
					int size = clientList.size();
					for (int i=0;i<size;i++) {
						client = clientList.get(i);
						if(client.isAlive()) {
							client.interrupt();
						}
					}
					if(socket != null && !socket.isClosed()) {
						socket.close();
					}
					if(server != null && !server.isClosed()) {
						server.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(arg == "stat") {
				statObj.setMaxUsers(maxUsers);
				serverStatus = new ServerStatus(statObj);
				serverStatus.get();
			}
		}
	}
}
