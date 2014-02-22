package pkg1;
import java.util.HashMap;
import java.util.ArrayList;
public class Results {
	private static MedalCategories medalTypes;
	private HashMap<MedalCategories, Team> winners;
	
	public Results(ArrayList<Team> winners){
		this.winners = new HashMap<MedalCategories, Team>();
		updateWinners(winners);
	}

	public Results(){
		this.winners = new HashMap<MedalCategories, Team>();
	}
	
	public ArrayList<MedalCategories> getPosition(Team team){
		ArrayList<MedalCategories> medalsWon = new ArrayList<MedalCategories>();
		for(MedalCategories medal:winners.keySet())
		{
			if(winners.get(medal) == team){
				medalsWon.add(medal);
			}
		}
		return medalsWon;
	}
	
	public void updateWinners(ArrayList<Team> winners)
	{
		int i = 0;
		for(MedalCategories medalType:medalTypes.values())
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
