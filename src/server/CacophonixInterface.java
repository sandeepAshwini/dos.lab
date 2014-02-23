package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import base.Athlete;
import base.Event;

public interface CacophonixInterface extends Remote {
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException;
	public void updateCurrentScores(Event simulatedEvent, List<Athlete> currentScores) throws RemoteException;
}
