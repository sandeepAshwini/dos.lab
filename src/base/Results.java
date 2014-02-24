package base;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Results extends Printable implements Serializable{
	private static final long serialVersionUID = -7858027301096005662L;
	private HashMap<MedalCategories, NationCategories> winners;
	
	public Results(ArrayList<NationCategories> winners){
		this.winners = new HashMap<MedalCategories, NationCategories>();
		updateWinners(winners);
	}

	public Results(){
		this.winners = new HashMap<MedalCategories, NationCategories>();
	}
	
	public NationCategories getTeam(MedalCategories medalType) {
		return winners.get(medalType);
	}
	
	public void updateWinners(ArrayList<NationCategories> winners)
	{
		int i = 0;
		for(MedalCategories medalType : MedalCategories.values())
		{
			this.winners.put(medalType, winners.get(i));
			i++;
		}
		
	}
	
	public void printResults(){
		for(MedalCategories medal : winners.keySet()){
			System.out.println(medal.getCategory() + " : " + winners.get(medal).getCategory());
		}
	}

	public void printContents() {
		printResults();
		
	}
	
}
