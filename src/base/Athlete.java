package base;
import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class Athlete implements Comparable<Athlete>, Serializable{
	private static final long serialVersionUID = 6637345322731549058L;
	private static int scoreRange = 10;
	private static Random randomNumberGenerator = new Random();
	
	private NationCategories nationality;
	private EventCategories event;
	private int score;
	private String name;
	
	public Athlete(EventCategories participatingEvent){
		int number = randomNumberGenerator.nextInt(NationCategories.values().length);
		this.nationality = NationCategories.values()[number];
		this.score = randomNumberGenerator.nextInt(scoreRange);
		this.event = participatingEvent;
		this.name = UUID.randomUUID().toString();

	}
	
	public void incrementScore(){
		int increment = randomNumberGenerator.nextInt(scoreRange);
		this.score += increment;
	}

	@Override
	public int compareTo(Athlete competitor) {
		return -(this.score - competitor.score);		
	}
	
	public NationCategories getNationality(){
		return this.nationality;
	}
	
	public void printScore(){
		System.out.printf("Name : %s \t Nationality : %s \t Score : %d. \n", this.name, this.nationality.getCategory(), this.score);
	}
	
}
