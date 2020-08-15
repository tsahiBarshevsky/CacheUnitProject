package com.hit.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Scanner;

public class CLI extends Observable implements Runnable {

    String start;
    String shutdown;
    String stat;
    
    String userString;
    
    Scanner in;
    PrintWriter out;
        
    public CLI(InputStream in, OutputStream out) {
        this.out = new PrintWriter(out, true);
        this.in = new Scanner(in);
        
        start = "start";
        shutdown = "stop";
        stat = "stat";
        
        userString = "";
    }
    
    public void write(String string) {
        out.println(string);
    }
    
    public void run() {
        
        while (true) {
        	write("Please enter your command");
            userString = in.nextLine();
            if(userString.equals(start)) {
                write("Starting server.....");
                setChanged();
                notifyObservers(start);                
            }
            else if (userString.equals(shutdown)) {
                write("Shutdown server");
                setChanged();
                notifyObservers(shutdown);
                break;
            }
            else if (userString.equals(stat)) {
                write("server status");
                setChanged();
                notifyObservers(stat);
            }
            else {
                write("Not a valid command");
            }
        }
    }
}
