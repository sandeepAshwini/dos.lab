package base;
import java.util.ArrayList;
import java.util.Random;

public class Event {
	private static int numberOfMedals = 3;
	
	private EventCategories eventName;
	private Results result;
	
	public Event(EventCategories eventName, Results result){
		this.eventName = eventName;
		this.result = result;
	}
	
	public Event(EventCategories eventName){
		this.eventName = eventName;
		this.result = new Results();
	}
	
	public void updateResults(Results result){
		this.result = result;		
	}
	
	public Results getResult(){
		return this.result;
	}
	
	public EventCategories getName(){
		return this.eventName;
	}
	
	public void printResults(){
		System.out.println("Event : " + this.eventName.getCategory());
		this.result.printResults();
	} 
	
	public void simulateEvent(ArrayList<Team> participants){
		ArrayList<Team> winners = new ArrayList<Team>();
		Random r = new Random();
		
		for(int i = 0; i < numberOfMedals; i++)
		{
			int key = r.nextInt(numberOfMedals);
			winners.add(participants.get(key));
		}
		
		System.out.println();
		Results eventResults = new Results(winners);
		this.updateResults(eventResults);		
	}	
}