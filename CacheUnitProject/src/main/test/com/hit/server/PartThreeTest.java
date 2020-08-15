package com.hit.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import com.hit.dm.DataModel;


public class PartThreeTest extends Observable implements Runnable {
	
	int port;
	
	public PartThreeTest() {
		port = 12345;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
            String serverHostname = new String("127.0.0.1");
 
            System.out.println("Connecting to host " + serverHostname + " on port " + port + ".");
 
            Socket echoSocket = null;
            ObjectOutputStream outC = null;
            ObjectInputStream inC = null;
            
 
            try {
                echoSocket = new Socket(serverHostname, port);
                outC = new ObjectOutputStream(echoSocket.getOutputStream());
                inC = new ObjectInputStream(echoSocket.getInputStream());
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + serverHostname);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Unable to get streams from server");
                System.exit(1);
            }
 
        	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

 
            while (true) {            	
                System.out.print("client: ");
                String userInput = stdIn.readLine();
                if ("q".equals(userInput)) {
                	outC.writeObject(" ");
                    outC.flush();
                	echoSocket.close();
                	setChanged();
                    notifyObservers("stop");
                    break;
                }
                outC.writeObject(userInput);
                outC.flush();
               
                try {
        			TimeUnit.SECONDS.sleep(1);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                
                Object o;
                o = inC.readObject();
                
                if(o instanceof Boolean) {
                	if((boolean)o == true) {
                		System.out.println("good");
                	}
                	else {
                		System.out.println("bad");
                	}
                }
                
                else {
                	Request<DataModel<String>[]> request;
                	DataModel<String>[] body;
                
                	request = (Request<DataModel<String>[]>) o;
                	body = request.getBody();
                
                	int size = body.length;
                	for (int i=0; i< size; i++) {
                		System.out.println("body: " + body[i].toString());
                	}
                }
            }
 
            outC.close();
            inC.close();
            stdIn.close();           
        }
		catch (Exception e) {
            e.printStackTrace();
        }		
	}
}
