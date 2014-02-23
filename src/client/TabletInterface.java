package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import base.Athlete;
import base.Results;

public interface TabletInterface extends Remote {
	public void updateScores(List<Athlete> scores) throws RemoteException;
	public void updateResults(Results result) throws RemoteException;
}
