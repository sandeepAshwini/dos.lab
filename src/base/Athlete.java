package base;
import java.util.Random;
import java.util.UUID;

public class Athlete implements Comparable<Athlete>{
	private static int scoreRange = 10;
	
	private NationCategories nationality;
	private EventCategories event;
	private int score;
	private String name;
	
	public Athlete(EventCategories participatingEvent){
		Random rand = new Random();
		int number = rand.nextInt(NationCategories.values().length);
		this.nationality = NationCategories.values()[number];
		this.score = rand.nextInt(scoreRange);
		this.event = participatingEvent;
		this.name = UUID.randomUUID().toString();

	}

	@Override
	public int compareTo(Athlete competitor) {
		return -(this.score - competitor.score);		
	}
	
	public NationCategories getNationality(){
		return this.nationality;
	}
	
}
