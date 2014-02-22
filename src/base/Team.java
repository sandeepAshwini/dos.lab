package base;

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
	
	public void incrementMedalTally(MedalCategories medalType){
		this.medalTally.incrementTally(medalType);
	}
	
	public NationCategories getTeamName(){
		return this.teamName;
	}
	
	public Tally getMedalTally(){
		return this.medalTally;
	}
	
	public void printMedalTally(){
		System.out.println("Team : " + this.getTeamName().getCategory());
		this.medalTally.printMedalTally();
	}
	
}
