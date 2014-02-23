package server;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import base.Athlete;
import base.Event;


public class Cacophonix implements CacophonixInterface {
	
	private static String HOST;
	private static String CACOPHONIX_SERVER_NAME = "Cacophonix";
	private static String OBELIX_SERVER_NAME = "Obelix";
	private ObelixInterface clientStub;
	
	public Cacophonix() {
	}
	
	public Cacophonix(ObelixInterface clientStub) {
		this.clientStub = clientStub;
	}

	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException {
		if (clientStub != null) {
			clientStub.updateResultsAndTallies(simulatedEvent);
		}		
	}
	
	public void updateCurrentScores(Event simulatedEvent, List<Athlete> currentScores) throws RemoteException {
		if (clientStub != null) {
			clientStub.updateCurrentScores(simulatedEvent.getName(), currentScores);
		}		
	}	
	
	public static void main(String args[]) {
		HOST = (args.length < 1) ? null : args[0];
		Cacophonix cacophonixInstance = new Cacophonix();
		ObelixInterface clientStub = cacophonixInstance.setupClientInstance();
		cacophonixInstance.setupServerInstance(clientStub);
	}
	
	private void setupServerInstance(ObelixInterface clientStub) {
		Registry registry = null;
    	CacophonixInterface serverStub = null;
        
        try {
        	serverStub = (CacophonixInterface) UnicastRemoteObject.exportObject(new Cacophonix(clientStub), 0);
            registry = LocateRegistry.getRegistry(HOST);
            registry.rebind(CACOPHONIX_SERVER_NAME, serverStub);
            System.err.println("Cacophonix ready");
        } catch (Exception e) {
            System.err.println("Cacophonix exception: " + e.toString());
            e.printStackTrace();
        }
   	}
	
	private ObelixInterface setupClientInstance() {
		Registry registry = null;
		ObelixInterface clientStub = null;
		
		try {
			registry = LocateRegistry.getRegistry(HOST);
	        clientStub = (ObelixInterface) registry.lookup(OBELIX_SERVER_NAME);
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		return clientStub;
    }
}
