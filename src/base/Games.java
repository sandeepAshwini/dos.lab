package base;
import java.util.ArrayList;

public class Games {
	private static EventCategories eventCategories;
	private static NationCategories nationCategories;
	
	private ArrayList<Event> events;
	private ArrayList<Team> participants;
	private String venue;
	private String year;
	private int currentEvent;
	
	public Games(String venue, String year){
		this.events = new ArrayList<Event>();
		this.participants = new ArrayList<Team>();
		this.year = year;
		this.venue = venue;
		currentEvent = 0;

		for(EventCategories eventName : EventCategories.values())
		{
			this.events.add(new Event(eventName));
		}
	
		for(NationCategories teamName : NationCategories.values())
		{
			this.participants.add(new Team(teamName));
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
	
	public ArrayList<Team> getTeams(){
		return this.participants;
	}
	
	public Event simulateNextEvent(){
			Event currentEvent = events.get(this.currentEvent++);
			currentEvent.simulateEvent(participants);
			return currentEvent;
	}
	
	public Event getNextEvent(){
		return this.events.get(this.currentEvent);
	}
	
	
	public void printGameIntro()
	{
		System.out.println("Welcome to the Stone Olympics of " + this.year + " at " + this.venue);
	}
	
	public static void main(String[] args){
		int timeDelay = 10;
		//Integer.parseInt(args[1]);
		Games game = new Games("Pompeii", "48 BC");

		int numEvents = game.events.size();
		
		game.printGameIntro();
		
		for(int i = 0; i < numEvents; i++)
		{
			Event simulatedEvent = game.simulateNextEvent();
			
			try{
				Thread.currentThread().sleep(timeDelay*1000);
				
			}catch(Exception e){
				System.out.println("Vesuvius is erupting. Games Abandoned.");
				System.exit(0);
			}
			
		}
		System.out.println();
		System.out.println("Final Medal Tallies");
		for(Team team:game.getTeams())
		{
			team.printMedalTally();
			System.out.println();
			
		}
		
	}
}
