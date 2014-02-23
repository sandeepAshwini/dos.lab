package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import base.Athlete;
import base.Event;
import base.EventCategories;
import base.NationCategories;
import base.Results;
import base.Tally;

public interface ObelixInterface extends Remote {
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException ;
	public void updateCurrentScores(EventCategories eventType, ArrayList<Athlete> currentScores)throws RemoteException;
	public Tally getMedalTally(NationCategories teamName) throws RemoteException;
	public Results getScores(EventCategories eventName) throws RemoteException;
	public ArrayList<Athlete> getCurrentScores(EventCategories eventName)throws RemoteException;
		
}
