package base;

import java.util.HashMap;

public class Tally {
	private HashMap<MedalCategories, Integer> medalTally;
	
	public Tally(){
		this.medalTally = new HashMap<MedalCategories, Integer>();
		for (MedalCategories category : MedalCategories.values())
		{
			medalTally.put(category, 0);
		}
	}
	
	public void incrementTally(MedalCategories category){
		int currentCount = this.medalTally.get(category);
		this.medalTally.put(category, currentCount + 1);
	}
	
	public void printMedalTally(){
		for(MedalCategories medal:medalTally.keySet()){
			System.out.print(medal.getCategory() + " : " + medalTally.get(medal));
		}
	}
	
}
