package base;
import java.util.Collections;
import java.util.Random;
import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable{
	private static final long serialVersionUID = -9092827493794079435L;
	private static int numberOfMedals = MedalCategories.values().length;
	
	private EventCategories eventName;
	private Results result;
	private int numberOfParticipants;
	private ArrayList<Athlete> scores;
	
	public Event(EventCategories eventName){
		this.eventName = eventName;
		this.result = new Results();
	}

	private void setScores(){
		Random rand = new Random();
		this.numberOfParticipants = rand.nextInt(10);
		this.scores = new ArrayList<Athlete>();
		for(int i = 0; i < numberOfParticipants; i++)
		{
			this.scores.add(new Athlete(this.eventName));
		}
		
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
	
	public void simulateEvent(){
		Collections.sort(this.scores);
		ArrayList<NationCategories> winners = new ArrayList<NationCategories>();
		for(int i = 0; i < numberOfMedals; i++){
			winners.add(this.scores.get(i).getNationality());
		}
		this.result.updateWinners(winners);
	
	}
	
}