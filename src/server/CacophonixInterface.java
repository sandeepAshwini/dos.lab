package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import base.Event;

public interface CacophonixInterface extends Remote {
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException;
}
