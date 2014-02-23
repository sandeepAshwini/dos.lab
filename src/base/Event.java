package base;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Event implements Serializable, Runnable{
	private static final long serialVersionUID = -9092827493794079435L;
	private static int numberOfMedals = MedalCategories.values().length;
	private static int EVENT_LEGS = 5;
	private static long INTERVAL = 5 * 1000;
	
	private EventCategories eventName;
	private Results result;
	private int numberOfParticipants;
	private ArrayList<Athlete> athletes;
	private boolean isCompleted;
	
	
	public Event(EventCategories eventName){
		this.eventName = eventName;
		this.result = new Results();
		this.isCompleted = false;
		setScores();
	}

	private void setScores(){
		Random rand = new Random();
		this.numberOfParticipants = rand.nextInt(7) + 3;
		this.athletes = new ArrayList<Athlete>();
		for(int i = 0; i < numberOfParticipants; i++)
		{
			this.athletes.add(new Athlete(this.eventName));
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
	
	public void run(){
		int count = 0;
		
		try{
		while(count++ < EVENT_LEGS){
			updateScores();
			Thread.currentThread().sleep(INTERVAL);
		}
		
		Collections.sort(this.athletes);
		ArrayList<NationCategories> winners = new ArrayList<NationCategories>();
		for(int i = 0; i < numberOfMedals; i++){
			winners.add(this.athletes.get(i).getNationality());
		}
		this.result.updateWinners(winners);	
		this.isCompleted = true;
		}catch(InterruptedException e){
			System.out.println("Event Interrupted by Vesuvius.");
			e.printStackTrace();
		}
		
	}	
	
	private synchronized void updateScores(){
		for(Athlete athlete : this.athletes){
			athlete.incrementScore();
		}
	}
	
	public synchronized ArrayList<Athlete> getScores(){
		Collections.sort(this.athletes);
		return this.athletes;
	}
	
	public boolean isCompleted()
	{
		return this.isCompleted;
	}
	
}