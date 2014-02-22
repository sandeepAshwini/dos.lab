package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import base.Event;

public interface ObelixInterface extends Remote {
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException ;
}
