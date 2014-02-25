package server;

import java.rmi.NotBoundException;
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
import java.util.concurrent.Callable;

import base.Athlete;
import base.Event;
import base.EventCategories;
import base.MedalCategories;
import base.NationCategories;
import base.Results;
import base.Tally;
import client.TabletInterface;

public class Obelix implements ObelixInterface {
	
	private Set<Event> completedEvents;
	private Map<NationCategories, Tally> medalTallies;	
	private Map<EventCategories, Subscription> subscriptionMap;
	private Map<EventCategories, ArrayList<Athlete>> scores;
	private Map<String, String> subscriberHostMap;
	
	public Obelix() {
		this.completedEvents = new HashSet<Event>();
		this.medalTallies = new HashMap<NationCategories, Tally>();
		this.scores = new HashMap<EventCategories, ArrayList<Athlete>>();
		this.subscriptionMap = new HashMap<EventCategories, Subscription>();
		this.subscriberHostMap = new HashMap<String, String>();

		for (NationCategories nation : NationCategories.values()) {
			this.medalTallies.put(nation, new Tally());
		}
	}
	
	public void updateResultsAndTallies(Event simulatedEvent) throws RemoteException {
		updateResults(simulatedEvent);
		updateMedalTallies(simulatedEvent.getResult());
	}
	
	private void updateResults(Event completedEvent) {
		pushResults(completedEvent);
		synchronized(this.completedEvents) {
			this.completedEvents.add(completedEvent);
		}		
	}
	
	private void updateMedalTallies(Results eventResult) {
		synchronized(this.medalTallies) {
			for(MedalCategories medalType : MedalCategories.values()) {
				this.medalTallies.get(eventResult.getTeam(medalType)).incrementTally(medalType);
			}
		}				
	}
	
	public void updateCurrentScores(EventCategories eventName, List<Athlete> currentScores) {
		pushCurrentScores(eventName, currentScores);
		synchronized(this.scores) {
			this.scores.put(eventName, (ArrayList<Athlete>) currentScores);
		}		
	}
	
	private void pushCurrentScores(final EventCategories eventName, final List<Athlete> currentScores) {
		Thread scoreThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendScoresToSubscribers(eventName, currentScores);
			}
		}, "Score Update Thread");
		scoreThread.start();
	}
	
	private void pushResults(final Event completedEvent) {
		Thread resultThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendResultsToSubscribers(completedEvent.getName(), completedEvent.getResult());
			}
		}, "Result Update Thread");
		resultThread.start();
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
		else {
			return null;
		}
		
	}
	public Tally getMedalTally(NationCategories teamName) {
		synchronized(this.medalTallies){
			return this.medalTallies.get(teamName);
		}
	}
	
	public void registerClient(String clientID, String clientHost, EventCategories eventName) {
		
		Subscription subscription = null;
		
		synchronized(this.subscriptionMap) {
			if(this.subscriptionMap.containsKey(eventName)) {
				subscription = this.subscriptionMap.get(eventName);
			} else {
				subscription = new Subscription();
				subscription.setEventName(eventName);
				this.subscriptionMap.put(eventName, subscription);
			}
			
			subscription.addSubscriber(clientID);
		}
		synchronized(this.subscriberHostMap) {
			this.subscriberHostMap.put(clientID, clientHost);
		}
	}
	
	private void sendScoresToSubscribers(EventCategories eventName, List<Athlete> currentScores) {
		Registry registry = null;
		Subscription subscription = null;
		
		synchronized(this.subscriptionMap) {
			subscription = this.subscriptionMap.get(eventName);
		}

		if(subscription == null) {
			return;
		}
		
		synchronized(this.subscriberHostMap) {
			for (String subscriber : subscription.getSubscribers()) {
				try {
					registry = LocateRegistry.getRegistry(this.subscriberHostMap.get(subscriber));
					TabletInterface tabletStub = (TabletInterface) registry.lookup(subscriber);
					tabletStub.updateScores(eventName, currentScores);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendResultsToSubscribers(EventCategories eventName, Results result) {
		Registry registry = null;
		Subscription subscription = null;
		
		synchronized(this.subscriptionMap) {
			subscription = this.subscriptionMap.remove(eventName);
		}		
		
		if(subscription == null) {
			return;
		}
		
		synchronized(this.subscriberHostMap) {
			for (String subscriber : subscription.getSubscribers()) {
				try {
					registry = LocateRegistry.getRegistry(this.subscriberHostMap.get(subscriber));
					TabletInterface tabletStub = (TabletInterface) registry.lookup(subscriber);
					tabletStub.updateResults(eventName, result);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		}
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

class ResultHandler implements Callable<Results> {
	
	private Set<Event> completedEvents;
	private EventCategories queriedEventName;
	
	ResultHandler() {}
	
	ResultHandler(Set<Event> completedEvents, EventCategories eventName) {
		this.completedEvents = completedEvents;
		this.queriedEventName = eventName;
	}

	@Override
	public Results call() throws Exception {
		Results eventResult = null;
		
		for(Event event : this.completedEvents) {
			if(event.getName() == this.queriedEventName) {
				eventResult = event.getResult();
				break;
			}
		}
		
		return eventResult;
	}
	
}