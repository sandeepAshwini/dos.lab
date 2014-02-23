package server;

import java.util.ArrayList;
import java.util.List;

import base.EventCategories;

public class Subscriber {
	private String clientID;
	private List<EventCategories> subscribedEvents;
	
	public String getClientID() {
		return this.clientID;
	}
	
	public List<EventCategories> getSubscribedEvents() {
		return this.subscribedEvents;
	}
	
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	public void subscribeTo(EventCategories eventName) {
		if(subscribedEvents == null) {
			subscribedEvents = new ArrayList<EventCategories>();
		}
		subscribedEvents.add(eventName);
	}
}
