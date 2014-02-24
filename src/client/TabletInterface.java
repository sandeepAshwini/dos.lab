package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import base.Athlete;
import base.EventCategories;
import base.Results;

public interface TabletInterface extends Remote {
	public void updateScores(EventCategories eventName, List<Athlete> scores) throws RemoteException;
	public void updateResults(EventCategories eventName, Results result) throws RemoteException;
}
