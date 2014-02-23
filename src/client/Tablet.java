package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ObelixInterface;
import base.EventCategories;
import base.NationCategories;
import base.Results;
import base.Tally;

public class Tablet {

    public Tablet() {}
    
    private static String HOST;
    private static String SERVER_NAME = "Obelix";

    public static void main(String[] args) {    	
    	HOST = (args.length < 1) ? null : args[0];
    	Tablet tabletInstance = new Tablet();
    	ObelixInterface stub = tabletInstance.connectToObelix();
    	tabletInstance.menuLoop(stub);
    }
    
    private void menuLoop(ObelixInterface stub) {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
    	try {
	    	while(true) {
	    		System.out.println("1. Get score.\n2. Get medal tally.\n3. Subscribe to updates.");
	    		int choice = Integer.parseInt(reader.readLine());
	    		switch(choice) {
	    			case 1: this.getScores(stub).printResults(); break;
	    			case 2: this.getMedalTally(stub).printMedalTally(); break;
	    			case 3: System.out.println("Coming soon..."); break;
	    			default: System.out.println("Not a valid choice");
	    		}
	    	}
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private ObelixInterface connectToObelix() {
    	
		Registry registry = null;
		ObelixInterface stub = null;
		
		try {
			registry = LocateRegistry.getRegistry(HOST);
	        stub = (ObelixInterface) registry.lookup(SERVER_NAME);
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
            	
    	return stub;
    }
    
    private String getInput(String msg) {
    	
    	System.out.println(msg + "?");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	String input = null;
    	
    	try {
    		input = reader.readLine();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return input;
    }
    
    private Tally getMedalTally(ObelixInterface stub) throws RemoteException {
    	String teamName = getInput("Team name");
    	return stub.getMedalTally(NationCategories.valueOf(teamName.toUpperCase()));
    }
    
    private Results getScores(ObelixInterface stub) throws RemoteException {
    	String eventName = getInput("Event name");
    	return stub.getScores(EventCategories.valueOf(eventName.toUpperCase()));
    }
}