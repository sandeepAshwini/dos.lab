package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import base.Athlete;
import base.Event;

public interface CacophonixInterface extends Remote {
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException;
	public void updateCurrentScores(Event simulatedEvent, ArrayList<Athlete> currentScores) throws RemoteException;
}
