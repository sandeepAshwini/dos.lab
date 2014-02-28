package server;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import base.Athlete;
import base.Event;

/**
 * Encapsulates the functions of Cacophonix.
 * Receives updates from the Olympic Games and relays them on to 
 * Obelix by singing.
 * @author sandeep
 *
 */
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

	/**
	 * Remote method called by Games when there is an update to
	 * the results of any event, that is when the event is completed.
	 * This in turn causes these results to be relayed on to Obelix,
	 * whose database is accordingly updated.
	 */
	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException {
		if (clientStub != null) {
			clientStub.updateResultsAndTallies(simulatedEvent);
		}		
	}
	
	/**
	 * Remote method called by Games when there is an update to scores
	 * in some event. The updates are relayed on to Obelix whose 
	 * database is accordingly updated.
	 */
	public void updateCurrentScores(Event simulatedEvent, List<Athlete> currentScores) throws RemoteException {
		if (clientStub != null) {
			clientStub.updateCurrentScores(simulatedEvent.getName(), currentScores);
		}		
	}	
	
	/**
	 * Sets up client and server functions of Cacophonix.
	 * @param args
	 */
	public static void main(String args[]) {
		HOST = (args.length < 1) ? null : args[0];
		Cacophonix cacophonixInstance = new Cacophonix();
		ObelixInterface clientStub = cacophonixInstance.setupClientInstance();
		cacophonixInstance.setupServerInstance(clientStub);
	}
	
	/**
	 * Sets Cacophonix up as a client for the Games class, enabling it to receive scores
	 * and updates.
	 * @param clientStub
	 */
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
	
	/**
	 * Sets Cacophonix up as a server for Obelix. This allows Cacophonix to 'sing'
	 * any received updates and hence inform Obelix of the same.
	 * @return
	 */
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
