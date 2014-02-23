package base;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import server.Cacophonix;
import server.CacophonixInterface;


public class Games {
	
	private ArrayList<Event> events;
	private String venue;
	private String year;
	private int currentEvent;
	
	public Games(String venue, String year){
		this.events = new ArrayList<Event>();
		this.year = year;
		this.venue = venue;
		currentEvent = 0;

		for(EventCategories eventName : EventCategories.values())
		{
			this.events.add(new Event(eventName));
		}
	}
	
	public String getGameVenue(){
		return this.venue;
	}
	public String getGameYear(){
		return this.year;
	}
	
	public ArrayList<Event> getEvents(){
		return this.events;
	}
	
	public Event simulateNextEvent(){
			Event currentEvent = events.get(this.currentEvent++);
			currentEvent.simulateEvent();
			return currentEvent;
	}
	
	public Event getNextEvent(){
		return this.events.get(this.currentEvent);
	}
	
	
	public void printGameIntro()
	{
		System.out.println("Welcome to the Stone Olympics of " + this.year + " at " + this.venue + ".");
	}
	
	public static void main(String[] args){
		int timeDelay = 10;
		Games game = new Games("Pompeii", "48 BC");
		int numEvents = game.events.size();
		String host = (args.length < 1) ? null : args[0];
		String SERVER_NAME = "Cacophonix";
		
		game.printGameIntro();
		
        try {
        	Registry registry = LocateRegistry.getRegistry();
            CacophonixInterface stub = (CacophonixInterface) registry.lookup(SERVER_NAME);
            for(int i = 0; i < numEvents; i++)
    		{
    			Event simulatedEvent = game.simulateNextEvent();
    			stub.updateScoresAndTallies(simulatedEvent);
    			Thread.currentThread().sleep(timeDelay*1000);
    		}
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}
}
