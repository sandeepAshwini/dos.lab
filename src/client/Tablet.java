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
/**
 * Implementation of the client tablet.
 * @author sandeep
 *
 */
public class Tablet implements TabletInterface {

    public Tablet() {}
    
    public Tablet(ObelixInterface obelixStub) {
    	this.obelixStub = obelixStub;
    }
    /**
     * Members specifying the server name(Obelix)
     * and the base client identifier.
     */
    private static String SERVER_NAME = "Obelix";
    private static String CLIENT_BASE_NAME = "Client_";
    
    /**
     * The server stub and the client ID(for event subscription).
     */
    private ObelixInterface obelixStub;
    private String clientID;
    
    /**
     * Control Variable to switch between subscribe and 
     * query modes.
     */
    private volatile boolean resumeMenuLoop = false;

    /**
     * Main method sets up the client tablet.
     * Sets up the server stub for client pull.
     * Also sets up the client as a server for server push mode.
     * Finally calls the menu loop from where the server can be 
     * queried or events subscribed to.
     * @param args
     */
    public static void main(String[] args) {
    	String obelixHost = (args.length < 1) ? null : args[0];
    	String tabletHost = (args.length < 2) ? null : args[1];
    	Tablet tabletInstance = getTabletInstance(obelixHost, tabletHost);
    	tabletInstance.menuLoop();
    }
    
    /**
     * Simple command line interface to interact with the user.
     */
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
	    			case 4: this.subscribeTo();
	    					this.waitToResume();
	    					this.resumeMenuLoop = false; break;
	    			default: this.printToConsole("Not a valid menu option.", null, null);
	    		}
	    	}
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Sets up the Obelix server stub.
     * Also sets up the Tablet server stub for server push mode.
     * @param obelixHost
     * @param tabletHost
     * @return
     */
    private static Tablet getTabletInstance(String obelixHost, String tabletHost) {

    	ObelixInterface obelixStub = connectToObelix(obelixHost);
    	Tablet tabletInstance = new Tablet(obelixStub);
    	tabletInstance.setupTabletServer(tabletHost);
    	    	
    	return tabletInstance;
    }

    /**
     * Returns the Server(Obelix) stub after doing the required lookup 
     * in the RMI Registry and creating the stub.
     * @param obelixHost
     * @return
     */
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
    
    /**
     * Sets up the tablet as a server to receive updates from Obelix.
     * @param host
     */
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
    
    /**
     * Allows subscription to events.
     * Takes the Event Name as user input from the CLI.
     */
    private void subscribeTo() {
    	EventCategories eventName = EventCategories.valueOf(getInput("Event name?"));
    	subscribeTo(eventName);
    }
    
    private void subscribeTo(EventCategories eventName) {
    	try {
			obelixStub.registerClient(clientID, null, eventName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Method to retrieve input from user.
     * Synchronized as results should not be written into the console 
     * while waiting for user input.
     * @param msg
     * @return
     */
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
    
    /**
     * Queries the server for the results of a particular event.
     * The event is accepted as input from the CLI.
     * @throws RemoteException
     */
    private void getResults() throws RemoteException {
    	String eventName = getInput("Event name");
    	EventCategories eventType = EventCategories.valueOf(eventName.toUpperCase());
    	Results result = obelixStub.getResults(eventType);
    	this.printCurrentResult(eventType, result);
    }
    
    /**
     * Queries the server for the medal tally of a particular team.
     * The team name is accepted as input from the CLI.
     * @throws RemoteException
     */
    
    private void getMedalTally() throws RemoteException {
    	String teamName = getInput("Team name");
    	NationCategories nation = NationCategories.valueOf(teamName.toUpperCase());
    	Tally medalTally = this.obelixStub.getMedalTally(nation);
    	this.printCurrentTally(nation, medalTally);
    }
    
    /**
     * Queries the server for the current scores of a particular event.
     * The event is accepted as input from the CLI.
     * @throws RemoteException
     */
    
    private void getCurrentScore() throws RemoteException {
    	String eventName = getInput("Event Name");
    	List<Athlete> scores = this.obelixStub.getCurrentScores(EventCategories.valueOf(eventName.toUpperCase()));
    	printCurrentScore(EventCategories.valueOf(eventName.toUpperCase()), scores);
    }

	/**
	 * Pretty prints the results of the specified event to 
	 * the console.
	 * @param eventName
	 * @param result
	 */
    private void printCurrentResult(EventCategories eventName, Results result)
	{
		String header = String.format("Results for %s.", eventName.getCategory());
    	this.printToConsole(header, result.convertToList(), null);
	}
	
	/**
	 * Pretty prints the medal tally of the specified team to 
	 * the console.
	 * @param eventName
	 * @param result
	 */
    private void printCurrentTally(NationCategories teamName, Tally medalTally)
	{
		String header = String.format("Medal Tally for %s.", teamName.getCategory());
    	this.printToConsole(header, medalTally.convertToList(), null);
	}

    /**
	 * Pretty prints the current scores of the specified event to 
	 * the console.
	 * @param eventName
	 * @param result
	 */
	private void printCurrentScore(EventCategories eventName, List<Athlete> scores){
    	String header = String.format("Scores for %s.", eventName.getCategory());
    	List<Printable> printList = new ArrayList<Printable>();
    	for(Athlete athlete : scores){
    		printList.add(athlete);
    	}
    	this.printToConsole(header, printList, null);

    }
    
	/**
	 * Calls made by the Obelix server for server push mode.
	 */
	/**
	 * The server calls this function when the scores of a subscribed event change.
	 * When called, the updated scores are printed to console.
	 */
	@Override
	public void updateScores(EventCategories eventName, List<Athlete> scores) throws RemoteException {
		System.out.println(eventName.getCategory());
		printCurrentScore(eventName, scores);
	}
	
	/**
	 * The server calls this function when the final results of a subscribed event are
	 * available, that is, the event is completed.
	 * When called, the final results are printed to console.
	 * Then, the subscription is effectively over and the user is taken back to the 
	 * menu.
	 */
	@Override
	public void updateResults(EventCategories eventName, Results result) throws RemoteException {
		printCurrentResult(eventName, result);
		this.resumeMenuLoop = true;
	}
	
	/**
	 * Synchronized method to print to console.
	 * This method needs to be thread safe as only one thing should be printed 
	 * to the console at any given time.
	 * Also, no utput should be dumped when we are waiting for user input.
	 * @param header
	 * @param printList
	 * @param footer
	 */
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
	
	/**
	 * Wait loop entered into when a subscription is registered.
	 * Only subscription updates are allowed to be printed durung this time.
	 * When the event is completed, the user is once again shown the menu.
	 */
	private void waitToResume() {
		while(this.resumeMenuLoop == false);
	}
}