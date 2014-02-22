package base;
import java.util.ArrayList;
import java.util.HashMap;
public class Results {
	private HashMap<MedalCategories, Team> winners;
	
	public Results(ArrayList<Team> winners){
		this.winners = new HashMap<MedalCategories, Team>();
		updateWinners(winners);
	}

	public Results(){
		this.winners = new HashMap<MedalCategories, Team>();
	}
	
	public Team getTeam(MedalCategories medalType) {
		return winners.get(medalType);
	}
	
	public void updateWinners(ArrayList<Team> winners)
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
			System.out.println(medal.getCategory() + " : " + winners.get(medal).getTeamName().getCategory());
		}
	}
	
}
