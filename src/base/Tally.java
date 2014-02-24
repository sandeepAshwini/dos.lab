package base;

import java.io.Serializable;
import java.util.HashMap;

public class Tally extends Printable implements Serializable {
	private static MedalCategories medalTypes;
	
	private HashMap<MedalCategories, Integer> medalTally;
	
	public Tally(){
		this.medalTally = new HashMap<MedalCategories, Integer>();
		for (MedalCategories category : medalTypes.values())
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
			System.out.print(medal.getCategory() + " : " + medalTally.get(medal) + " ");
		}
		System.out.println();
	}

	public void printContents() {
		printMedalTally();
	}
	
}
