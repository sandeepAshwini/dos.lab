package base;
import java.util.ArrayList;

public class Team {
	private Tally medalTally;
	private NationCategories teamName;
	
	public Team(NationCategories teamName){
		this.medalTally = new Tally();
		this.teamName = teamName;
	}
	
	public Team(){
		this.medalTally = new Tally();
	}
	
	public void setTeamName(NationCategories name){
		this.teamName = name;
	}
	
	public void incrementMedalTally(ArrayList<MedalCategories> categories){
		for(MedalCategories medal : categories){
			this.medalTally.incrementTally(medal);
		}
	}
	
	public NationCategories getTeamName(){
		return this.teamName;
	}
	
	public Tally getMedalTally(){
		return this.medalTally;
	}
	
	public void printMedalTally(){
		this.medalTally.printMedalTally();
	}
	
}
