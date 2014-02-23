package server;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import base.Event;


public class Cacophonix implements CacophonixInterface {
	
	private static String HOST;
	private static String CACOPHONIX_SERVER_NAME = "Cacophonix";
	private static String OBELIX_SERVER_NAME = "Obelix";
	private ObelixInterface clientStub;
	
	public Cacophonix() {
	}

	public void updateScoresAndTallies(Event simulatedEvent) throws RemoteException {
		System.out.println(clientStub);
		if (clientStub != null) {
			simulatedEvent.printResults();
			clientStub.updateScoresAndTallies(simulatedEvent);
		}		
	}
	
	public static void main(String args[]) {
		HOST = (args.length < 1) ? null : args[0];
		Cacophonix cacophonixInstance = new Cacophonix();
		cacophonixInstance.clientStub = cacophonixInstance.setupClientInstance();
		cacophonixInstance.setupServerInstance();
	}
	
	private void setupServerInstance() {
		Registry registry = null;
    	CacophonixInterface stub = null;
        
        try {
        	stub = (CacophonixInterface) UnicastRemoteObject.exportObject(new Cacophonix(), 0);
            registry = LocateRegistry.getRegistry();
            registry.bind(CACOPHONIX_SERVER_NAME, stub);
            System.err.println("Cacophonix ready");
        } catch (Exception e) {
            System.err.println("Cacophonix exception: " + e.toString());
            e.printStackTrace();
        }
   	}
	
	private ObelixInterface setupClientInstance() {
		Registry registry = null;
		ObelixInterface stub = null;
		
		try {
			registry = LocateRegistry.getRegistry();
	        stub = (ObelixInterface) registry.lookup(OBELIX_SERVER_NAME);
		} catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		return stub;
    }
}
