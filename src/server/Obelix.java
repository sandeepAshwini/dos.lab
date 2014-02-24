package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import base.Athlete;
import base.Event;
import base.EventCategories;
import base.MedalCategories;
import base.NationCategories;
import base.Results;
import base.Tally;

public class Obelix implements ObelixInterface {
	
	private Set<Event> completedEvents;
	private Map<NationCategories, Tally> medalTallies;	
	private Map<String, Subscriber> subscribers = new HashMap<String, Subscriber>();
	private Map<EventCategories, ArrayList<Athlete>> scores;
	
	public Obelix() {
		this.completedEvents = new HashSet<Event>();
		this.medalTallies = new HashMap<NationCategories, Tally>();
		this.scores = new HashMap<EventCategories, ArrayList<Athlete>>();

		for (NationCategories nation : NationCategories.values()) {
			this.medalTallies.put(nation, new Tally());
		}
	}
	
	public void updateResultsAndTallies(Event simulatedEvent) throws RemoteException {
		updateResults(simulatedEvent);
		updateMedalTallies(simulatedEvent.getResult());
	}
	
	private void updateResults(Event completedEvent) {
		this.completedEvents.add(completedEvent);
	}
	
	public void updateCurrentScores(final EventCategories eventName, final List<Athlete> currentScores) {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendScoresToSubscribers(eventName, currentScores);
			}
		});
		thread.start();
		this.scores.put(eventName, (ArrayList<Athlete>) currentScores);
	}
	
	private void updateMedalTallies(Results eventResult) {
		for(MedalCategories medalType : MedalCategories.values()) {
			this.medalTallies.get(eventResult.getTeam(medalType)).incrementTally(medalType);
		}		
	}
	
	private void sendScoresToSubscribers(EventCategories eventName, List<Athlete> currentScores) {
		
	}
	
	public Results getResults(EventCategories eventName) {
		Results eventResult = null;
		synchronized(this.completedEvents){
			for(Event event : this.completedEvents) {
				if(event.getName() == eventName) {
					eventResult = event.getResult();
					break;
				}
			}
		}
		return eventResult;
	}
	
	public List<Athlete> getCurrentScores(EventCategories eventName)throws RemoteException
	{	
		if(this.scores.containsKey(eventName))
		{
			synchronized(this.scores){
				return this.scores.get(eventName);
			}
		}
		else{
			return null;
		}
		
	}
	public Tally getMedalTally(NationCategories teamName) {
		synchronized(this.medalTallies){
			return this.medalTallies.get(teamName);
		}
	}
	
	public synchronized void registerClient(String clientID, EventCategories eventName) {
		Subscriber subscriber = null;
		if(subscribers.containsKey(clientID)) {
			subscriber = new Subscriber();
			subscriber.setClientID(clientID);
		} else {
			subscriber = subscribers.get(clientID);
		}
		
		subscriber.subscribeTo(eventName);		
	}
	
	public static void main(String args[])throws Exception {
        
		// Bind the remote object's stub in the registry
    	Registry registry = null;
    	String SERVER_NAME = "Obelix";
    	
    	String host = (args.length < 1) ? null : args[0];
        ObelixInterface serverStub = (ObelixInterface) UnicastRemoteObject.exportObject(new Obelix(), 0);

        try {        	
            registry = LocateRegistry.getRegistry(host);
            registry.rebind(SERVER_NAME, serverStub);
            System.err.println("Obelix ready");            
        } catch (Exception e) {
            System.err.println("Obelix exception: " + e.toString());
            e.printStackTrace();
        }
	}
}