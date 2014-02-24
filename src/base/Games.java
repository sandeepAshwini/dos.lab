package base;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

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
		Thread thread = new Thread(currentEvent);
		thread.start();
		return currentEvent;
	}
	
	public Event getNextEvent(){
		return this.events.get(this.currentEvent);
	}
	
	
	public void printGameIntro()
	{
		System.out.println("Welcome to the Stone Olympics of " + this.year + " at " + this.venue + ".");
	}
	
	public static void main(String[] args)throws OlympicException{
		long TIME_DELAY = 20*1000;
		long SLEEP_DURATION = (long) 5.1*1000;
		Games game = new Games("Pompeii", "48 BC");
		int NUM_EVENTS = game.events.size();
		String HOST = (args.length < 1) ? null : args[0];
		String SERVER_NAME = "Cacophonix";
		
		game.printGameIntro();
		try {
        	Registry registry = LocateRegistry.getRegistry(HOST);
            CacophonixInterface stub = (CacophonixInterface) registry.lookup(SERVER_NAME);
            for(int i = 0; i < NUM_EVENTS; i++)
    		{
        		Event simulatedEvent = game.simulateNextEvent();
        		System.out.println(simulatedEvent.getName().getCategory());
    			if(simulatedEvent != null)
    			{
    				while(!simulatedEvent.isCompleted())
    				{
    					ArrayList<Athlete> currentScores = simulatedEvent.getScores();
    					stub.updateCurrentScores(simulatedEvent, currentScores);
    					Thread.currentThread().sleep(SLEEP_DURATION);
    				}
    				stub.updateScoresAndTallies(simulatedEvent);
    			}
    			Thread.currentThread().sleep(TIME_DELAY);
    		}
        }catch(NotBoundException e){
        	throw new OlympicException("Cannot find Cacophonix.", e);
        }catch(RemoteException e){
        	throw new OlympicException("Error while broadcasting.", e);
        }
        catch (InterruptedException e) {
        	throw new OlympicException("Games Interrupted.", e);
        }
	}
}
