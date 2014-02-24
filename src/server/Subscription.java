package server;

import java.util.ArrayList;
import java.util.List;

import base.EventCategories;

public class Subscription {
	private EventCategories eventName;
	private List<String> subscribers;
	
	public EventCategories getEventName() {
		return this.eventName;
	}
	
	public List<String> getSubscribers() {
		return this.subscribers;
	}
	
	public void setEventName(EventCategories eventName) {
		this.eventName = eventName;
	}
	
	public void addSubscriber(String subscriber) {
		if(subscribers == null) {
			subscribers = new ArrayList<String>();
		}
		subscribers.add(subscriber);
	}
}
