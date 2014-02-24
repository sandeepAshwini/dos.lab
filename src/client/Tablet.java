package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import server.ObelixInterface;
import base.Athlete;
import base.EventCategories;
import base.NationCategories;
import base.Printable;
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
    	try {
	    	while(true) {
	    		String menuLine = String.format("1. Get final results.\n2. Get medal tally.\n3. Get current score.\n4. Subscribe to updates.");
	    		this.printToConsole(menuLine, null, null);
	    		int choice = Integer.parseInt(getInput("Enter choice."));
	    		switch(choice) {
	    			case 1: this.getResults(); break;
	    			case 2: this.getMedalTally(); break;
	    			case 3: this.getCurrentScore();break;
	    			case 4: this.printToConsole("Coming soon...", null, null); break;
	    			default: this.printToConsole("Not a valid menu option.", null, null);
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
    
    private synchronized String getInput(String msg) {
    	this.printToConsole( msg , null, null);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	String input = null;
    	
    	try {
    		input = reader.readLine();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	return input;
    }
    
    
    private void getResults() throws RemoteException {
    	String eventName = getInput("Event name");
    	EventCategories eventType = EventCategories.valueOf(eventName.toUpperCase());
    	Results result = obelixStub.getResults(eventType);
    	this.printCurrentResult(eventType, result);
    }
    
    private void getMedalTally() throws RemoteException {
    	String teamName = getInput("Team name");
    	NationCategories nation = NationCategories.valueOf(teamName.toUpperCase());
    	Tally medalTally = this.obelixStub.getMedalTally(nation);
    	this.printCurrentTally(nation, medalTally);
    }
    
    private void getCurrentScore() throws RemoteException {
    	String eventName = getInput("Event Name");
    	List<Athlete> scores = this.obelixStub.getCurrentScores(EventCategories.valueOf(eventName.toUpperCase()));
    	printCurrentScore(EventCategories.valueOf(eventName.toUpperCase()), scores);
    }

    
	private void printCurrentResult(EventCategories eventName, Results result)
	{
		String header = String.format("Results for %s.", eventName.getCategory());
    	this.printToConsole(header, result.convertToList(), null);
	}
	
	private void printCurrentTally(NationCategories teamName, Tally medalTally)
	{
		String header = String.format("Medal Tally for %s.", teamName.getCategory());
    	this.printToConsole(header, medalTally.convertToList(), null);
	}

	private void printCurrentScore(EventCategories eventName, List<Athlete> scores){
    	String header = String.format("Scores for %s.", eventName.getCategory());
    	List<Printable> printList = new ArrayList<Printable>();
    	for(Athlete athlete : scores){
    		printList.add(athlete);
    	}
    	this.printToConsole(header, printList, null);

    }
    
	@Override
	public void updateScores(EventCategories eventName, List<Athlete> scores) throws RemoteException {
		printCurrentScore(eventName, scores);
	}

	@Override
	public void updateResults(EventCategories eventName, Results result) throws RemoteException {
		printCurrentResult(eventName, result);
	}
	
	
	private synchronized void printToConsole(String header, List<Printable> printList, String footer){
		if(header != null)
			System.out.println(header);
		
		if(printList != null)
		{
		for(Printable printObject:printList){
			printObject.printContents();
			}
		}
		if(footer != null)
		System.out.println(footer);

		System.out.println();

	}
}