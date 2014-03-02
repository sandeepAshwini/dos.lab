package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import base.EventCategories;

public class Subscription {
	private EventCategories eventName;
	private Set<String> subscribers;
	
	public EventCategories getEventName() {
		return this.eventName;
	}
	
	public Set<String> getSubscribers() {
		return this.subscribers;
	}
	
	public void setEventName(EventCategories eventName) {
		this.eventName = eventName;
	}
	
	public void addSubscriber(String subscriber) {
		if(subscribers == null) {
			subscribers = new HashSet<String>();
		}
		subscribers.add(subscriber);
	}
}
