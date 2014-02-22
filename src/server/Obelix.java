package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import base.Event;
import base.EventCategories;
import base.MedalCategories;
import base.NationCategories;
import base.Results;
import base.Tally;

public class Obelix implements ObelixInterface {
	
	private static Set<Event> completedEvents;
	private static Map<NationCategories, Tally> medalTallies;
	
	public Obelix() {
		completedEvents = new HashSet<Event>();
		medalTallies = new HashMap<NationCategories, Tally>();
		for (NationCategories nation : NationCategories.values()) {
			medalTallies.put(nation, new Tally());
		}
	}
	
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException {
		updateScores(simulatedEvent);
		updateMedalTallies(simulatedEvent.getResult());
	}
	
	private void updateScores(Event completedEvent) {
		completedEvents.add(completedEvent);
	}
	
	private void updateMedalTallies(Results eventResult) {
		for(MedalCategories medalType : MedalCategories.values()) {
			medalTallies.get(eventResult.getTeam(medalType)).incrementTally(medalType);
		}		
	}
	
	public Results getScores(EventCategories eventName) {
		Results eventResult = null;
		
		for(Event event : completedEvents) {
			if(event.getName() == eventName) {
				eventResult = event.getResult();
				break;
			}
		}
		
		return eventResult;
	}
	
	public static void main(String args[])throws Exception {
        
		// Bind the remote object's stub in the registry
    	Registry registry = null;
    	String SERVER_NAME = "Obelix";
    
        ObelixInterface stub = (ObelixInterface) UnicastRemoteObject.exportObject(new Obelix(), 0);
        
        try {        	
            registry = LocateRegistry.getRegistry();
            registry.bind(SERVER_NAME, stub);
            System.err.println("Obelix ready");            
        } catch (Exception e) {
            System.err.println("Obelix exception: " + e.toString());
            e.printStackTrace();
            if(registry != null) {
            	registry.rebind(SERVER_NAME, stub);
        	}            
        }
	}
}