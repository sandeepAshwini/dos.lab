package sim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Random;

import base.EventCategories;
import base.NationCategories;
import base.OlympicException;
import client.Tablet;

public class TabletTester implements Runnable
{
	private Tablet tabletInstance;
	private static Random rand = new Random();
	private static int SLEEP_INTERVAL = 5000;
	private int numRequests;
	private static int counter = 0;
	
	public TabletTester(Tablet tabletInstance) throws IOException{
		this.tabletInstance = tabletInstance;
		try {
			this.tabletInstance.setOut("./output/TabletTester" + counter++ + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		numRequests = rand.nextInt(10) + 5;
		
		
	}
	
	public EventCategories getEventType(){
		EventCategories[] events  = EventCategories.values();
		return events[rand.nextInt(events.length)];
	}
	
	public NationCategories getNation(){
		NationCategories[] nations  = NationCategories.values();
		return nations[rand.nextInt(nations.length)];
	}

	@Override
	public void run() {
		try{
			for(int i = 0; i < numRequests; i++)
			{
				int requestNumber = rand.nextInt(2);
				switch(requestNumber){
				case 0: this.tabletInstance.getResults(this.getEventType());
						break;
				case 1: this.tabletInstance.getMedalTally(this.getNation());
						break;
				case 2: //this.tabletInstance.getCurrentScore(this.getEventType());
					break;
				case 3: //this.tabletInstance.subscribeTo(this.getEventType());
					break;
				}
			
				Thread.currentThread().sleep(SLEEP_INTERVAL);
				
			}
			
			
			}catch(RemoteException e){
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
}


