package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

import server.ObelixInterface;
import base.Athlete;
import base.EventCategories;
import base.NationCategories;
import base.Results;
import base.Tally;

public class Tablet implements TabletInterface, Runnable {

    public Tablet() {}
    
    public Tablet(ObelixInterface obelixStub) {
    	this.obelixStub = obelixStub;
    }
    
    private static String SERVER_NAME = "Obelix";
    private static String CLIENT_BASE_NAME = "Client_";
    
    private ObelixInterface obelixStub;
    private String clientID;

    public static void main(String[] args) {
    	String obelixHost = (args.length < 1) ? null : args[0];
    	String tabletHost = (args.length < 2) ? null : args[1];
    	Tablet tabletInstance = getTabletInstance(obelixHost, tabletHost);
    	tabletInstance.menuLoop();
    }
    
    private void menuLoop() {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
    	try {
	    	while(true) {
	    		System.out.println("1. Get final results.\n2. Get medal tally.\n3. Get current score.\n4. Subscribe to updates.");
	    		int choice = Integer.parseInt(reader.readLine());
	    		switch(choice) {
	    			case 1: this.getResults().printResults(); break;
	    			case 2: this.getMedalTally().printMedalTally(); break;
	    			case 3: this.getCurrentScore();break;
	    			case 4: System.out.println("Coming soon..."); break;
	    			default: System.out.println("Not a valid choice");
	    		}
	    	}
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    @Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
    
    private static Tablet getTabletInstance(String obelixHost, String tabletHost) {

    	ObelixInterface obelixStub = connectToObelix(obelixHost);
    	Tablet tabletInstance = new Tablet(obelixStub);
    	tabletInstance.setupTabletServer(tabletHost);
    	    	
    	return tabletInstance;
    }

    private static ObelixInterface connectToObelix(String obelixHost) {
    	
		Registry registry = null;
		ObelixInterface obelixStub = null;
		
		try {
			registry = LocateRegistry.getRegistry(obelixHost);
	        obelixStub = (ObelixInterface) registry.lookup(SERVER_NAME);
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
            	
    	return obelixStub;
    }
    
    private void setupTabletServer(String host) {
    	
    	Registry registry = null;
		this.clientID = CLIENT_BASE_NAME + UUID.randomUUID().toString();
		TabletInterface tabletStub = null;
        
        try {
        	tabletStub = (TabletInterface) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.getRegistry(host);
            registry.rebind(clientID, tabletStub);
            System.err.println("Tablet ready.");         
        } catch (RemoteException e) {
            System.err.println("Tablet server exception: " + e.toString());
            e.printStackTrace();
        }       
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
    
    private Tally getMedalTally() throws RemoteException {
    	String teamName = getInput("Team name");
    	return this.obelixStub.getMedalTally(NationCategories.valueOf(teamName.toUpperCase()));
    }
    
    private Results getResults() throws RemoteException {
    	String eventName = getInput("Event name");
    	return this.obelixStub.getResults(EventCategories.valueOf(eventName.toUpperCase()));
    }
    
    private void getCurrentScore() throws RemoteException {
    	String eventName = getInput("Event Name");
    	System.out.printf("Scores for %s.\n", eventName);
    	List<Athlete> scores = this.obelixStub.getCurrentScores(EventCategories.valueOf(eventName.toUpperCase()));
    	for(Athlete athlete : scores)
    	{
    		athlete.printScore();
    	}
    }

	@Override
	public void updateScores(List<Athlete> scores) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateResults(Results result) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
    
}